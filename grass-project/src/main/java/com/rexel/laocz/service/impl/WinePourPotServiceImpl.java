package com.rexel.laocz.service.impl;

import com.rexel.common.exception.CustomException;
import com.rexel.common.utils.SequenceUtils;
import com.rexel.laocz.domain.LaoczBatchPotteryMapping;
import com.rexel.laocz.domain.LaoczPotteryAltarManagement;
import com.rexel.laocz.domain.LaoczWineDetails;
import com.rexel.laocz.domain.dto.WineEntryDTO;
import com.rexel.laocz.domain.dto.WinePourPotApplyDTO;
import com.rexel.laocz.enums.OperationTypeEnum;
import com.rexel.laocz.enums.RealStatusEnum;
import com.rexel.laocz.enums.WineDetailTypeEnum;
import com.rexel.laocz.service.WineAbstract;
import com.rexel.laocz.service.WineEntryApplyService;
import com.rexel.laocz.service.WineOutService;
import com.rexel.laocz.service.WinePourPotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @ClassName WinePourPotServiceImpl
 * @Description
 * @Author 孟开通
 * @Date 2024/4/1 15:38
 **/
@Service
public class WinePourPotServiceImpl extends WineAbstract implements WinePourPotService {
    @Autowired
    private WineOutService wineOutService;
    @Autowired
    private WineEntryApplyService wineEntryApplyService;

    /**
     * 倒坛申请
     *
     * @param winePourPotApplyDTO 倒坛申请参数：申请重量，出酒陶坛罐ID，入酒陶坛罐ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void winePourPotApply(WinePourPotApplyDTO winePourPotApplyDTO) {
        if (winePourPotApplyDTO.getInPotteryAltarId().equals(winePourPotApplyDTO.getOutPotteryAltarId())) {
            throw new CustomException("出酒陶坛罐和入酒陶坛罐不能是同一个");
        }
        List<LaoczBatchPotteryMapping> list = iLaoczBatchPotteryMappingService.lambdaQuery()
                .in(LaoczBatchPotteryMapping::getPotteryAltarId, Arrays.asList(winePourPotApplyDTO.getInPotteryAltarId(),
                        winePourPotApplyDTO.getOutPotteryAltarId())).list();
        LaoczBatchPotteryMapping outMapping = list.stream().filter(e -> e.getPotteryAltarId().equals(winePourPotApplyDTO.getOutPotteryAltarId())).findFirst().orElse(null);
        LaoczBatchPotteryMapping inMapping = list.stream().filter(e -> e.getPotteryAltarId().equals(winePourPotApplyDTO.getInPotteryAltarId())).findFirst().orElse(null);
        //检查陶坛罐实时状态
        winePourApplyCheck(winePourPotApplyDTO, inMapping);


        //新增 工单表（流程审批创建）,然后需要把laocz_liquor_batch的liquor_batch_id字段作为业务id来和流程关联
        String workId = SequenceUtils.nextId().toString();
        //生成busy_id
        String busyId = SequenceUtils.nextId().toString();
        //新增laocz_wine_operations
        saveLaoczWineOperations(busyId, workId, OperationTypeEnum.POUR_POT);
        //新增laocz_wine_details
        saveLaoczWineDetails(busyId, workId, outMapping, winePourPotApplyDTO);
        //新增 laocz_batch_pottery_mapping
        savalaoczBatchPotteryMapping(outMapping, inMapping, winePourPotApplyDTO);

        //更新实时表
        super.updatePotteryMappingState(list.stream().map(wineOutApplyDTO -> {
            LaoczBatchPotteryMapping laoczBatchPotteryMapping = new LaoczBatchPotteryMapping();
            laoczBatchPotteryMapping.setMappingId(wineOutApplyDTO.getMappingId());
            laoczBatchPotteryMapping.setRealStatus(RealStatusEnum.OCCUPY.getCode());
            return laoczBatchPotteryMapping;
        }).collect(Collectors.toList()));

    }

    /**
     * 倒坛出酒开始获取重量
     *
     * @param wineDetailsId 酒操作业务详情id
     * @return 重量
     */
    @Override
    public String winePourPotOutStart(Long wineDetailsId) {
        LaoczWineDetails wineDetailsById = getWineDetailsById(wineDetailsId);
        checkDetailType(wineDetailsById.getDetailType(), WineDetailTypeEnum.POUR_IN);
        return wineOutService.wineOutStart(wineDetailsId);
    }

