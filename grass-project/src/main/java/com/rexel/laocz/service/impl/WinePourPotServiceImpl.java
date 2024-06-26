package com.rexel.laocz.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.rexel.bpm.enums.BpmTaskStatusEnum;
import com.rexel.common.exception.CustomException;
import com.rexel.laocz.domain.LaoczBatchPotteryMapping;
import com.rexel.laocz.domain.LaoczPotteryAltarManagement;
import com.rexel.laocz.domain.LaoczWineDetails;
import com.rexel.laocz.domain.dto.*;
import com.rexel.laocz.domain.vo.WineOperaPotteryAltarVO;
import com.rexel.laocz.enums.OperationTypeEnum;
import com.rexel.laocz.enums.RealStatusEnum;
import com.rexel.laocz.enums.WineDetailTypeEnum;
import com.rexel.laocz.enums.WineProcessDefinitionKeyEnum;
import com.rexel.laocz.service.WineAbstract;
import com.rexel.laocz.service.WineEntryApplyService;
import com.rexel.laocz.service.WineOutService;
import com.rexel.laocz.service.WinePourPotService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName WinePourPotServiceImpl
 * @Description
 * @Author 孟开通
 * @Date 2024/4/1 15:38
 **/
@Service
@Slf4j
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
        //申请前验证
        applyCheck(winePourPotApplyDTO.getOutPotteryAltarId(), winePourPotApplyDTO.getApplyWeight(), winePourPotApplyDTO.getInPotteryAltarId());

        List<LaoczBatchPotteryMapping> list = iLaoczBatchPotteryMappingService.lambdaQuery()
                .in(LaoczBatchPotteryMapping::getPotteryAltarId, Arrays.asList(winePourPotApplyDTO.getInPotteryAltarId(),
                        winePourPotApplyDTO.getOutPotteryAltarId())).list();
        LaoczBatchPotteryMapping outMapping = list.stream().filter(e -> e.getPotteryAltarId().equals(winePourPotApplyDTO.getOutPotteryAltarId())).findFirst().orElseThrow(() -> new CustomException("出酒陶坛罐不存在"));
        LaoczBatchPotteryMapping inMapping = list.stream().filter(e -> e.getPotteryAltarId().equals(winePourPotApplyDTO.getInPotteryAltarId())).findFirst().orElse(null);

        //生成busy_id
        String busyId = super.getBusyId();

        Collection<String> values = iLaoczLiquorBatchService.getLiquorManagementNameMap(Collections.singletonList(outMapping.getLiquorBatchId())).values();
        String liquorNames = String.join(",", values);
        //新增流程实例
        String processInstanceId = saveProcessInstancesService(busyId
                , super.getVariables(outMapping.getLiquorBatchId(), OperationTypeEnum.POUR_POT.getValue(),liquorNames)
                , WineProcessDefinitionKeyEnum.POUR_TANK);
        //新增laocz_wine_operations
        saveLaoczWineOperations(busyId, processInstanceId, OperationTypeEnum.POUR_POT);
        //新增laocz_wine_details
        saveLaoczWineDetails(busyId, processInstanceId, outMapping, winePourPotApplyDTO);
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
     * 倒坛申请前验证
     * @param outPotteryAltarId 出酒陶坛id
     * @param applyWeight 申请重量
     * @param inPotteryAltarId 入酒陶坛id
     */
    private void applyCheck(Long outPotteryAltarId, Double applyWeight, Long inPotteryAltarId) {
        if (outPotteryAltarId.equals(inPotteryAltarId)) {
            throw new CustomException("出酒陶坛罐和入酒陶坛罐不能是同一个");
        }
        check(outPotteryAltarId, applyWeight, inPotteryAltarId);
    }

    /**
     * 倒坛出酒开始获取重量
     *
     * @param wineOutStartDTO 酒操作业务详情id
     * @return 重量
     */
    @Override
    public LaoczWineDetails winePourPotOutStart(WineOutStartDTO wineOutStartDTO) {
        LaoczWineDetails wineDetailsById = getWineDetailsById(wineOutStartDTO.getWineDetailsId());
        //验证是否是倒坛出酒
        checkDetailType(wineDetailsById.getDetailType(), WineDetailTypeEnum.POUR_IN);
        //验证审核是否通过
        super.approvalCheck(wineDetailsById.getBusyId(), BpmTaskStatusEnum.APPROVE);
        return wineOutService.wineOutStart(wineOutStartDTO);
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
        if (laoczWineDetails == null) {
            throw new CustomException("已保存完成，请退出刷新重试");
        }
        //验证审核是否通过
        super.approvalCheck(laoczWineDetails.getBusyId(), BpmTaskStatusEnum.APPROVE);
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
     * 二维码扫描获取倒坛出酒陶坛信息
     *
     * @param potteryAltarNumber 陶坛编号
     * @return 陶坛信息
     */
    @Override
    public WineOperaPotteryAltarVO qrOutCodeScan(String potteryAltarNumber) {
        //陶坛验证，是否存在，是否被封存， 实时表 验证，是否被使用
        super.potteryAltarNumberCheck(potteryAltarNumber, true);
        WineOutPotteryAltarDTO wineOutPotteryAltarDTO = new WineOutPotteryAltarDTO();
        wineOutPotteryAltarDTO.setEqPotteryAltarNumber(potteryAltarNumber);
        List<WineOperaPotteryAltarVO> wineOperaPotteryAltarVOS = iLaoczPotteryAltarManagementService.wineOutPotteryAltarList(wineOutPotteryAltarDTO);
        if (wineOperaPotteryAltarVOS.isEmpty()) {
            throw new CustomException("系统异常，请联系管理员");
        }
        return wineOperaPotteryAltarVOS.get(0);
    }

    /**
     * 倒坛 申请前验证
     *
     * @param outPotteryAltarId 出酒陶坛id
     * @param applyWeight       申请重量
     * @param inPotteryAltarId  入酒陶坛id
     * @return 入酒陶坛罐 是否已经有酒 true 有酒 false 没酒
     */
    private boolean check(Long outPotteryAltarId, Double applyWeight, Long inPotteryAltarId) {
        if (outPotteryAltarId == null || inPotteryAltarId == null) {
            throw new CustomException("陶坛编号有误，请重新操作");
        }
        if (applyWeight == null || applyWeight <= 0) {
            throw new CustomException("重量有误，请重新操作");
        }
        List<LaoczPotteryAltarManagement> altarManagements = iLaoczPotteryAltarManagementService.lambdaQuery().in(LaoczPotteryAltarManagement::getPotteryAltarId, Arrays.asList(inPotteryAltarId, outPotteryAltarId)).list();
        LaoczPotteryAltarManagement inLaoczPotteryAltarManagement = altarManagements.stream().filter(e -> e.getPotteryAltarId().equals(inPotteryAltarId)).findFirst().orElse(null);
        LaoczPotteryAltarManagement outLaoczPotteryAltarManagement = altarManagements.stream().filter(e -> e.getPotteryAltarId().equals(outPotteryAltarId)).findFirst().orElse(null);
        if (outLaoczPotteryAltarManagement == null) {
            throw new CustomException("出酒陶坛不存在");
        }
        //出酒验证是否可以使用
        altarStateCheck(outLaoczPotteryAltarManagement);
        if (inLaoczPotteryAltarManagement == null) {
            throw new CustomException("入酒陶坛不存在");
        }
        //入酒验证是否可以使用
        altarStateCheck(inLaoczPotteryAltarManagement);

        //查询陶坛是否存在
        List<LaoczBatchPotteryMapping> list = iLaoczBatchPotteryMappingService.lambdaQuery()
                .in(LaoczBatchPotteryMapping::getPotteryAltarId, Arrays.asList(inLaoczPotteryAltarManagement.getPotteryAltarId(), outPotteryAltarId))
                .list();
        LaoczBatchPotteryMapping outMapping = list.stream().filter(e -> e.getPotteryAltarId().equals(outPotteryAltarId)).findFirst().orElse(null);
        if (outMapping == null) {
            throw new CustomException("出酒陶坛未存储酒液，操作失败");
        }
        //验证入酒实时状态是否可以出酒
        checkRealStatus(RealStatusEnum.getEnumByCode(outMapping.getRealStatus()));
        LaoczBatchPotteryMapping inMapping = list.stream().filter(e -> e.getPotteryAltarId().equals(inLaoczPotteryAltarManagement.getPotteryAltarId())).findFirst().orElse(null);
        //申请入酒的陶坛，是否已经有酒 true 有酒 false 没酒
        boolean inMappingIsNull = inMapping != null;
        //查询陶坛是否有酒，如果有酒就比较批次和重量
        //如果没有酒就没事了
        if (inMappingIsNull) {
            //验证入酒实时状态是否可以出酒
            checkRealStatus(RealStatusEnum.getEnumByCode(inMapping.getRealStatus()));
            //判断批次
            if (!inMapping.getLiquorBatchId().equals(outMapping.getLiquorBatchId())) {
                throw new CustomException("入酒陶坛罐不是同一批次");
            }
            if (inMapping.getActualWeight() + applyWeight > inLaoczPotteryAltarManagement.getPotteryAltarFullAltarWeight()) {
                throw new CustomException("入酒陶坛罐容量不足");
            }
        } else {
            if (applyWeight > inLaoczPotteryAltarManagement.getPotteryAltarFullAltarWeight()) {
                throw new CustomException("入酒陶坛罐容量不足");
            }
        }
        return inMappingIsNull;
    }

    /**
     * 二维码扫描获取倒坛出酒陶坛信息
     *
     * @param qrInCodeScanDTO 陶坛编号，出酒陶坛id，出酒重量
     * @return 陶坛信息
     */
    @Override
    public WineOperaPotteryAltarVO qrInCodeScan(QrInCodeScanDTO qrInCodeScanDTO) {
        String potteryAltarNumber = qrInCodeScanDTO.getPotteryAltarNumber();
        LaoczPotteryAltarManagement inLaoczPotteryAltarManagement = getLaoczPotteryAltarManagementByPotteryAltarNumber(potteryAltarNumber);
        boolean inMappingIsNull = check(qrInCodeScanDTO.getOutPotteryAltarId(), qrInCodeScanDTO.getOutWeight(), inLaoczPotteryAltarManagement.getPotteryAltarId());
        //查询陶坛是否有酒，如果有酒就比较批次和重量
        //如果没有酒就没事了
        if (inMappingIsNull) {
            WineOutPotteryAltarDTO wineOutPotteryAltarDTO = new WineOutPotteryAltarDTO();
            wineOutPotteryAltarDTO.setEqPotteryAltarNumber(potteryAltarNumber);
            List<WineOperaPotteryAltarVO> wineOperaPotteryAltarVOS = iLaoczPotteryAltarManagementService.wineOutPotteryAltarList(wineOutPotteryAltarDTO);
            if (wineOperaPotteryAltarVOS.isEmpty()) {
                throw new CustomException("系统异常，请联系管理员");
            }
            return wineOperaPotteryAltarVOS.get(0);
        } else {
            WineEntryPotteryAltarDTO wineEntryPotteryAltarDTO = new WineEntryPotteryAltarDTO();
            wineEntryPotteryAltarDTO.setEqPotteryAltarNumber(potteryAltarNumber);
            List<WineOperaPotteryAltarVO> wineOperaPotteryAltarVOS = iLaoczPotteryAltarManagementService.wineEntryPotteryAltarList(wineEntryPotteryAltarDTO);
            if (wineOperaPotteryAltarVOS.isEmpty()) {
                throw new CustomException("系统异常，请联系管理员");
            }
            return wineOperaPotteryAltarVOS.get(0);
        }
    }

    /**
     * 倒坛审批结束后处理，审批通过或不通过
     *
     * @param busyId 业务id
     */
    @Override
    public void updateWinePourStatus(String busyId) {
        //验证审核是否通过
        super.approvalCheck(busyId, BpmTaskStatusEnum.REJECT);
        List<LaoczWineDetails> list  = super.getDetailsInBusyId(busyId);
        if (CollectionUtil.isNotEmpty(list)) {
            LaoczWineDetails pourIn = list.stream().filter(laoczWineDetails -> laoczWineDetails.getDetailType().equals(WineDetailTypeEnum.POUR_IN.getCode())).findFirst().orElseThrow(() -> new CustomException("倒坛审核不通过后，处理数据异常"));
            LaoczWineDetails pourOut = list.stream().filter(laoczWineDetails -> laoczWineDetails.getDetailType().equals(WineDetailTypeEnum.POUR_OUT.getCode())).findFirst().orElseThrow(() -> new CustomException("倒坛审核不通过后，处理数据异常"));
            List<Long> potteryAltarIds = list.stream().map(LaoczWineDetails::getPotteryAltarId).collect(Collectors.toList());
            List<LaoczBatchPotteryMapping> mappings = iLaoczBatchPotteryMappingService.lambdaQuery()
                    .in(LaoczBatchPotteryMapping::getPotteryAltarId, potteryAltarIds).list();
            LaoczBatchPotteryMapping outMapping = mappings.stream().filter(e -> e.getPotteryAltarId().equals(pourOut.getPotteryAltarId())).findFirst().orElseThrow(() -> new CustomException("倒坛审核不通过后，处理数据异常"));
            LaoczBatchPotteryMapping inMapping = mappings.stream().filter(e -> e.getPotteryAltarId().equals(pourIn.getPotteryAltarId())).findFirst().orElseThrow(() -> new CustomException("倒坛审核不通过后，处理数据异常"));
            outMapping.setRealStatus(RealStatusEnum.STORAGE.getCode());
            iLaoczBatchPotteryMappingService.updateById(outMapping);
            iLaoczBatchPotteryMappingService.removeById(inMapping.getMappingId());
            //备份酒操作业务表
            super.backupWineDetails(list);
        }
        //laocz_wine_operations备份到his，然后删除
        taskVerify(busyId);

        //备份所有数据到laocz_wine_history，并标记为不通过
        rejectSaveHistory(busyId);
    }

    /**
     * 入酒前判断，出酒是否已经完成
     *
     * @param wineDetailsId 酒操作业务详情id
     */
    private void wineIncheck(Long wineDetailsId) {
        LaoczWineDetails wineDetailsById = getWineDetailsById(wineDetailsId);
        if (Objects.isNull(wineDetailsById)) {
            throw new CustomException("已保存完成，请退出刷新重试");
        }
        //验证审核是否通过
        super.approvalCheck(wineDetailsById.getBusyId(), BpmTaskStatusEnum.APPROVE);
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
