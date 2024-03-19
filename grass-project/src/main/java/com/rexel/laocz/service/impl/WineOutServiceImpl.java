package com.rexel.laocz.service.impl;

import com.alibaba.fastjson2.JSON;
import com.rexel.common.exception.CustomException;
import com.rexel.common.utils.SequenceUtils;
import com.rexel.laocz.constant.WinePointConstants;
import com.rexel.laocz.domain.LaoczBatchPotteryMapping;
import com.rexel.laocz.domain.LaoczWineDetails;
import com.rexel.laocz.domain.LaoczWineEvent;
import com.rexel.laocz.domain.LaoczWineOperations;
import com.rexel.laocz.domain.dto.WineOutApplyDTO;
import com.rexel.laocz.domain.vo.WineDetailPointVO;
import com.rexel.laocz.dview.DviewUtils;
import com.rexel.laocz.enums.OperationTypeEnum;
import com.rexel.laocz.enums.WineRealRunStatusEnum;
import com.rexel.laocz.mapper.LaoczWineDetailsMapper;
import com.rexel.laocz.mapper.LaoczWineHistoryMapper;
import com.rexel.laocz.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
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
public class WineOutServiceImpl implements WineOutService {
    @Autowired
    private ILaoczWineOperationsService iLaoczWineOperationsService;
    @Autowired
    private ILaoczWineDetailsService iLaoczWineDetailsService;
    @Autowired
    private LaoczWineDetailsMapper laoczWineDetailsMapper;
    @Autowired
    private LaoczWineHistoryMapper laoczWineHistoryMapper;

    @Autowired
    private ILaoczBatchPotteryMappingService iLaoczBatchPotteryMappingService;
    @Autowired
    private ILaoczWineEventService iLaoczWineEventService;

