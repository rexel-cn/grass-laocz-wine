package com.rexel.laocz.service.impl;

import com.alibaba.fastjson2.JSON;
import com.rexel.common.exception.CustomException;
import com.rexel.common.utils.SequenceUtils;
import com.rexel.dview.pojo.DViewVarInfo;
import com.rexel.laocz.constant.WineConstants;
import com.rexel.laocz.constant.WinePointConstants;
import com.rexel.laocz.domain.*;
import com.rexel.laocz.domain.dto.WineEntryApplyDTO;
import com.rexel.laocz.domain.dto.WineEntryDTO;
import com.rexel.laocz.domain.vo.WineDetailPointVO;
import com.rexel.laocz.domain.vo.WineRealDataVO;
import com.rexel.laocz.dview.DviewUtils;
import com.rexel.laocz.dview.DviewUtilsPro;
import com.rexel.laocz.dview.domain.DviewPointDTO;
import com.rexel.laocz.enums.*;
import com.rexel.laocz.mapper.LaoczWineDetailsMapper;
import com.rexel.laocz.mapper.LaoczWineHistoryMapper;
import com.rexel.laocz.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @ClassName WineEntryApplyServiceImpl
 * @Description 入酒
 * @Author 孟开通
 * @Date 2024/3/11 11:05
 **/
@Service
@Slf4j
public class WineEntryApplyServiceImpl extends WineAbstract implements WineEntryApplyService {
    private static final AtomicReference<String> atomicReference = new AtomicReference<>();
    private static final AtomicInteger atomicInteger = new AtomicInteger(0);
    private final ReentrantLock lock = new ReentrantLock(true);
    private final Map<Long, ScheduledFuture<?>> threadMap = new ConcurrentHashMap<>();
    @Autowired
    private ILaoczLiquorBatchService iLaoczLiquorBatchService;
    @Autowired
    private ILaoczPotteryAltarManagementService iLaoczPotteryAltarManagementService;
    @Autowired
    private ILaoczBatchPotteryMappingService iLaoczBatchPotteryMappingService;
    @Autowired
    private ILaoczWineOperationsService iLaoczWineOperationsService;
    @Autowired
    private ILaoczWineDetailsService iLaoczWineDetailsService;
    @Autowired
    private LaoczWineDetailsMapper laoczWineDetailsMapper;
    @Autowired
    private ILaoczWeighingTankService iLaoczWeighingTankService;
    @Autowired
    @Qualifier("laoczTtlScheduledExecutorService")
    private ScheduledExecutorService threadPoolTaskScheduler;
    @Autowired
    private LaoczWineHistoryMapper laoczWineHistoryMapper;

    @Autowired
    private ILaoczWineEventService iLaoczWineEventService;


    /**
     * 生成酒品批次号
     *
     * @return 酒品批次号
     */
    @Override
    public synchronized String getLiquorBatchId() {
        // 获取当前的日期和时间
        LocalDateTime now = LocalDateTime.now();
        // 定义一个没有分隔符的日期时间格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String format = now.format(formatter);
        String time = atomicReference.get();
        if (format.equals(time)) {
            int i = atomicInteger.addAndGet(1);
            format += "-" + i;
        } else {
            atomicInteger.set(0);
            atomicReference.set(format);
        }
        return format;
    }

