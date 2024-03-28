package com.rexel.laocz.service;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson2.JSON;
import com.rexel.laocz.domain.*;
import com.rexel.laocz.domain.dto.WineHistoryDTO;
import com.rexel.laocz.enums.OperationTypeEnum;
import com.rexel.laocz.enums.RealStatusEnum;
import com.rexel.laocz.enums.WineBusyStatusEnum;
import com.rexel.laocz.enums.WineOperationTypeEnum;
import com.rexel.laocz.mapper.LaoczWineDetailsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @ClassName WineAbstract
 * @Description
 * @Author 孟开通
 * @Date 2024/3/13 17:48
 **/
@Service
public abstract class WineAbstract {

    @Autowired
    @Qualifier("laoczTtlScheduledExecutorService")
    protected ScheduledExecutorService threadPoolTaskScheduler;
    @Autowired
    private ILaoczWineDetailsService iLaoczWineDetailsService;
    @Autowired
    private ILaoczSamplingHistorityService iLaoczSamplingHistorityService;
    @Autowired
    private LaoczWineDetailsMapper laoczWineDetailsMapper;
    @Autowired
    private ILaoczWineOperationsService iLaoczWineOperationsService;
    @Autowired
    private ILaoczWineOperationsHisService iLaoczWineOperationsHisService;
    @Autowired
    private ILaoczWineEventService iLaoczWineEventService;
    @Autowired
    private ILaoczWineDetailsHisService iLaoczWineDetailsHisService;

    @Autowired
    private ILaoczBatchPotteryMappingService iLaoczBatchPotteryMappingService;

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
    protected LaoczWineDetails buildLaoczWineDetails(String busyId, String workId, String liquorBatchId, Long potteryAltarId, Double applyWeight) {
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
        return new LaoczWineDetails();
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


    protected void saveHistory(Long wineDetailsId, OperationTypeEnum operationTypeEnum) {
        List<WineHistoryDTO> wineHistoryDTOS = laoczWineDetailsMapper.selectWineHistoryDTOList(wineDetailsId);

        switch (operationTypeEnum) {
            case WINE_ENTRY:
                //入酒
                break;
            case WINE_OUT:
                //出酒
                break;
            case POUR_POT:
                //倒坛
                break;
            case SAMPLING:
                //取样
                break;
            default:
                throw new IllegalArgumentException("Unexpected value: " + operationTypeEnum);
        }
    }

    protected void saveLaoczWineDetails() {

    }


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
        laoczSamplingHistority.setSamplingPurpose(laoczWineDetails.getSamplingPurpose());
        //取样重量
        laoczSamplingHistority.setSamplingWeight(laoczWineDetails.getPotteryAltarApplyWeight());
        //取样时间
        laoczSamplingHistority.setSamplingDate(laoczWineDetails.getOperationTime());
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
        LaoczWineDetailsHis laoczWineDetailsHis = BeanUtil.copyProperties(laoczWineDetails, LaoczWineDetailsHis.class);
        laoczWineDetailsHis.setOperationTime(new Date());
        iLaoczWineDetailsHisService.save(laoczWineDetailsHis);
        iLaoczWineDetailsService.removeById(laoczWineDetails);
    }

    /**
     * 更新陶坛罐
     *
     * @param potteryAltarId      陶坛罐id
     * @param operator            运算符 +  或 -
     * @param actualWeight        实际重量
     * @param operatingStatusEnum 操作状态
     */
    protected void updatePotteryMappingState(Long potteryAltarId, String operator, Double actualWeight, RealStatusEnum operatingStatusEnum) {
        LaoczBatchPotteryMapping laoczBatchPotteryMapping = iLaoczBatchPotteryMappingService.lambdaQuery().eq(LaoczBatchPotteryMapping::getPotteryAltarId, potteryAltarId).one();
        Double weight = laoczBatchPotteryMapping.getActualWeight();
        //判断加减，如果加 更新重量和状态
        //如果减那么判断是否减到0，如果没有到0，更新减法后的重量并更新状态，如果到0，那么就删除此条数据
        if ("+".equals(operator)) {
            laoczBatchPotteryMapping.setActualWeight(weight + actualWeight);
            laoczBatchPotteryMapping.setRealStatus(operatingStatusEnum.getCode());
            laoczBatchPotteryMapping.setStoringTime(new Date());
            iLaoczBatchPotteryMappingService.updateById(laoczBatchPotteryMapping);
        } else if ("-".equals(operator)) {
            if (weight - actualWeight > 0) {
                laoczBatchPotteryMapping.setActualWeight(weight - actualWeight);
                laoczBatchPotteryMapping.setRealStatus(operatingStatusEnum.getCode());
                iLaoczBatchPotteryMappingService.updateById(laoczBatchPotteryMapping);
            } else {
                iLaoczBatchPotteryMappingService.removeById(laoczBatchPotteryMapping);
            }
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
        iLaoczBatchPotteryMappingService.lambdaUpdate().set(LaoczBatchPotteryMapping::getRealStatus, operatingStatusEnum.getCode()).eq(LaoczBatchPotteryMapping::getPotteryAltarId, potteryAltarId).update();
    }
}