    /**
     * 倒坛出酒
     *
     * @param wineDetailsId 酒操作业务详情id
     * @return 入酒的id
     */
    @Override
    public Long winePourPotOut(Long wineDetailsId) {
        //查询同一个业务id下的倒坛入的酒，如果出酒成功就把id返回给前端
        LaoczWineDetails laoczWineDetails = iLaoczWineDetailsService.lambdaQuery().eq(LaoczWineDetails::getWineDetailsId, wineDetailsId).one();
        LaoczWineDetails inWine = iLaoczWineDetailsService.lambdaQuery().eq(LaoczWineDetails::getBusyId, laoczWineDetails.getBusyId())
                .eq(LaoczWineDetails::getDetailType, WineDetailTypeEnum.POUR_IN.getCode()).one();
        wineOutService.wineOutFinish(wineDetailsId);
        return inWine.getWineDetailsId();
    }

    /**
     * 倒坛入酒
     *
     * @param wineDetailsId 酒操作业务详情id
     */
    @Override
    public void winePourPotIn(Long wineDetailsId) {
        //入酒前判断，出酒是否已经完成
        wineIncheck(wineDetailsId);
        //入酒完成
        wineEntryApplyService.wineEntryFinish(wineDetailsId);
    }

    /**
     * 倒坛入酒开始
     *
     * @param wineEntryDTO 酒入坛参数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void winePourPotInStart(WineEntryDTO wineEntryDTO) {
        //入酒前判断，出酒是否已经完成
        wineIncheck(wineEntryDTO.getWineDetailsId());
        //入酒开始
        wineEntryApplyService.wineIn(wineEntryDTO);
    }

    /**
     * 入酒前判断，出酒是否已经完成
     *
     * @param wineDetailsId 酒操作业务详情id
     */
    private void wineIncheck(Long wineDetailsId) {
        LaoczWineDetails wineDetailsById = getWineDetailsById(wineDetailsId);
        if (Objects.isNull(wineDetailsById)) {
            throw new CustomException("酒操作业务详情不存在");
        }
        checkDetailType(wineDetailsById.getDetailType(), WineDetailTypeEnum.POUR_IN);
        Integer count = iLaoczWineDetailsService.lambdaQuery().eq(LaoczWineDetails::getBusyId, wineDetailsById.getBusyId()).count();
        if (count >= 2) {
            throw new CustomException("请先操作倒坛出酒");
        }
    }

    /**
     * 保存实时表，如果入酒陶坛罐不存在，则新增
     *
     * @param outMapping          出酒陶坛罐
     * @param inMapping           入酒陶坛罐
     * @param winePourPotApplyDTO 倒坛申请参数
     */
    private void savalaoczBatchPotteryMapping(LaoczBatchPotteryMapping outMapping, LaoczBatchPotteryMapping inMapping, WinePourPotApplyDTO winePourPotApplyDTO) {
        if (inMapping != null) {
            return;
        }
        LaoczBatchPotteryMapping laoczBatchPotteryMapping = new LaoczBatchPotteryMapping();
        //批次id
        laoczBatchPotteryMapping.setLiquorBatchId(outMapping.getLiquorBatchId());
        //陶坛罐id
        laoczBatchPotteryMapping.setPotteryAltarId(winePourPotApplyDTO.getInPotteryAltarId());
        //运行状态
        laoczBatchPotteryMapping.setRealStatus(RealStatusEnum.OCCUPY.getCode());
        iLaoczBatchPotteryMappingService.save(laoczBatchPotteryMapping);
    }