    /**
     * 入酒申请
     *
     * @param wineEntryApplyDTO 入酒申请参数：申请重量，陶坛罐ID，酒品管理ID，酒品批次号
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void wineEntryApply(WineEntryApplyDTO wineEntryApplyDTO) {
        List<Long> collect = wineEntryApplyDTO.getPotteryAltars().stream().map(WineEntryApplyDTO.PotteryAltar::getPotteryAltarId).collect(Collectors.toList());
        //检查陶坛是否都可以使用
        List<LaoczPotteryAltarManagement> laoczPotteryAltarManagements = iLaoczPotteryAltarManagementService.lambdaQuery().in(LaoczPotteryAltarManagement::getPotteryAltarId, collect).list();
        if (collect.size() != laoczPotteryAltarManagements.size()) {
            throw new CustomException("陶坛罐不存在，请刷新重新选择");
        }
        for (LaoczPotteryAltarManagement laoczPotteryAltarManagement : laoczPotteryAltarManagements) {
            if (Objects.equals(laoczPotteryAltarManagement.getPotteryAltarState(), PotteryAltarStateEnum.SEAL.getCode())) {
                throw new CustomException("陶坛罐编号:" + laoczPotteryAltarManagement.getPotteryAltarNumber() + "已经封存");
            }
        }
        //检查入酒陶坛罐是否已经申请
        List<LaoczBatchPotteryMapping> list = iLaoczBatchPotteryMappingService.lambdaQuery().in(LaoczBatchPotteryMapping::getPotteryAltarId, collect).list();
        if (!list.isEmpty()) {
            throw new CustomException("陶坛罐已经申请,请刷新重新选择");
        }
        //检查申请重量是否等于陶坛罐申请重量总和
        double sum = wineEntryApplyDTO.getPotteryAltars().stream().mapToDouble(WineEntryApplyDTO.PotteryAltar::getApplyWeight).sum();
        if (sum != wineEntryApplyDTO.getApplyWeight()) {
            throw new CustomException("申请重量与陶坛罐申请重量不符");
        }


        //新增    laocz_liquor_batch
        saveLiquorBatch(wineEntryApplyDTO);
        //新增 工单表（流程审批创建）,然后需要把laocz_liquor_batch的liquor_batch_id字段作为业务id来和流程关联
        String workId = SequenceUtils.nextId().toString();
        //将创建工单生成的id与laocz_liquor_batch进行关联更新
        //后续的表也新增
        //新增 laocz_batch_pottery_mapping
        savalaoczBatchPotteryMapping(wineEntryApplyDTO);
        //生成busy_id
        String busyId = SequenceUtils.nextId().toString();
        //新增laocz_wine_operations
        saveLaoczWineOperations(busyId, workId);
        //新增laocz_wine_details
        saveLaoczWineDetails(wineEntryApplyDTO, busyId, workId);
    }

    /**
     * 新增 酒操作详情表
     *
     * @param wineEntryApplyDTO 入酒申请参数
     * @param busyId            业务id
     * @param workId            工单id
     */
    private void saveLaoczWineDetails(WineEntryApplyDTO wineEntryApplyDTO, String busyId, String workId) {
        List<LaoczWineDetails> list = new ArrayList<>();
        for (WineEntryApplyDTO.PotteryAltar potteryAltarId : wineEntryApplyDTO.getPotteryAltars()) {
            LaoczWineDetails laoczWineDetails = new LaoczWineDetails();
            //业务id
            laoczWineDetails.setBusyId(busyId);
            //工单id
            laoczWineDetails.setWorkOrderId(workId);
            //酒品批次号
            laoczWineDetails.setLiquorBatchId(wineEntryApplyDTO.getLiquorBatchId());
            //陶坛罐id
            laoczWineDetails.setPotteryAltarId(potteryAltarId.getPotteryAltarId());
            //运行状态
            laoczWineDetails.setBusyStatus(WineRealRunStatusEnum.NOT_STARTED.getValue());
            //申请重量
            laoczWineDetails.setPotteryAltarApplyWeight(potteryAltarId.getApplyWeight());
            list.add(laoczWineDetails);
        }
        iLaoczWineDetailsService.saveBatch(list);
    }

    /**
     * 新增 酒操作表
     *
     * @param busyId 业务id
     * @param workId 工单id
     */
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

    /**
     * 新增批次陶坛关联表
     *
     * @param wineEntryApplyDTO 入酒申请参数
     */
    private void savalaoczBatchPotteryMapping(WineEntryApplyDTO wineEntryApplyDTO) {
        List<LaoczBatchPotteryMapping> laoczBatchPotteryMappings = new ArrayList<>();
        for (WineEntryApplyDTO.PotteryAltar potteryAltarId : wineEntryApplyDTO.getPotteryAltars()) {
            LaoczBatchPotteryMapping laoczBatchPotteryMapping = new LaoczBatchPotteryMapping();
            //批次id
            laoczBatchPotteryMapping.setLiquorBatchId(wineEntryApplyDTO.getLiquorBatchId());
            //陶坛罐id
            laoczBatchPotteryMapping.setPotteryAltarId(potteryAltarId.getPotteryAltarId());
            //运行状态
            laoczBatchPotteryMapping.setOperatingState(OperatingStatusEnum.OCCUPY.getCode());
            laoczBatchPotteryMappings.add(laoczBatchPotteryMapping);
        }
        iLaoczBatchPotteryMappingService.saveBatch(laoczBatchPotteryMappings);

    }

