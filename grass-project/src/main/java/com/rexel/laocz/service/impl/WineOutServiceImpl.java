package com.rexel.laocz.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.rexel.common.exception.CustomException;
import com.rexel.common.utils.SequenceUtils;
import com.rexel.laocz.constant.WineConstants;
import com.rexel.laocz.constant.WinePointConstants;
import com.rexel.laocz.domain.LaoczBatchPotteryMapping;
import com.rexel.laocz.domain.LaoczWineDetails;
import com.rexel.laocz.domain.dto.WineOutApplyDTO;
import com.rexel.laocz.domain.dto.WineOutStartDTO;
import com.rexel.laocz.domain.vo.WineDetailPointVO;
import com.rexel.laocz.dview.DviewUtils;
import com.rexel.laocz.enums.*;
import com.rexel.laocz.service.WineAbstract;
import com.rexel.laocz.service.WineOutService;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @ClassName WineOutServiceImpl
 * @Description
 * @Author 孟开通
 * @Date 2024/3/11 14:26
 **/
@Service
public class WineOutServiceImpl extends WineAbstract implements WineOutService {
    /**
     * 出酒申请
     *
     * @param list 出酒申请参数：陶坛罐ID，申请重量
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void wineOutApply(List<WineOutApplyDTO> list) {
        //查询 使用中，有酒，必须是存储状态
        //二维码扫描:有没有这个罐子，是否在使用中，必须有酒，必须是存储状态
        //手动输入：有没有这个罐子，是否在使用中，必须有酒,必须是存储状态，申请重量必须小于等于实际重量

        //检查实时表，检查申请的是否都在实时表里面存在，检查申请的重量是否小于等于实时表里面的重量
        List<LaoczBatchPotteryMapping> batchPotteryMappings = check(list);


        //生成busy_id
        String busyId = SequenceUtils.nextId().toString();
        //新增流程实例
        String processInstanceId = saveProcessInstancesService(busyId, WineProcessDefinitionKeyEnum.OUT_WINE);
        //新增laocz_wine_operations
        saveLaoczWineOperations(busyId, processInstanceId, OperationTypeEnum.WINE_OUT);
        //新增laocz_wine_details
        saveLaoczWineDetails(list, busyId, processInstanceId, batchPotteryMappings);

        //更新陶坛状态 为占用，目的为了不让别的业务申请
        super.updatePotteryMappingState(batchPotteryMappings.stream().map(wineOutApplyDTO -> {
            LaoczBatchPotteryMapping laoczBatchPotteryMapping = new LaoczBatchPotteryMapping();
            laoczBatchPotteryMapping.setMappingId(wineOutApplyDTO.getMappingId());
            laoczBatchPotteryMapping.setRealStatus(RealStatusEnum.OCCUPY.getCode());
            return laoczBatchPotteryMapping;
        }).collect(Collectors.toList()));
    }

    @NotNull
    private List<LaoczBatchPotteryMapping> check(List<WineOutApplyDTO> list) {
        if (list.isEmpty()) {
            throw new CustomException("出酒申请参数不能为空");
        }
        List<Long> potteryList = list.stream().map(WineOutApplyDTO::getPotteryAltarId).collect(Collectors.toList());
        Map<Long, Double> map = list.stream().collect(Collectors.toMap(WineOutApplyDTO::getPotteryAltarId, WineOutApplyDTO::getApplyWeight));
        List<LaoczBatchPotteryMapping> batchPotteryMappings = iLaoczBatchPotteryMappingService.lambdaQuery().in(LaoczBatchPotteryMapping::getPotteryAltarId, potteryList).list();
        if (batchPotteryMappings == null || potteryList.size() != batchPotteryMappings.size()) {
            throw new CustomException("陶坛申请出酒错误，请重新申请");
        }
        for (LaoczBatchPotteryMapping batchPotteryMapping : batchPotteryMappings) {
            if (map.containsKey(batchPotteryMapping.getPotteryAltarId())) {
                if (map.get(batchPotteryMapping.getPotteryAltarId()) > batchPotteryMapping.getActualWeight()) {
                    throw new CustomException("申请重量大于实际重量，请重新申请");
                }
            }
            //校验陶坛状态
            checkRealStatus(RealStatusEnum.getEnumByCode(batchPotteryMapping.getRealStatus()));
        }
        return batchPotteryMappings;
    }


    /**
     * 出酒操作，称重罐称重量
     *
     * @param wineOutStartDTO 酒操作业务详情id
     * @return 称重量
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public LaoczWineDetails wineOutStart(WineOutStartDTO wineOutStartDTO) {

        Long wineDetailsId = wineOutStartDTO.getWineDetailsId();

        LaoczWineDetails wineDetails = iLaoczWineDetailsService.getById(wineDetailsId);
        if(wineDetails==null){
            throw new CustomException("请刷新后重试");
        }
        //查询称重罐测点
        List<WineDetailPointVO> weighingTankPointVOList = laoczWineDetailsMapper.selectWineDetailWeighingTankPointVOList(wineDetailsId);
        WineDetailPointVO zlOut = getZlOut(weighingTankPointVOList);
        String eventStatus = WineConstants.SUCCESS;
        try {
            String pointValue = DviewUtils.queryPointValue(zlOut.getPointId(), zlOut.getPointType());

            if(wineOutStartDTO.getType().equals(WineOutTypeEnum.WINE_OUT_BEFORE.getCode())){
                if(wineDetails.getAfterTime()!=null||wineDetails.getAfterWeight()!=null){
                    throw new CustomException("出酒前已经称重，请勿重复称重");
                }
                wineDetails.setBeforeWeight(Double.parseDouble(pointValue));
                wineDetails.setBeforeTime(new Date());
            }else if(wineOutStartDTO.getType().equals(WineOutTypeEnum.WINE_OUT_AFTER.getCode())){
                wineDetails.setAfterWeight(Double.parseDouble(pointValue));
                wineDetails.setAfterTime(new Date());

                BigDecimal subtract = BigDecimal.valueOf(wineDetails.getAfterWeight()).subtract(BigDecimal.valueOf(wineDetails.getBeforeWeight()));

                wineDetails.setWeighingTankWeight(subtract.doubleValue());
            }
            iLaoczWineDetailsService.updateById(wineDetails);
            super.updatePotteryMappingState(wineDetails.getPotteryAltarId(), RealStatusEnum.WINE_OUT);
            return wineDetails;
        } catch (IOException e) {
            eventStatus = WineConstants.FAIL;
            throw new CustomException("采集称重罐重量失败，请重试");
        } finally {
            String finalEventStatus = eventStatus;
            threadPoolTaskScheduler.execute(() -> saveWineEvent(wineDetails, weighingTankPointVOList, finalEventStatus));
        }

    }

    private void saveWineEvent(LaoczWineDetails wineDetails, List<WineDetailPointVO> weighingTankPointVOList, String finalEventStatus) {
        List<String> weightPoints = weighingTankPointVOList.stream().map(WineDetailPointVO::getPointId).collect(Collectors.toList());
        super.saveWineEvent(wineDetails, WineOperationTypeEnum.WEIGH.getValue(), weightPoints, finalEventStatus);
    }

    /**
     * 出酒操作完成
     *
     * @param wineDetailsId 酒操作业务详情id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void wineOutFinish(Long wineDetailsId) {
        //更新实时表重量（为申请重量），如果出完就删除数据
        //酒操作业务表+酒操作业务详情表，删除并备份
        //更新酒操作业务详情表的出酒时间
        //新增酒历史表\
        LaoczWineDetails laoczWineDetails = iLaoczWineDetailsService.getById(wineDetailsId);
        //判断是否已经有了称重罐重量以及是否已经完成
        winOutfinishCheck(laoczWineDetails);
        //更新陶坛实时关系表，入酒，更新为存储，更新实际重量（为称重罐的实际重量）
        LaoczBatchPotteryMapping laoczBatchPotteryMapping = super.updatePotteryMappingState(laoczWineDetails.getPotteryAltarId(), "-",
                laoczWineDetails.getPotteryAltarApplyWeight());
        //备份酒操作业务表
        super.backupWineDetails(laoczWineDetails);
        //新增数据到历史表
        super.saveHistory(laoczWineDetails);
        //查询当前业务id还有没有正在完成的任务，如果没有了，就备份酒操作业务表
        super.taskVerify(laoczWineDetails.getBusyId());
        //如果陶坛实际重量小于等于0，删除实时关系表
        potteryMappingFinish(laoczBatchPotteryMapping);
    }

    /**
     * 出酒操作完成 陶坛实际重量小于等于0，删除实时关系表
     *
     * @param laoczBatchPotteryMapping 陶坛实时关系表
     */
    private void potteryMappingFinish(LaoczBatchPotteryMapping laoczBatchPotteryMapping) {
        if (laoczBatchPotteryMapping.getActualWeight() <= 0) {
            iLaoczBatchPotteryMappingService.removeById(laoczBatchPotteryMapping);
        }
    }


