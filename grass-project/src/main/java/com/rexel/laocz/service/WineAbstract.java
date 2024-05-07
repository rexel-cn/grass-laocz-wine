package com.rexel.laocz.service;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson2.JSON;
import com.rexel.bpm.domain.task.BpmProcessInstanceCreateReqDTO;
import com.rexel.bpm.service.task.BpmProcessInstanceService;
import com.rexel.common.exception.CustomException;
import com.rexel.common.utils.DictUtils;
import com.rexel.common.utils.SecurityUtils;
import com.rexel.laocz.constant.WineDictConstants;
import com.rexel.laocz.domain.*;
import com.rexel.laocz.domain.dto.WineHistoryDTO;
import com.rexel.laocz.domain.dto.WineOutApplyDTO;
import com.rexel.laocz.enums.*;
import com.rexel.laocz.mapper.LaoczWineDetailsMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @ClassName WineAbstract
 * @Description
 * @Author 孟开通
 * @Date 2024/3/13 17:48
 **/
@Service
@Slf4j
public abstract class WineAbstract {
    protected static final String APPLY_LOCK = "apply_lock";

    @Autowired
    @Qualifier("laoczTtlScheduledExecutorService")
    protected ScheduledExecutorService threadPoolTaskScheduler;
    @Autowired
    protected ILaoczWineDetailsService iLaoczWineDetailsService;
    @Autowired
    protected ILaoczSamplingHistorityService iLaoczSamplingHistorityService;
    @Autowired
    protected LaoczWineDetailsMapper laoczWineDetailsMapper;
    @Autowired
    protected ILaoczWineOperationsService iLaoczWineOperationsService;
    @Autowired
    protected ILaoczWineOperationsHisService iLaoczWineOperationsHisService;
    @Autowired
    protected ILaoczWineEventService iLaoczWineEventService;
    @Autowired
    protected ILaoczWineDetailsHisService iLaoczWineDetailsHisService;
    @Autowired
    protected ILaoczBatchPotteryMappingService iLaoczBatchPotteryMappingService;
    @Autowired
    protected ILaoczWineHistoryService iLaoczWineHistoryService;
    @Autowired
    protected ILaoczPotteryAltarManagementService iLaoczPotteryAltarManagementService;
    @Resource
    private BpmProcessInstanceService processInstanceService;

    protected void saveWineEvent(LaoczWineDetails wineDetails, String Status, List<String> points, String eventStatus) {
        LaoczWineEvent laoczWineEvent = new LaoczWineEvent();
        //工单id
        laoczWineEvent.setWorkOrderId(wineDetails.getWorkOrderId());
        //业务id
        laoczWineEvent.setBusyId(wineDetails.getBusyId());
        //酒品批次号
        laoczWineEvent.setLiquorBatchId(wineDetails.getLiquorBatchId());
        //陶坛罐id
        laoczWineEvent.setPotteryAltarId(wineDetails.getPotteryAltarId());
        //事件id
        laoczWineEvent.setEventId(WineOperationTypeEnum.getNameByValue(Status));
        //事件时间
        laoczWineEvent.setEventTime(new Date());
        //事件状态
        laoczWineEvent.setEventStatus(eventStatus);
        //事件参数
        laoczWineEvent.setEventParam(JSON.toJSONString(points));
        //转换为，号分割
        laoczWineEvent.setPointArray(String.join(",", points));
        iLaoczWineEventService.save(laoczWineEvent);
    }

    /**
     * 构造LaoczWineDetails
     *
     * @param busyId         业务id
     * @param workId         工单id
     * @param liquorBatchId  酒品批次号
     * @param potteryAltarId 陶坛罐id
     * @param applyWeight    申请重量
     * @return LaoczWineDetails
     */
    protected LaoczWineDetails buildLaoczWineDetails(String busyId,
                                                     String workId,
                                                     String liquorBatchId,
                                                     Long potteryAltarId,
                                                     Double applyWeight,
                                                     WineDetailTypeEnum wineDetailTypeEnum,
                                                     String samplingPurpose) {
        LaoczWineDetails laoczWineDetails = new LaoczWineDetails();
        //业务id
        laoczWineDetails.setBusyId(busyId);
        //工单id
        laoczWineDetails.setWorkOrderId(workId);
        //酒品批次号
        laoczWineDetails.setLiquorBatchId(liquorBatchId);
        //陶坛罐id
        laoczWineDetails.setPotteryAltarId(potteryAltarId);
        //运行状态
        laoczWineDetails.setBusyStatus(WineBusyStatusEnum.NOT_STARTED.getValue());
        //申请重量
        laoczWineDetails.setPotteryAltarApplyWeight(applyWeight);
        //操作详细类型
        laoczWineDetails.setDetailType(wineDetailTypeEnum.getCode());
        //取样用途
        laoczWineDetails.setSamplingPurpose(samplingPurpose);
        return laoczWineDetails;
    }