    /**
     * 新增酒批次
     *
     * @param wineEntryApplyDTO 入酒申请参数
     */
    private void saveLiquorBatch(WineEntryApplyDTO wineEntryApplyDTO) {
        LaoczLiquorBatch liquorBatch = new LaoczLiquorBatch();
        //酒品批次号
        liquorBatch.setLiquorBatchId(wineEntryApplyDTO.getLiquorBatchId());
        //申请重量
        liquorBatch.setApplyWeight(wineEntryApplyDTO.getApplyWeight());
        //酒品管理id
        liquorBatch.setLiquorManagementId(wineEntryApplyDTO.getLiquorManagementId());
        iLaoczLiquorBatchService.save(liquorBatch);
    }

    /**
     * 入酒开始
     *
     * @param wineEntryDTO 入酒开始参数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void wineEntry(WineEntryDTO wineEntryDTO) {
        //查询酒操作详情
        LaoczWineDetails wineDetails;
        lock.lock();
        try {
            //查询酒操作详情
            wineDetails = iLaoczWineDetailsService.getById(wineEntryDTO.getWineDetailsId());
            busyStatusJudge(wineDetails, wineEntryDTO.getStatus());
        } finally {
            lock.unlock();
        }
        //查询称重罐测点
        List<WineDetailPointVO> weighingTankPointVOList = laoczWineDetailsMapper.selectWineDetailWeighingTankPointVOList(wineDetails.getWineDetailsId());
        //查询泵测点
        List<WineDetailPointVO> pumpPointVOList = laoczWineDetailsMapper.selectWineDetailPumpPointVOList(wineDetails.getWineDetailsId());
        //查询称重罐，需要的业务字段：称重罐重量上限，称重罐编号等。
        LaoczWeighingTank weighingTank = iLaoczWeighingTankService.getById(wineDetails.getWeighingTank());

        //更新实时表状态 入酒开始
        updatePotteryMappingState(wineDetails.getPotteryAltarId(), OperatingStatusEnum.WINE_IN);

        String eventStatus = WineConstants.SUCCESS;
        try {
            switch (wineEntryDTO.getStatus()) {
                case "1":
                    //开始
                    start(wineDetails, weighingTankPointVOList, pumpPointVOList, weighingTank);
                    break;
                case "2":
                    //急停
                    pause(wineDetails, pumpPointVOList);
                    break;
                case "3":
                    //继续
                    winContinue(wineDetails, pumpPointVOList, weighingTankPointVOList);
                    break;
                default:
                    throw new CustomException("入酒状态错误");
            }
        } catch (Exception e) {
            log.error("入酒操作异常:{}", e.getMessage());
            eventStatus = WineConstants.FAIL;
            throw new CustomException("入酒操作异常：{}", e.getMessage());
        } finally {
            String finalEventStatus = eventStatus;
            threadPoolTaskScheduler.execute(() -> {
                //保存入酒事件
                saveWineEvent(wineDetails, wineEntryDTO, weighingTankPointVOList, pumpPointVOList, finalEventStatus);
            });
        }
    }

    /**
     * 入酒状态判断
     *
     * @param wineDetails 酒操作详情
     * @param status      入酒状态
     */
    private void busyStatusJudge(LaoczWineDetails wineDetails, String status) {
        switch (status) {
            case "1":
                if (Objects.equals(wineDetails.getBusyStatus(), WineRealRunStatusEnum.STARTED.getValue())) {
                    throw new CustomException("该任务正在执行中");
                }
                if (Objects.equals(wineDetails.getBusyStatus(), WineRealRunStatusEnum.EMERGENCY_STOP.getValue())) {
                    throw new CustomException("该任务已经急停,通过继续按钮启动");
                }
                if (Objects.equals(wineDetails.getBusyStatus(), WineRealRunStatusEnum.COMPLETED.getValue())) {
                    throw new CustomException("该任务已经完成");
                }
                break;
            case "2":
                if (Objects.equals(wineDetails.getBusyStatus(), WineRealRunStatusEnum.NOT_STARTED.getValue())) {
                    throw new CustomException("该任务未开始");
                }
                if (Objects.equals(wineDetails.getBusyStatus(), WineRealRunStatusEnum.EMERGENCY_STOP.getValue())) {
                    throw new CustomException("该任务已急停");
                }
                if (Objects.equals(wineDetails.getBusyStatus(), WineRealRunStatusEnum.COMPLETED.getValue())) {
                    throw new CustomException("该任务已经完成");
                }
                break;
            case "3":
                if (Objects.equals(wineDetails.getBusyStatus(), WineRealRunStatusEnum.NOT_STARTED.getValue())) {
                    throw new CustomException("该任务未开始");
                }
                if (Objects.equals(wineDetails.getBusyStatus(), WineRealRunStatusEnum.COMPLETED.getValue())) {
                    throw new CustomException("该任务已经完成");
                }
                if (Objects.equals(wineDetails.getBusyStatus(), WineRealRunStatusEnum.STARTED.getValue())) {
                    throw new CustomException("该任务正在执行中");
                }
                break;
            default:
                throw new CustomException("入酒状态错误");
        }
    }