    /**
     * 出酒申请
     *
     * @param list 出酒申请参数：陶坛罐ID，申请重量
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void wineOutApply(List<WineOutApplyDTO> list) {
        //检查实时表，检查申请的是否都在实时表里面存在，检查申请的重量是否小于等于实时表里面的重量
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
        }


        //新增 工单表（流程审批创建）,然后需要把laocz_liquor_batch的liquor_batch_id字段作为业务id来和流程关联
        String workId = SequenceUtils.nextId().toString();
        //生成busy_id
        String busyId = SequenceUtils.nextId().toString();
        //新增laocz_wine_operations
        saveLaoczWineOperations(busyId, workId);
        //新增laocz_wine_details
        saveLaoczWineDetails(list, busyId, workId, batchPotteryMappings);
    }

    /**
     * 出酒操作，称重罐称重量
     *
     * @param wineDetailsId 酒操作业务详情id
     * @return 称重量
     */
    @Override
    public String wineOutStart(Long wineDetailsId) {
        LaoczWineDetails wineDetails = iLaoczWineDetailsService.getById(wineDetailsId);

        //查询称重罐测点
        List<WineDetailPointVO> weighingTankPointVOList = laoczWineDetailsMapper.selectWineDetailWeighingTankPointVOList(wineDetailsId);
        WineDetailPointVO zlOut = getZlOut(weighingTankPointVOList);
        try {
            String value = DviewUtils.queryPointValue(zlOut.getPointId(), zlOut.getPointType());
            iLaoczWineDetailsService.lambdaUpdate()
                    .eq(LaoczWineDetails::getWineDetailsId, wineDetailsId)
                    .set(LaoczWineDetails::getWeighingTankWeight, Double.parseDouble(value))
                    .set(LaoczWineDetails::getBusyStatus, WineRealRunStatusEnum.COMPLETED.getValue())
                    .update();
            saveWineEvent(wineDetails, weighingTankPointVOList);
            return value;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveWineEvent(LaoczWineDetails wineDetails, List<WineDetailPointVO> weighingTankPointVOList) {
        List<String> weightPoints = weighingTankPointVOList.stream().map(WineDetailPointVO::getPointId).collect(Collectors.toList());
        LaoczWineEvent laoczWineEvent = new LaoczWineEvent();
        laoczWineEvent.setWorkOrderId(wineDetails.getWorkOrderId());
        laoczWineEvent.setBusyId(wineDetails.getBusyId());
        laoczWineEvent.setLiquorBatchId(wineDetails.getLiquorBatchId());
        laoczWineEvent.setPotteryAltarId(wineDetails.getPotteryAltarId());
        laoczWineEvent.setEventId("");
        laoczWineEvent.setEventTime(new Date());
        laoczWineEvent.setEventStatus("");
        laoczWineEvent.setEventParam(JSON.toJSONString(wineDetails.getWineDetailsId()));
        //转换为，号分割
        laoczWineEvent.setPointArray(String.join(",", weightPoints));

        iLaoczWineEventService.save(laoczWineEvent);

    }

    /**
     * 出酒操作完成
     *
     * @param wineDetailsId 酒操作业务详情id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void wineOutFinish(Long wineDetailsId) {
        LaoczWineDetails laoczWineDetails = iLaoczWineDetailsService.getById(wineDetailsId);
        laoczWineDetails.setBusyStatus(WineRealRunStatusEnum.COMPLETED.getValue());
        laoczWineDetails.setOperationTime(new Date());
        //更新入酒时间和状态
        iLaoczWineDetailsService.updateById(laoczWineDetails);
        //新增数据到历史表
        laoczWineHistoryMapper.saveHistory(wineDetailsId);

        //更新实时表，如果酒出完就删除数据


        LaoczBatchPotteryMapping laoczBatchPotteryMapping = iLaoczBatchPotteryMappingService.lambdaQuery()
                .eq(LaoczBatchPotteryMapping::getLiquorBatchId, laoczWineDetails.getLiquorBatchId())
                .eq(LaoczBatchPotteryMapping::getPotteryAltarId, laoczWineDetails.getPotteryAltarId())
                .one();
        //陶坛罐重量
        Double weighingTankWeight = laoczWineDetails.getWeighingTankWeight();
        //实际储存的重量
        Double actualWeight = laoczBatchPotteryMapping.getActualWeight();
        if (weighingTankWeight >= actualWeight) {
            iLaoczBatchPotteryMappingService.removeById(laoczBatchPotteryMapping);
        } else {
            laoczBatchPotteryMapping.setActualWeight(actualWeight - weighingTankWeight);
            iLaoczBatchPotteryMappingService.updateById(laoczBatchPotteryMapping);
        }

    }

    private WineDetailPointVO getZlOut(List<WineDetailPointVO> weighingTankPointVOList) {
        for (WineDetailPointVO weighingTankPointVO : weighingTankPointVOList) {
            if (WinePointConstants.ZL_OUT.equals(weighingTankPointVO.getUseMark())) {
                return weighingTankPointVO;
            }
        }
        throw new RuntimeException("未找到称重罐测点");
    }

    private void saveLaoczWineDetails(List<WineOutApplyDTO> wineOutApplyDTOS, String busyId, String workId, List<LaoczBatchPotteryMapping> batchPotteryMappings) {
        Map<Long, LaoczBatchPotteryMapping> map = batchPotteryMappings.stream().collect(Collectors.toMap(LaoczBatchPotteryMapping::getPotteryAltarId, Function.identity()));

        List<LaoczWineDetails> list = new ArrayList<>();
        for (WineOutApplyDTO wineOutApplyDTO : wineOutApplyDTOS) {
            Long potteryAltarId = wineOutApplyDTO.getPotteryAltarId();
            LaoczBatchPotteryMapping laoczBatchPotteryMapping = map.get(potteryAltarId);

            LaoczWineDetails laoczWineDetails = new LaoczWineDetails();
            //业务id
            laoczWineDetails.setBusyId(busyId);
            //工单id
            laoczWineDetails.setWorkOrderId(workId);
            //酒品批次号
            laoczWineDetails.setLiquorBatchId(laoczBatchPotteryMapping.getLiquorBatchId());
            //陶坛罐id
            laoczWineDetails.setPotteryAltarId(wineOutApplyDTO.getPotteryAltarId());
            //运行状态
            laoczWineDetails.setBusyStatus(WineRealRunStatusEnum.NOT_STARTED.getValue());
            //申请重量
            laoczWineDetails.setPotteryAltarApplyWeight(wineOutApplyDTO.getApplyWeight());
            list.add(laoczWineDetails);
        }
        iLaoczWineDetailsService.saveBatch(list);
    }

    private void saveLaoczWineOperations(String busyId, String workId) {
        LaoczWineOperations operations = new LaoczWineOperations();
        //业务id
        operations.setBusyId(busyId);
        //工单id
        operations.setWorkOrderId(workId);
        //操作类型 入酒
        operations.setOperationType(OperationTypeEnum.WINE_ENTRY.getValue());
        iLaoczWineOperationsService.save(operations);
    }


}