    /**
     * 保存操作记录
     *
     * @param busyId            业务id
     * @param workId            工单id
     * @param operationTypeEnum 操作类型
     */
    protected void saveLaoczWineOperations(String busyId, String workId, OperationTypeEnum operationTypeEnum) {
        LaoczWineOperations operations = new LaoczWineOperations();
        //业务id
        operations.setBusyId(busyId);
        //工单id
        operations.setWorkOrderId(workId);
        //操作类型 1入酒 2出酒 3倒坛 4取样
        operations.setOperationType(operationTypeEnum.getValue());
        iLaoczWineOperationsService.save(operations);
    }


    protected String saveProcessInstancesService(String businessKey, WineProcessDefinitionKeyEnum wineProcessDefinitionKeyEnum){
        //创建流程实例
        return processInstanceService.createProcessInstance(
                new BpmProcessInstanceCreateReqDTO()
                        .setProcessDefinitionKey(wineProcessDefinitionKeyEnum.getKey())
                        .setBusinessKey(businessKey));
    }


    protected static void AltarStateCheck(LaoczPotteryAltarManagement laoczPotteryAltarManagement) {
        if (!Objects.equals(PotteryAltarStateEnum.USE.getCode(), laoczPotteryAltarManagement.getPotteryAltarState())) {
            throw new CustomException("陶坛罐已被封存");
        }
    }

    /**
     * 保存取样历史
     *
     * @param laoczWineDetails 酒品详情
     * @param laoczWineHistory 酒品历史
     */
    private void saveSamplingHistory(LaoczWineDetails laoczWineDetails, LaoczWineHistory laoczWineHistory) {
        //新增数据到laocz_sampling_histority
        LaoczSamplingHistority laoczSamplingHistority = new LaoczSamplingHistority();
        //工单id
        laoczSamplingHistority.setWorkOrderId(laoczWineDetails.getWorkOrderId());
        //业务标识
        laoczSamplingHistority.setBusyId(laoczWineDetails.getBusyId());
        //酒批次
        laoczSamplingHistority.setLiquorBatchId(laoczWineDetails.getLiquorBatchId());
        //陶坛罐id
        laoczSamplingHistority.setPotteryAltarId(laoczWineDetails.getPotteryAltarId());
        //取样用途
        laoczSamplingHistority.setSamplingPurpose(laoczWineHistory.getSamplingPurpose());
        //取样重量
        laoczSamplingHistority.setSamplingWeight(laoczWineDetails.getPotteryAltarApplyWeight());
        //取样时间
        laoczSamplingHistority.setSamplingDate(laoczWineHistory.getOperationTime());
        //场区名称
        laoczSamplingHistority.setAreaName(laoczWineHistory.getAreaName());
        //防火区名称
        laoczSamplingHistority.setFireZoneName(laoczWineHistory.getFireZoneName());
        //陶坛管理编号
        laoczSamplingHistority.setPotteryAltarNumber(laoczWineHistory.getPotteryAltarNumber());
        //新增取样记录表
        iLaoczSamplingHistorityService.save(laoczSamplingHistority);
    }

    /**
     * 备份酒品详情
     *
     * @param laoczWineDetails 酒品详情
     */
    protected void backupWineDetails(LaoczWineDetails laoczWineDetails) {
        if (laoczWineDetails == null) {
            throw new CustomException("业务异常，请联系管理员");
        }
        LaoczWineDetailsHis laoczWineDetailsHis = BeanUtil.copyProperties(laoczWineDetails, LaoczWineDetailsHis.class);
        laoczWineDetailsHis.setOperationTime(new Date());
        laoczWineDetailsHis.setBusyStatus(WineBusyStatusEnum.COMPLETED.getValue());
        iLaoczWineDetailsHisService.save(laoczWineDetailsHis);
        iLaoczWineDetailsService.removeById(laoczWineDetails);
    }