    /**
     * 更新酒操作详情状态
     *
     * @param wineDetailsId         酒操作详情id
     * @param wineRealRunStatusEnum 酒操作状态
     */
    private void updateWineDetails(Long wineDetailsId, WineRealRunStatusEnum wineRealRunStatusEnum) {
        iLaoczWineDetailsService.lambdaUpdate()
                .eq(LaoczWineDetails::getWineDetailsId, wineDetailsId)
                .set(LaoczWineDetails::getBusyStatus, wineRealRunStatusEnum.getValue())
                .update();
    }

    /**
     * 更新陶坛罐状态
     *
     * @param potteryAltarId      陶坛罐id
     * @param operatingStatusEnum 陶坛罐状态
     */
    private void updatePotteryMappingState(Long potteryAltarId, OperatingStatusEnum operatingStatusEnum) {
        iLaoczBatchPotteryMappingService.lambdaUpdate()
                .eq(LaoczBatchPotteryMapping::getPotteryAltarId, potteryAltarId)
                .set(LaoczBatchPotteryMapping::getOperatingState, operatingStatusEnum.getCode())
                .update();
    }

    /**
     * 保存酒事件
     * @param wineDetails 酒操作详情
     * @param wineEntryDTO 入酒参数
     * @param weighingTankPointVOList 称重罐测点
     * @param pumpPointVOList 泵测点
     * @param eventStatus 事件状态
     */
    private void saveWineEvent(LaoczWineDetails wineDetails, WineEntryDTO wineEntryDTO, List<WineDetailPointVO> weighingTankPointVOList, List<WineDetailPointVO> pumpPointVOList, String eventStatus) {
        List<String> weightPoints = weighingTankPointVOList.stream().map(WineDetailPointVO::getPointId).collect(Collectors.toList());
        List<String> pumpPoints = pumpPointVOList.stream().map(WineDetailPointVO::getPointId).collect(Collectors.toList());

        weightPoints.addAll(pumpPoints);

        LaoczWineEvent laoczWineEvent = new LaoczWineEvent();
        laoczWineEvent.setWorkOrderId(wineDetails.getWorkOrderId());
        laoczWineEvent.setBusyId(wineDetails.getBusyId());
        laoczWineEvent.setLiquorBatchId(wineDetails.getLiquorBatchId());
        laoczWineEvent.setPotteryAltarId(wineDetails.getPotteryAltarId());
        laoczWineEvent.setEventId(WineOperationTypeEnum.getNameByValue(wineEntryDTO.getStatus()));
        laoczWineEvent.setEventTime(new Date());
        laoczWineEvent.setEventStatus(eventStatus);
        laoczWineEvent.setEventParam(JSON.toJSONString(wineEntryDTO));
        //转换为，号分割
        laoczWineEvent.setPointArray(String.join(",", weightPoints));

        iLaoczWineEventService.save(laoczWineEvent);

    }

