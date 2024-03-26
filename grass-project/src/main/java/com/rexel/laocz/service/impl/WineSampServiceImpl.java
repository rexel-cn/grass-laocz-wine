package com.rexel.laocz.service.impl;

import com.rexel.common.exception.CustomException;
import com.rexel.common.utils.SequenceUtils;
import com.rexel.laocz.domain.LaoczBatchPotteryMapping;
import com.rexel.laocz.domain.LaoczWineDetails;
import com.rexel.laocz.domain.LaoczWineOperations;
import com.rexel.laocz.domain.dto.WineSampApplyDTO;
import com.rexel.laocz.enums.OperationTypeEnum;
import com.rexel.laocz.enums.WineRealRunStatusEnum;
import com.rexel.laocz.mapper.LaoczWineHistoryMapper;
import com.rexel.laocz.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @ClassName WineSampServiceImpl
 * @Description
 * @Author 孟开通
 * @Date 2024/3/19 18:18
 **/
@Service
public class WineSampServiceImpl extends WineAbstract implements WineSampService {
    @Autowired
    private ILaoczWineOperationsService iLaoczWineOperationsService;
    @Autowired
    private ILaoczWineDetailsService iLaoczWineDetailsService;
    @Autowired
    private ILaoczBatchPotteryMappingService iLaoczBatchPotteryMappingService;
    @Autowired
    private LaoczWineHistoryMapper laoczWineHistoryMapper;


    /**
     * 酒取样申请
     *
     * @param wineSampApplyDTO 酒取样申请
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void wineSampApply(WineSampApplyDTO wineSampApplyDTO) {
        //根据陶坛id查询实时表，查询陶坛批次号
        LaoczBatchPotteryMapping laoczBatchPotteryMapping = iLaoczBatchPotteryMappingService.lambdaQuery().eq(LaoczBatchPotteryMapping::getPotteryAltarId, wineSampApplyDTO.getPotteryAltarId()).one();
        //验证是否存在，以及是否是存储状态
        if (laoczBatchPotteryMapping == null) {
            throw new CustomException("陶坛罐没有酒不允许取样");
        }
        //新增酒操作业务表
        //新增酒操作业务详情
        //新增 工单表（流程审批创建）,然后需要把laocz_liquor_batch的liquor_batch_id字段作为业务id来和流程关联
        String workId = SequenceUtils.nextId().toString();
        //生成busy_id
        String busyId = SequenceUtils.nextId().toString();
        //新增laocz_wine_operations
        saveLaoczWineOperations(busyId, workId);
        //新增laocz_wine_details
        saveLaoczWineDetails(wineSampApplyDTO, busyId, workId, laoczBatchPotteryMapping);
    }

    private void saveLaoczWineDetails(WineSampApplyDTO wineSampApplyDTO, String busyId, String workId, LaoczBatchPotteryMapping laoczBatchPotteryMapping) {
        LaoczWineDetails laoczWineDetails = new LaoczWineDetails();
        //业务id
        laoczWineDetails.setBusyId(busyId);
        //工单id
        laoczWineDetails.setWorkOrderId(workId);
        //酒品批次号
        laoczWineDetails.setLiquorBatchId(laoczBatchPotteryMapping.getLiquorBatchId());
        //陶坛罐id
        laoczWineDetails.setPotteryAltarId(wineSampApplyDTO.getPotteryAltarId());
        //运行状态
        laoczWineDetails.setBusyStatus(WineRealRunStatusEnum.NOT_STARTED.getValue());
        //申请重量
        laoczWineDetails.setPotteryAltarApplyWeight(wineSampApplyDTO.getSamplingWeight());
        iLaoczWineDetailsService.save(laoczWineDetails);
    }

    private void saveLaoczWineOperations(String busyId, String workId) {
        LaoczWineOperations operations = new LaoczWineOperations();
        //业务id
        operations.setBusyId(busyId);
        //工单id
        operations.setWorkOrderId(workId);
        //操作类型 入酒
        operations.setOperationType(OperationTypeEnum.SAMPLING.getValue());
        iLaoczWineOperationsService.save(operations);
    }

    /**
     * 酒取样完成
     *
     * @param wineDetailsId 酒操作业务详情id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void wineSampFinish(Long wineDetailsId) {
        //新增取样记录表
        LaoczWineDetails laoczWineDetails = iLaoczWineDetailsService.getById(wineDetailsId);
        laoczWineDetails.setBusyStatus(WineRealRunStatusEnum.COMPLETED.getValue());
        laoczWineDetails.setOperationTime(new Date());
        //更新入酒时间和状态
        iLaoczWineDetailsService.updateById(laoczWineDetails);
        //新增数据到历史表
        super.saveHistory(wineDetailsId, OperationTypeEnum.SAMPLING);
    }


}