    /**
     * 判断是否已经有了称重罐重量以及是否已经完成
     * @param laoczWineDetails 酒操作业务详情
     */
    private void winOutfinishCheck(LaoczWineDetails laoczWineDetails) {
        if (laoczWineDetails.getWeighingTankWeight() == null) {
            throw new CustomException("称重罐重量未获取");
        }
    }

    /**
     * 获取称重罐测点
     * @param weighingTankPointVOList 称重罐测点列表
     * @return 称重罐测点
     */
    private WineDetailPointVO getZlOut(List<WineDetailPointVO> weighingTankPointVOList) {
        if(CollectionUtil.isEmpty(weighingTankPointVOList)){
            throw new CustomException("未找到称重罐测点");
        }
        for (WineDetailPointVO weighingTankPointVO : weighingTankPointVOList) {
            if (WinePointConstants.ZL_OUT.equals(weighingTankPointVO.getUseMark())) {
                if (StrUtil.isEmpty(weighingTankPointVO.getPointId()) || StrUtil.isEmpty(weighingTankPointVO.getPointType())) {
                    throw new CustomException("称重罐测点信息不全,请联系管理员");
                }
                return weighingTankPointVO;
            }
        }
        throw new CustomException("未找到称重罐测点");
    }

    /**
     * 保存laocz_wine_details
     * @param wineOutApplyDTOS 出酒申请参数
     * @param busyId 业务id
     * @param workId 工单id
     * @param batchPotteryMappings 陶坛关系表
     */
    private void saveLaoczWineDetails(List<WineOutApplyDTO> wineOutApplyDTOS, String busyId, String workId, List<LaoczBatchPotteryMapping> batchPotteryMappings) {
        Map<Long, LaoczBatchPotteryMapping> map = batchPotteryMappings.stream().collect(Collectors.toMap(LaoczBatchPotteryMapping::getPotteryAltarId, Function.identity()));
        List<LaoczWineDetails> list = new ArrayList<>();
        for (WineOutApplyDTO wineOutApplyDTO : wineOutApplyDTOS) {
            Long potteryAltarId = wineOutApplyDTO.getPotteryAltarId();
            LaoczBatchPotteryMapping laoczBatchPotteryMapping = map.get(potteryAltarId);
            list.add(buildLaoczWineDetails(busyId, workId, laoczBatchPotteryMapping.getLiquorBatchId(), potteryAltarId, wineOutApplyDTO.getApplyWeight(), WineDetailTypeEnum.OUT_WINE, null));
        }
        iLaoczWineDetailsService.saveBatch(list);
    }


}