    /**
     * 入酒继续.并继续监听
     *
     * @param wineDetails     酒操作详情
     * @param pumpPointVOList 泵测点
     * @param weighingTank    称重罐
     * @throws IOException 异常
     */
    private void winContinue(LaoczWineDetails wineDetails, List<WineDetailPointVO> pumpPointVOList, List<WineDetailPointVO> weighingTank) throws IOException {
        //更新酒操作详情状态，开始
        updateWineDetails(wineDetails.getWineDetailsId(), WineRealRunStatusEnum.STARTED);
        //下发继续测点
        continuePlc(pumpPointVOList, weighingTank);
        //继续监听
        startListener(wineDetails.getWineDetailsId(), weighingTank, pumpPointVOList);
    }

    /**
     * 下发急停，并取消监听
     *
     * @param wineDetails     酒操作详情
     * @param pumpPointVOList 泵测点
     */
    private void pause(LaoczWineDetails wineDetails, List<WineDetailPointVO> pumpPointVOList) {
        //更新酒操作详情状态，开始
        updateWineDetails(wineDetails.getWineDetailsId(), WineRealRunStatusEnum.EMERGENCY_STOP);
        //下发急停测点
        pausePlc(pumpPointVOList);
        //取消监听
        pauseListener(wineDetails.getWineDetailsId());
    }

    /**
     * 下发启动并开始监听
     *
     * @param wineDetails             酒操作详情
     * @param weighingTankPointVOList 称重罐测点
     * @param pumpPointVOList         泵测点
     * @param weighingTank            称重罐
     * @throws IOException
     * @throws InterruptedException
     */
    private void start(LaoczWineDetails wineDetails, List<WineDetailPointVO> weighingTankPointVOList, List<WineDetailPointVO> pumpPointVOList, LaoczWeighingTank weighingTank) throws IOException, InterruptedException {
        //更新酒操作详情状态，开始
        updateWineDetails(wineDetails.getWineDetailsId(), WineRealRunStatusEnum.STARTED);
        //下发测点
        startPlc(wineDetails, weighingTankPointVOList, pumpPointVOList, weighingTank);
        //开始监听
        startListener(wineDetails.getWineDetailsId(), weighingTankPointVOList, pumpPointVOList);
    }