    /**
     * 倒坛申请校验
     *
     * @param winePourPotApplyDTO 倒坛申请参数
     * @param inMapping           入酒陶坛罐实时关系表
     */
    private void winePourApplyCheck(WinePourPotApplyDTO winePourPotApplyDTO, LaoczBatchPotteryMapping inMapping) {
        List<LaoczPotteryAltarManagement> managements = iLaoczPotteryAltarManagementService.lambdaQuery().in(LaoczPotteryAltarManagement::getPotteryAltarId, Arrays.asList(winePourPotApplyDTO.getInPotteryAltarId(), winePourPotApplyDTO.getOutPotteryAltarId())).list();
        LaoczPotteryAltarManagement outPotteryAltar = managements.stream().filter(e -> e.getPotteryAltarId().equals(winePourPotApplyDTO.getOutPotteryAltarId())).findFirst().orElse(null);
        LaoczPotteryAltarManagement inPotteryAltar = managements.stream().filter(e -> e.getPotteryAltarId().equals(winePourPotApplyDTO.getInPotteryAltarId())).findFirst().orElse(null);
        if (outPotteryAltar == null) {
            throw new CustomException("选择的出酒陶坛罐不存在");
        }
        if (inPotteryAltar == null) {
            throw new CustomException("选择的入酒陶坛罐不存在");
        }
        potteryAltarNumberCheck(outPotteryAltar.getPotteryAltarNumber(), true);

        if (inMapping == null) {
            //如果是空陶坛就先判断重量，在判断状态
            Double applyWeight = winePourPotApplyDTO.getApplyWeight();
            if (applyWeight > inPotteryAltar.getPotteryAltarFullAltarWeight()) {
                throw new CustomException("入酒陶坛罐容量不足");
            }
            potteryAltarNumberCheck(inPotteryAltar.getPotteryAltarNumber(), false);
        } else {
            //有酒的校验
            //判断入酒的陶坛罐子是否是存储状态
            //判断入酒的陶坛罐子是否是同一批次
            //判断入酒的陶坛罐子剩余容量是否可以盛下

            //判断容量
            if (winePourPotApplyDTO.getApplyWeight() > (inPotteryAltar.getPotteryAltarFullAltarWeight() - inMapping.getActualWeight())) {
                throw new CustomException("入酒陶坛罐容量不足");
            }
            //判断批次
            if (!inMapping.getLiquorBatchId().equals(outPotteryAltar.getLiquorBatchId())) {
                throw new CustomException("入酒陶坛罐不是同一批次");
            }

            RealStatusEnum realStatus = RealStatusEnum.getEnumByCode(inMapping.getRealStatus());
            super.checkRealStatus(realStatus);
        }
    }

    /**
     * 保存laocz_wine_details
     *
     * @param busyId              busy_id
     * @param workId              work_id
     * @param outMapping          出酒陶坛罐
     * @param winePourPotApplyDTO 倒坛申请参数
     */
    private void saveLaoczWineDetails(String busyId, String workId, LaoczBatchPotteryMapping outMapping, WinePourPotApplyDTO winePourPotApplyDTO) {
        if (outMapping == null) {
            throw new CustomException("出酒陶坛罐不存在");
        }
        LaoczWineDetails out = buildLaoczWineDetails(busyId, workId, outMapping.getLiquorBatchId(), outMapping.getPotteryAltarId(), winePourPotApplyDTO.getApplyWeight(), WineDetailTypeEnum.POUR_OUT, null);
        LaoczWineDetails in = buildLaoczWineDetails(busyId, workId, outMapping.getLiquorBatchId(), winePourPotApplyDTO.getInPotteryAltarId(), winePourPotApplyDTO.getApplyWeight(), WineDetailTypeEnum.POUR_IN, null);
        List<LaoczWineDetails> list = new ArrayList<>();
        list.add(out);
        list.add(in);
        iLaoczWineDetailsService.saveBatch(list);
    }


}