    protected void saveHistory(LaoczWineDetails wineDetailsId) {
        Long detailType = wineDetailsId.getDetailType();
        WineDetailTypeEnum wineDetailTypeEnum = WineDetailTypeEnum.getEnumByCode(detailType);

        WineHistoryDTO wineHistoryDTO = laoczWineDetailsMapper.selectWineHistoryDTOList(wineDetailsId.getWineDetailsId());
        LaoczWineHistory laoczWineHistory = BeanUtil.copyProperties(wineHistoryDTO, LaoczWineHistory.class);
        LaoczBatchPotteryMapping batchPotteryMapping;
        switch (wineDetailTypeEnum) {
            case IN_WINE:
                //剩余重量，因为是入酒所以剩余重量就是称重罐重量
                laoczWineHistory.setRemainingWeight(laoczWineHistory.getWeighingTankWeight());
                //因为是入酒所以，操作时间就是入酒时间
                laoczWineHistory.setStoringTime(laoczWineHistory.getOperationTime());
                //亏损重量=申请重量-称重罐重量
                laoczWineHistory.setLossWeight(laoczWineHistory.getPotteryAltarApplyWeight() - laoczWineHistory.getWeighingTankWeight());
                //入酒
                iLaoczWineHistoryService.save(laoczWineHistory);
                break;
            case OUT_WINE:
            case POUR_OUT:
                //出酒
                batchPotteryMapping = iLaoczBatchPotteryMappingService.lambdaQuery()
                        .eq(LaoczBatchPotteryMapping::getLiquorBatchId, wineDetailsId.getLiquorBatchId())
                        .eq(LaoczBatchPotteryMapping::getPotteryAltarId, wineDetailsId.getPotteryAltarId())
                        .one();
                //剩余重量
                laoczWineHistory.setRemainingWeight(batchPotteryMapping.getActualWeight());
                //入酒时间
                laoczWineHistory.setStoringTime(batchPotteryMapping.getStoringTime());
                //亏损重量=申请重量-称重罐重量
                laoczWineHistory.setLossWeight(BigDecimal.valueOf(laoczWineHistory.getPotteryAltarApplyWeight()).subtract(BigDecimal.valueOf(laoczWineHistory.getWeighingTankWeight())).doubleValue());
                //出酒历史保存
                iLaoczWineHistoryService.save(laoczWineHistory);
                break;
            case POUR_IN:
                //去历史表找到busyId一样并且是倒坛入酒的数据，查询入酒时间，对比俩个入酒时间以时间短的为基准
                LaoczWineHistory history = iLaoczWineHistoryService.lambdaQuery()
                        .eq(LaoczWineHistory::getBusyId, wineDetailsId.getBusyId())
                        .eq(LaoczWineHistory::getDetailType, WineDetailTypeEnum.POUR_OUT.getCode())
                        .one();

                batchPotteryMapping = iLaoczBatchPotteryMappingService.lambdaQuery()
                        .eq(LaoczBatchPotteryMapping::getLiquorBatchId, wineDetailsId.getLiquorBatchId())
                        .eq(LaoczBatchPotteryMapping::getPotteryAltarId, wineDetailsId.getPotteryAltarId())
                        .one();
                //剩余重量
                laoczWineHistory.setRemainingWeight(batchPotteryMapping.getActualWeight());
                //因为是倒坛入，所以入酒时间=俩个入酒时间的最短值（出酒的入酒时间，或者本身的入酒时间（如果有））
                Date outTime = history.getStoringTime();
                Date inTime = batchPotteryMapping.getStoringTime();
                laoczWineHistory.setStoringTime(outTime.before(inTime) ? inTime : outTime);
                //亏损重量=申请重量-称重罐重量
                laoczWineHistory.setLossWeight(BigDecimal.valueOf(laoczWineHistory.getPotteryAltarApplyWeight()).subtract(BigDecimal.valueOf(laoczWineHistory.getWeighingTankWeight())).doubleValue());
                //入酒
                iLaoczWineHistoryService.save(laoczWineHistory);
                //倒坛
                break;
            case SAMPLING:
                //取样
                batchPotteryMapping = iLaoczBatchPotteryMappingService.lambdaQuery()
                        .eq(LaoczBatchPotteryMapping::getLiquorBatchId, wineDetailsId.getLiquorBatchId())
                        .eq(LaoczBatchPotteryMapping::getPotteryAltarId, wineDetailsId.getPotteryAltarId())
                        .one();
                //剩余重量
                laoczWineHistory.setRemainingWeight(batchPotteryMapping.getActualWeight());
                laoczWineHistory.setStoringTime(batchPotteryMapping.getStoringTime());
                //取样用途
                laoczWineHistory.setSamplingPurpose(DictUtils.getDictLabel(WineDictConstants.SAMPLING_PURPOSE, laoczWineHistory.getSamplingPurpose()));
                iLaoczWineHistoryService.save(laoczWineHistory);
                saveSamplingHistory(wineDetailsId, laoczWineHistory);
                break;
            default:
                throw new IllegalArgumentException("Unexpected value: " + wineDetailTypeEnum);
        }
    }