    /**
     * 查询入酒出酒当前实时数据
     *
     * @param wineDetailsId 酒操作业务详情id
     * @return 入酒出酒实时数据
     */
    @Override
    public WineRealDataVO getWineRealData(Long wineDetailsId) {
        LaoczWineDetails laoczWineDetails = iLaoczWineDetailsService.getById(wineDetailsId);
        WineRealDataVO wineRealDataVO = new WineRealDataVO();
        wineRealDataVO.setPotteryAltarApplyWeight(laoczWineDetails.getPotteryAltarApplyWeight());
        wineRealDataVO.setBusyStatus(laoczWineDetails.getBusyStatus());
        //查询称重罐测点
        List<WineDetailPointVO> weighingTankPointVOList = laoczWineDetailsMapper.selectWineDetailWeighingTankPointVOList(wineDetailsId);
        Map<String, WineDetailPointVO> wineDetailPointVOMap = weighingTankPointVOList.stream().collect(Collectors.toMap(WineDetailPointVO::getUseMark, Function.identity()));
        WineDetailPointVO wineDetailPointVO = wineDetailPointVOMap.get(WinePointConstants.ZL_OUT);
        try {
            String s = DviewUtils.queryCachePointValue(wineDetailPointVO.getPointId(), wineDetailPointVO.getPointType());
            wineRealDataVO.setCurrentWeight(Double.parseDouble(s));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return wineRealDataVO;
    }

    /**
     * 入酒结束
     *
     * @param wineDetailsId 酒操作业务详情id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void wineEntryFinish(Long wineDetailsId) {
        LaoczWineDetails laoczWineDetails = iLaoczWineDetailsService.getById(wineDetailsId);
        laoczWineDetails.setOperationTime(new Date());
        //更新入酒时间和状态
        iLaoczWineDetailsService.updateById(laoczWineDetails);

        //更新实时表状态
        updatePotteryMappingState(laoczWineDetails.getPotteryAltarId(), OperatingStatusEnum.STORAGE);

        //新增数据到历史表
        super.saveHistory(wineDetailsId,OperationTypeEnum.WINE_ENTRY);

    }

    private void pauseListener(Long wineDetailsId) {
        ScheduledFuture<?> scheduledFuture = threadMap.get(wineDetailsId);
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
        }
    }

    private void startListener(Long wineDetailsId, List<WineDetailPointVO> weighingTankPointVOList, List<WineDetailPointVO> pumpPointVOList) {
        ScheduledFuture<?> future = threadMap.get(wineDetailsId);
        if(future!=null){
            return;
        }
        Map<String, WineDetailPointVO> wineDetailPointVOMap = weighingTankPointVOList.stream().collect(Collectors.toMap(WineDetailPointVO::getUseMark, Function.identity()));
        Map<String, WineDetailPointVO> pointVOMap = pumpPointVOList.stream().collect(Collectors.toMap(WineDetailPointVO::getUseMark, Function.identity()));
        //完成测点
        WineDetailPointVO finishPoint = pointVOMap.get(WinePointConstants.FINISH_POINT);
        //称重罐重量测点
        WineDetailPointVO zlOut = wineDetailPointVOMap.get(WinePointConstants.ZL_OUT);
        //监听完成测点,如果完成，查询称重罐重量并保存到数据库中
        final Runnable task = () -> {
            try {
                String s = DviewUtils.queryPointValue(finishPoint.getPointId(), finishPoint.getPointType());
                if (Double.parseDouble(s) == 1) {
                    String s1 = DviewUtils.queryPointValue(zlOut.getPointId(), zlOut.getPointType());
                    iLaoczWineDetailsService.lambdaUpdate()
                            .eq(LaoczWineDetails::getWineDetailsId, wineDetailsId)
                            .set(LaoczWineDetails::getWeighingTankWeight, Double.parseDouble(s1))
                            .update();
                    ScheduledFuture<?> scheduledFuture = threadMap.get(wineDetailsId);
                    if (scheduledFuture != null) {
                        scheduledFuture.cancel(true);
                        lock.lock();
                        try {
                            updateWineDetails(wineDetailsId, WineRealRunStatusEnum.COMPLETED);
                        }finally {
                            lock.unlock();
                        }
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
        ScheduledFuture<?> scheduledFuture = threadPoolTaskScheduler.scheduleAtFixedRate(task, 0, 1, TimeUnit.SECONDS);
        threadMap.put(wineDetailsId, scheduledFuture);
    }

    private void startPlc(LaoczWineDetails wineDetails, List<WineDetailPointVO> weighingTankPointVOList, List<WineDetailPointVO> pumpPointVOList, LaoczWeighingTank weighingTank) throws IOException, InterruptedException {
        /*
         【监控测点】
          DJ_SEX_TTK_B1_ES 陶坛库1号泵急停（1为急停状态）          泵单位
          DJ_SEX_TTK_B1_FAULT 陶坛库1号泵故障（1为故障状态）       泵单位
          DJ_SEX_TTK_B1_RE 陶坛库1号泵远程（1为远程状态）          泵单位
          DJ_SEX_TTK_B1_RUN 陶坛库1号泵正运行（0为停止状态）       泵单位
          FM_SRX_TTK_FM1_CL_FAULT 陶坛库阀门1关故障（1为故障状态） 称重罐单位
          FM_SRX_TTK_FM1_OP_FAULT 陶坛库阀门1开故障（1为故障状态） 称重罐单位
          SEX_TTK_1_1_ZL_OUT 陶坛库1楼1号秤（确保不超过上限）      称重罐单位
          【下发测点】
          以泵为单位下发重量测点（待提供）                          泵单位
          以泵为单位下发称重罐号（待提供）                          称重罐单位
          以泵为单位启动信号（待提供）                              泵单位
         */
        /*
        函数需要的参数：
            下发用：1.称重罐编号
                   2.申请重量
             查询校验用：
             1：申请重量
             2：称重罐重量（PLC查询）
             3：称重罐最大重量
             4：
         */
        Map<String, WineDetailPointVO> pumpMap = pumpPointVOList.stream().collect(Collectors.toMap(WineDetailPointVO::getUseMark, Function.identity()));
        Map<String, WineDetailPointVO> wineDetailPointVOMap = weighingTankPointVOList.stream().collect(Collectors.toMap(WineDetailPointVO::getUseMark, Function.identity()));
        List<DviewPointDTO> pointIdList = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        for (MonitorPointConfig value : MonitorPointConfig.values()) {
            if (value.getType().equals("2")) {
                if (!pumpMap.containsKey(value.getUseMark())) {
                    throw new CustomException("泵测点不存在");
                }
                WineDetailPointVO wineDetailPointVO = pumpMap.get(value.getUseMark());
                pointIdList.add(new DviewPointDTO(wineDetailPointVO.getPointId(), wineDetailPointVO.getPointType(), wineDetailPointVO.getPointName(), null));
                map.put(value.getUseMark(), wineDetailPointVO.getPointId());
            }
            if (value.getType().equals("1")) {
                if (!wineDetailPointVOMap.containsKey(value.getUseMark())) {
                    throw new CustomException("称重罐测点不存在");
                }
                WineDetailPointVO wineDetailPointVO = wineDetailPointVOMap.get(value.getUseMark());
                pointIdList.add(new DviewPointDTO(wineDetailPointVO.getPointId(), wineDetailPointVO.getPointType(), wineDetailPointVO.getPointName(), null));
                map.put(value.getUseMark(), wineDetailPointVO.getPointId());
            }
        }

        List<DViewVarInfo> pumpPointValues = DviewUtils.queryCachePointValue(pointIdList);
        Map<String, DViewVarInfo> pointValues = pumpPointValues.stream().collect(Collectors.toMap(DViewVarInfo::getName, Function.identity()));

        for (MonitorPointConfig value : MonitorPointConfig.values()) {
            DViewVarInfo dViewVarInfo = pointValues.get(map.get(value.getUseMark()));
            //判断这次遍历是 ZL_OUT
            if (value.name().equals(MonitorPointConfig.ZL_OUT.name())) {
                //判断是否上限
                //称重罐上限
                String fullTankUpperLimit = weighingTank.getFullTankUpperLimit();
                //申请重量
                Double potteryAltarApplyWeight = wineDetails.getPotteryAltarApplyWeight();
                //查询已有的重量
                Double value1 = Double.parseDouble(dViewVarInfo.getValue().toString());
                //判断申请重量+已有重量>称重罐上限就报错
                if (!value.getCheck().checkValue(String.valueOf(potteryAltarApplyWeight + value1), fullTankUpperLimit)) {
                    throw new CustomException("申请重量:{},已有重量:{},称重罐上限:{}", potteryAltarApplyWeight, value1, fullTankUpperLimit);
                }
                continue;
            }
            //object 1.0 转换为整型
            int i = Double.valueOf(dViewVarInfo.getValue().toString()).intValue();
            //其他都按照配置进行判断即可
            if (value.getCheck().checkValue(String.valueOf(i), value.getValue())) {
                throw new CustomException(value.getException());
            }
        }

        //称重罐编号
        String weighingTankWeight = weighingTank.getWeighingTankNumber();
        //申请重量
        Double potteryAltarApplyWeight = wineDetails.getPotteryAltarApplyWeight();

        //申请重量测点
        WineDetailPointVO weightPoint = pumpMap.get(WinePointConstants.WEIGHT_POINT);
        //称重罐号测点
        WineDetailPointVO weighingTankNumberPoint = wineDetailPointVOMap.get(WinePointConstants.WEIGHING_TANK_NUMBER);
        //启动信号测点
        WineDetailPointVO startSignalPoint = pumpMap.get(WinePointConstants.START_SIGNAL);
        DviewUtilsPro.writePointValue(
                new DviewPointDTO(weightPoint.getPointId(), weightPoint.getPointType(), weightPoint.getPointName(), String.valueOf(potteryAltarApplyWeight)),
                new DviewPointDTO(weighingTankNumberPoint.getPointId(), weighingTankNumberPoint.getPointType(), weighingTankNumberPoint.getPointName(), weighingTankWeight)
        );
        Thread.sleep(500);
        DviewUtilsPro.writePointValue(new DviewPointDTO(startSignalPoint.getPointId(), startSignalPoint.getPointType(), startSignalPoint.getPointName(), "1"));
    }

