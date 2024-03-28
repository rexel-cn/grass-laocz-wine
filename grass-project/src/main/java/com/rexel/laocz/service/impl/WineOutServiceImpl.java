package com.rexel.laocz.service.impl;

import cn.hutool.core.util.StrUtil;
import com.rexel.common.exception.CustomException;
import com.rexel.common.utils.SequenceUtils;
import com.rexel.laocz.constant.WineConstants;
import com.rexel.laocz.constant.WinePointConstants;
import com.rexel.laocz.domain.LaoczBatchPotteryMapping;
import com.rexel.laocz.domain.LaoczWineDetails;
import com.rexel.laocz.domain.dto.WineOutApplyDTO;
import com.rexel.laocz.domain.vo.WineDetailPointVO;
import com.rexel.laocz.dview.DviewUtils;
import com.rexel.laocz.enums.OperationTypeEnum;
import com.rexel.laocz.enums.RealStatusEnum;
import com.rexel.laocz.enums.WineBusyStatusEnum;
import com.rexel.laocz.enums.WineOperationTypeEnum;
import com.rexel.laocz.mapper.LaoczWineDetailsMapper;
import com.rexel.laocz.mapper.LaoczWineHistoryMapper;
import com.rexel.laocz.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
        saveLaoczWineOperations(busyId, workId, OperationTypeEnum.WINE_OUT);
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
        String eventStatus = WineConstants.SUCCESS;
        try {
            String pointValue = DviewUtils.queryPointValue(zlOut.getPointId(), zlOut.getPointType());
            wineDetails.setWeighingTankWeight(Double.parseDouble(pointValue));
            wineDetails.setBusyStatus(WineBusyStatusEnum.COMPLETED.getValue());
            iLaoczWineDetailsService.updateById(wineDetails);
            return pointValue;
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
        //新增数据到历史表
        super.saveHistory(wineDetailsId, OperationTypeEnum.WINE_OUT);
        //备份酒操作业务表
        super.backupWineDetails(laoczWineDetails);
        //更新陶坛实时关系表，入酒，更新为存储，更新实际重量（为称重罐的实际重量）
        super.updatePotteryMappingState(laoczWineDetails.getPotteryAltarId(), "-",
                laoczWineDetails.getPotteryAltarApplyWeight(), RealStatusEnum.STORAGE);
        //查询当前业务id还有没有正在完成的任务，如果没有了，就备份酒操作业务表
        super.taskVerify(laoczWineDetails.getBusyId());
    }

    private void winOutfinishCheck(LaoczWineDetails laoczWineDetails) {
        if (laoczWineDetails.getWeighingTankWeight() == null) {
            throw new CustomException("称重罐重量未获取");
        }
        if (!Objects.equals(laoczWineDetails.getBusyStatus(), WineBusyStatusEnum.COMPLETED.getValue())) {
            throw new CustomException("该任务未完成");
        }
    }

    private WineDetailPointVO getZlOut(List<WineDetailPointVO> weighingTankPointVOList) {
        for (WineDetailPointVO weighingTankPointVO : weighingTankPointVOList) {
            if (WinePointConstants.ZL_OUT.equals(weighingTankPointVO.getUseMark())) {
                if (StrUtil.isEmpty(weighingTankPointVO.getPointId()) || StrUtil.isEmpty(weighingTankPointVO.getPointType())) {
                    throw new RuntimeException("称重罐测点信息不全,请联系管理员");
                }
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
            list.add(buildLaoczWineDetails(busyId, workId, laoczBatchPotteryMapping.getLiquorBatchId(), potteryAltarId, wineOutApplyDTO.getApplyWeight()));
        }
        iLaoczWineDetailsService.saveBatch(list);
    }


}