    /**
     * 酒操作验证，是否已经完成所有的详情，如果已经完成就备份操作记录
     *
     * @param busyId 业务id
     */
    protected void taskVerify(String busyId) {
        Integer count = iLaoczWineDetailsService.lambdaQuery().eq(LaoczWineDetails::getBusyId, busyId).count();
        if (count <= 0) {
            LaoczWineOperations operations = iLaoczWineOperationsService.lambdaQuery().eq(LaoczWineOperations::getBusyId, busyId).one();
            LaoczWineOperationsHis laoczWineOperationsHis = BeanUtil.copyProperties(operations, LaoczWineOperationsHis.class);
            iLaoczWineOperationsHisService.save(laoczWineOperationsHis);
            iLaoczWineOperationsService.removeById(operations);
        }

    }

    /**
     * 更新陶坛罐状态
     *
     * @param potteryAltarId      陶坛罐id
     * @param operatingStatusEnum 操作状态
     */
    protected void updatePotteryMappingState(Long potteryAltarId, RealStatusEnum operatingStatusEnum) {
        iLaoczBatchPotteryMappingService.lambdaUpdate().set(LaoczBatchPotteryMapping::getRealStatus,
                operatingStatusEnum.getCode()).eq(LaoczBatchPotteryMapping::getPotteryAltarId, potteryAltarId).update();
    }

    /**
     * 批量更新陶坛罐
     *
     * @param batchPotteryMappings 陶坛罐列表
     */
    protected void updatePotteryMappingState(List<LaoczBatchPotteryMapping> batchPotteryMappings) {
        iLaoczBatchPotteryMappingService.updateBatchById(batchPotteryMappings);
    }

    /**
     * 更新陶坛罐
     *
     * @param batchPotteryMappings 陶坛罐
     */
    protected void updatePotteryMappingState(LaoczBatchPotteryMapping batchPotteryMappings) {
        iLaoczBatchPotteryMappingService.updateById(batchPotteryMappings);
    }

    /**
     * 更新陶坛罐
     *
     * @param potteryAltarId 陶坛罐id
     * @param operator       运算符 +  或 -
     * @param actualWeight   实际重量
     */
    protected LaoczBatchPotteryMapping updatePotteryMappingState(Long potteryAltarId, String operator, Double actualWeight) {
        LaoczBatchPotteryMapping laoczBatchPotteryMapping = iLaoczBatchPotteryMappingService.lambdaQuery().eq(LaoczBatchPotteryMapping::getPotteryAltarId, potteryAltarId).one();
        Double weight = laoczBatchPotteryMapping.getActualWeight();
        if (weight == null) {
            weight = 0.0;
        }
        //判断加减，如果加 更新重量和状态
        //如果减那么判断是否减到0，如果没有到0，更新减法后的重量并更新状态，如果到0，那么就删除此条数据
        if ("+".equals(operator)) {
            laoczBatchPotteryMapping.setActualWeight(weight + actualWeight);
            laoczBatchPotteryMapping.setStoringTime(new Date());
        } else if ("-".equals(operator)) {
            double v = weight - actualWeight;
            if (v <= 0) {
                v = 0.0;
            }
            laoczBatchPotteryMapping.setActualWeight(v);
        }
        laoczBatchPotteryMapping.setRealStatus(RealStatusEnum.STORAGE.getCode());
        iLaoczBatchPotteryMappingService.updateById(laoczBatchPotteryMapping);
        return laoczBatchPotteryMapping;
    }