    private void pausePlc(List<WineDetailPointVO> pumpPointVOList) {
        /*
            【下发测点】
            以泵为单位急停信号（待提供）
         */
        Map<String, WineDetailPointVO> pumpPointMap = pumpPointVOList.stream().collect(Collectors.toMap(WineDetailPointVO::getUseMark, Function.identity()));
        if (!pumpPointMap.containsKey(WinePointConstants.EMERGENCY_STOP)) {
            throw new CustomException("急停测点不存在");
        }
        WineDetailPointVO wineDetailPointVO = pumpPointMap.get(WinePointConstants.EMERGENCY_STOP);
        DviewUtils.writePointValue(wineDetailPointVO.getPointId(), wineDetailPointVO.getPointType(), "1");
    }

    private void continuePlc(List<WineDetailPointVO> pumpPointVOList, List<WineDetailPointVO> weighingTankPointVOList) throws IOException {
        /*
        【监控测点】
        DJ_SEX_TTK_B1_ES 陶坛库1号泵急停（1为急停状态）
        DJ_SEX_TTK_B1_FAULT 陶坛库1号泵故障（1为故障状态）
        DJ_SEX_TTK_B1_RE 陶坛库1号泵远程（1为远程状态）
        DJ_SEX_TTK_B1_RUN 陶坛库1号泵正运行（0为停止状态）
        FM_SRX_TTK_FM1_CL_FAULT 陶坛库阀门1关故障（1为故障状态）
        FM_SRX_TTK_FM1_OP_FAULT 陶坛库阀门1开故障（1为故障状态）
        SEX_TTK_1_1_ZL_OUT 陶坛库1楼1号秤（确保不超过上限）
        【下发测点】
        以泵为单位下发继续信号（待提供）
         */
        Map<String, WineDetailPointVO> pumpMap = pumpPointVOList.stream().collect(Collectors.toMap(WineDetailPointVO::getUseMark, Function.identity()));
        Map<String, WineDetailPointVO> wineDetailPointVOMap = weighingTankPointVOList.stream().collect(Collectors.toMap(WineDetailPointVO::getUseMark, Function.identity()));
        List<DviewPointDTO> pointIdList = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        for (MonitorPointConfig value : MonitorPointConfig.values()) {
            if (value.getType().equals("2")) {
                if (!pumpMap.containsKey(value.getUseMark())) {
                    throw new CustomException("泵测点不存在");
                }
                WineDetailPointVO wineDetailPointVO = pumpMap.get(value.getUseMark());
                pointIdList.add(new DviewPointDTO(wineDetailPointVO.getPointId(), wineDetailPointVO.getPointType(), wineDetailPointVO.getPointName(), null));
                map.put(value.getUseMark(), wineDetailPointVO.getPointId());
            }
            if (value.getType().equals("1")) {
                if (!wineDetailPointVOMap.containsKey(value.getUseMark())) {
                    throw new CustomException("称重罐测点不存在");
                }
                WineDetailPointVO wineDetailPointVO = wineDetailPointVOMap.get(value.getUseMark());
                pointIdList.add(new DviewPointDTO(wineDetailPointVO.getPointId(), wineDetailPointVO.getPointType(), wineDetailPointVO.getPointName(), null));
                map.put(value.getUseMark(), wineDetailPointVO.getPointId());
            }
        }

        List<DViewVarInfo> pumpPointValues = DviewUtils.queryCachePointValue(pointIdList);
        Map<String, DViewVarInfo> pointValues = pumpPointValues.stream().collect(Collectors.toMap(DViewVarInfo::getName, Function.identity()));

        for (MonitorPointConfig value : MonitorPointConfig.values()) {
            DViewVarInfo dViewVarInfo = pointValues.get(map.get(value.getUseMark()));
            //判断这次遍历是 ZL_OUT
            if (value.name().equals(MonitorPointConfig.ZL_OUT.name())) {
                continue;
            }
            if (value.name().equals(MonitorPointConfig.ES.name())) {
                continue;
            }
            if (value.name().equals(MonitorPointConfig.RUN.name())) {
                continue;
            }
            //其他都按照配置进行判断即可
            if (value.getCheck().checkValue(dViewVarInfo.getValue().toString(), value.getValue())) {
                throw new CustomException(value.getException());
            }
        }
        //继续测点
        WineDetailPointVO weightPoint = pumpMap.get(WinePointConstants.POINT_CONTINUE);
        DviewUtils.writePointValue(weightPoint.getPointId(), weightPoint.getPointType(), "1");
    }

    private void cancelPlc() {
        /*
          【下发测点】
          以泵为单位下发任务取消（待提供）
         */
    }

}