    /**
     * 批量检查
     *
     * @param potteryAltars 陶坛罐列表
     */
    protected void checkLaoczPotteryAltarManagement(List<WineOutApplyDTO> potteryAltars) {
        //先验证选择的陶坛是否存在
        //验证陶坛是否可以使用，有没有被封存
        //验证陶坛是否已经被申请
        //验证陶坛的重量上限是否满足
        List<Long> potteryAltarIds = potteryAltars.stream().map(WineOutApplyDTO::getPotteryAltarId).collect(Collectors.toList());


        //检查陶坛是否都可以使用
        List<LaoczPotteryAltarManagement> laoczPotteryAltarManagements = iLaoczPotteryAltarManagementService.lambdaQuery().in(LaoczPotteryAltarManagement::getPotteryAltarId, potteryAltarIds).list();
        if (potteryAltars.size() != laoczPotteryAltarManagements.size()) {
            throw new CustomException("陶坛罐不存在，请刷新重新选择");
        }
        for (LaoczPotteryAltarManagement laoczPotteryAltarManagement : laoczPotteryAltarManagements) {
            if (Objects.equals(laoczPotteryAltarManagement.getPotteryAltarState(), PotteryAltarStateEnum.SEAL.getCode())) {
                throw new CustomException("陶坛罐编号:" + laoczPotteryAltarManagement.getPotteryAltarNumber() + "已经封存");
            }
        }
        //检查入酒陶坛罐是否已经申请
        List<LaoczBatchPotteryMapping> list = iLaoczBatchPotteryMappingService.lambdaQuery().in(LaoczBatchPotteryMapping::getPotteryAltarId, potteryAltarIds).list();
        if (!list.isEmpty()) {
            throw new CustomException("陶坛罐已经申请,请刷新重新选择");
        }
        Map<Long, WineOutApplyDTO> map = potteryAltars.stream().collect(Collectors.toMap(WineOutApplyDTO::getPotteryAltarId, Function.identity()));
        //检查陶坛的重量上限是否满足
        for (LaoczPotteryAltarManagement laoczPotteryAltarManagement : laoczPotteryAltarManagements) {
            WineOutApplyDTO wineOutApplyDTO = map.get(laoczPotteryAltarManagement.getPotteryAltarId());
            if (wineOutApplyDTO.getApplyWeight() > laoczPotteryAltarManagement.getPotteryAltarFullAltarWeight()) {
                throw new CustomException("陶坛罐编号:" + laoczPotteryAltarManagement.getPotteryAltarNumber() + "容量不足");
            }
        }
    }

    /**
     * 陶坛罐编号检查
     *
     * @param potteryAltarNumber 陶坛罐编号
     * @param isUse              是否使用（有没有酒） true 使用 false 不使用
     */
    protected void potteryAltarNumberCheck(String potteryAltarNumber, boolean isUse) {
        LaoczPotteryAltarManagement laoczPotteryAltarManagement = iLaoczPotteryAltarManagementService.lambdaQuery()
                .eq(LaoczPotteryAltarManagement::getPotteryAltarNumber, potteryAltarNumber).one();
        if (laoczPotteryAltarManagement == null) {
            throw new CustomException("陶坛罐不存在");
        }

        AltarStateCheck(laoczPotteryAltarManagement);

        LaoczBatchPotteryMapping batchPotteryMapping = iLaoczBatchPotteryMappingService.lambdaQuery()
                .eq(LaoczBatchPotteryMapping::getPotteryAltarId, laoczPotteryAltarManagement.getPotteryAltarId()).one();

        RealStatusEnum realStatus = batchPotteryMapping != null ? RealStatusEnum.getEnumByCode(batchPotteryMapping.getRealStatus()) : null;
        if (isUse) {
            if (batchPotteryMapping == null) {
                throw new CustomException("陶坛罐未存储酒液");
            }
        } else {
            if (batchPotteryMapping != null) {
                if (Objects.equals(RealStatusEnum.STORAGE.getCode(), realStatus.getCode())) {
                    throw new CustomException("陶坛罐已存储酒液");
                }
            }
        }
        if (realStatus != null) {
            checkRealStatus(realStatus);
        }
    }

    /**
     * 检查陶坛罐状态
     *
     * @param realStatus 陶坛罐状态
     */
    protected void checkRealStatus(RealStatusEnum realStatus) {
        if (realStatus == null) {
            throw new CustomException("陶坛罐状态异常,请联系管理员");
        }
        switch (realStatus) {
            case OCCUPY:
                throw new CustomException("陶坛罐已被申请");
            case WINE_IN:
                throw new CustomException("陶坛罐入酒中");
            case WINE_OUT:
                throw new CustomException("陶坛出酒中");
            case POUR:
                throw new CustomException("陶坛罐倒坛中");
        }
    }


    protected void checkDetailType(Long detailType, WineDetailTypeEnum wineDetailTypeEnum) {
        if (!wineDetailTypeEnum.getCode().equals(detailType)) {
            throw new CustomException("酒操作业务详情不是" + wineDetailTypeEnum.getDesc());
        }
    }

    protected LaoczWineDetails getWineDetailsById(Long wineDetailsId) {
        return iLaoczWineDetailsService.getById(wineDetailsId);
    }
}
