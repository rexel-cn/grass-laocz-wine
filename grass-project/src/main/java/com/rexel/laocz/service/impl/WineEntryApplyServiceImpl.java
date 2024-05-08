package com.rexel.laocz.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.rexel.common.core.redis.RedisCache;
import com.rexel.common.exception.CustomException;
import com.rexel.common.utils.SequenceUtils;
import com.rexel.dview.pojo.DViewVarInfo;
import com.rexel.laocz.constant.WineConstants;
import com.rexel.laocz.constant.WinePointConstants;
import com.rexel.laocz.domain.*;
import com.rexel.laocz.domain.dto.WineEntryApplyDTO;
import com.rexel.laocz.domain.dto.WineEntryDTO;
import com.rexel.laocz.domain.dto.WineEntryPotteryAltarDTO;
import com.rexel.laocz.domain.dto.WineOutApplyDTO;
import com.rexel.laocz.domain.vo.WineDetailPointVO;
import com.rexel.laocz.domain.vo.WineOperaPotteryAltarVO;
import com.rexel.laocz.domain.vo.WineRealDataVO;
import com.rexel.laocz.dview.DviewUtils;
import com.rexel.laocz.dview.DviewUtilsPro;
import com.rexel.laocz.dview.domain.DviewPointDTO;
import com.rexel.laocz.enums.*;
import com.rexel.laocz.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
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
    private static final String WINE_ENTRY_APPLY_LOCK = "WINE_ENTRY_APPLY_LOCK:";
    private static final AtomicReference<String> atomicReference = new AtomicReference<>();
    private static final AtomicInteger atomicInteger = new AtomicInteger(0);
    private final Map<Long, ScheduledFuture<?>> threadMap = new ConcurrentHashMap<>();
    @Autowired
    private ILaoczLiquorBatchService iLaoczLiquorBatchService;
    @Autowired
    private ILaoczWeighingTankService iLaoczWeighingTankService;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private ILaoczLiquorManagementService iLaoczLiquorManagementService;

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
     * 自动选择陶坛
     *
     * @param applyWeight 申请重量
     * @return 陶坛列表
     */
    @Override
    public List<WineOperaPotteryAltarVO> automaticChoosePotteryAltar(Double applyWeight) {
        List<WineOperaPotteryAltarVO> result = new ArrayList<>();
        if (applyWeight == null || applyWeight <= 0) {
            throw new CustomException("请填写正确的“申请重量”");
        }
        List<WineOperaPotteryAltarVO> wineOperaPotteryAltarVOS = iLaoczPotteryAltarManagementService.wineEntryPotteryAltarList(new WineEntryPotteryAltarDTO());
        if (CollectionUtil.isEmpty(wineOperaPotteryAltarVOS)) {
            return result;
        }
        //计算出总重量对应的陶坛罐，例如满坛重量是100，总重量为1110，那么就需要11个陶坛罐
        for (WineOperaPotteryAltarVO wineOperaPotteryAltarVO : wineOperaPotteryAltarVOS) {
            Double potteryAltarFullAltarWeight = wineOperaPotteryAltarVO.getPotteryAltarFullAltarWeight();
            if (applyWeight > potteryAltarFullAltarWeight) {
                wineOperaPotteryAltarVO.setApplyWeight(potteryAltarFullAltarWeight);
            } else {
                wineOperaPotteryAltarVO.setApplyWeight(applyWeight);
            }
            applyWeight -= potteryAltarFullAltarWeight;
            result.add(wineOperaPotteryAltarVO);
            if (applyWeight <= 0) {
                break;
            }
        }
        return result;
    }

    /**
     * 二维码扫描获取入酒陶坛信息
     *
     * @param potteryAltarNumber 陶坛编号
     * @return 陶坛信息
     */
    @Override
    public WineOperaPotteryAltarVO qrCodeScan(String potteryAltarNumber) {
        //陶坛验证，是否存在，是否被封存， 实时表 验证，是否被使用
        super.potteryAltarNumberCheck(potteryAltarNumber, false);
        WineEntryPotteryAltarDTO wineEntryPotteryAltarDTO = new WineEntryPotteryAltarDTO();
        wineEntryPotteryAltarDTO.setEqPotteryAltarNumber(potteryAltarNumber);
        List<WineOperaPotteryAltarVO> wineOperaPotteryAltarVOS = iLaoczPotteryAltarManagementService.wineEntryPotteryAltarList(wineEntryPotteryAltarDTO);
        if (wineOperaPotteryAltarVOS.isEmpty()) {
            throw new CustomException("系统异常，请联系管理员");
        }
        return wineOperaPotteryAltarVOS.get(0);
    }


    /**
     * 入酒申请
     *
     * @param wineEntryApplyDTO 入酒申请参数：申请重量，陶坛罐ID，酒品管理ID，酒品批次号
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void wineEntryApply(WineEntryApplyDTO wineEntryApplyDTO) {
        //查询： 1：空罐子，2：可使用的
        //自动选择： 1：空罐子，可使用的，重量没问题
        //手动选择： 1：有没有这个罐子。空罐子，可使用的，每个罐子的重量没问题，总重量没问题。
        //二维码扫描：1：有没有这个罐子，空罐子，可使用的，

        //校验
        wineEntryApplyCheck(wineEntryApplyDTO);
        //新增    laocz_liquor_batch
        saveLiquorBatch(wineEntryApplyDTO);
        //将创建工单生成的id与laocz_liquor_batch进行关联更新
        //后续的表也新增
        //新增 laocz_batch_pottery_mapping
        savalaoczBatchPotteryMapping(wineEntryApplyDTO);
        //生成busy_id
        String busyId = SequenceUtils.nextId().toString();
        //新增流程实例
        String processInstanceId = saveProcessInstancesService(busyId, WineProcessDefinitionKeyEnum.IN_WINE);
        //新增laocz_wine_operations
        saveLaoczWineOperations(busyId, processInstanceId, OperationTypeEnum.WINE_ENTRY);
        //新增laocz_wine_details
        saveLaoczWineDetails(wineEntryApplyDTO, busyId, processInstanceId);
    }

    /**
     * 入酒申请校验
     *
     * @param wineEntryApplyDTO
     */
    private void wineEntryApplyCheck(WineEntryApplyDTO wineEntryApplyDTO) {
        //先验证选择的陶坛是否存在
        //验证陶坛是否可以使用，有没有被封存
        //验证陶坛是否已经被申请
        //验证陶坛的重量上限是否满足
        //验证申请重量是否等于陶坛罐申请重量总和

        super.checkLaoczPotteryAltarManagement(wineEntryApplyDTO.getPotteryAltars());
        //检查申请重量是否等于陶坛罐申请重量总和
        double sum = wineEntryApplyDTO.getPotteryAltars().stream().mapToDouble(WineOutApplyDTO::getApplyWeight).sum();
        if (sum != wineEntryApplyDTO.getApplyWeight()) {
            throw new CustomException("申请重量与陶坛罐申请重量不符");
        }
        //检查酒品管理id是否存在
        LaoczLiquorManagement byId = iLaoczLiquorManagementService.getById(wineEntryApplyDTO.getLiquorManagementId());
        if (Objects.isNull(byId)) {
            throw new CustomException("酒品管理不存在");
        }
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
        for (WineOutApplyDTO wineOutApplyDTO : wineEntryApplyDTO.getPotteryAltars()) {
            list.add(buildLaoczWineDetails(busyId, workId, wineEntryApplyDTO.getLiquorBatchId(), wineOutApplyDTO.getPotteryAltarId(), wineOutApplyDTO.getApplyWeight(), WineDetailTypeEnum.IN_WINE, null));
        }
        iLaoczWineDetailsService.saveBatch(list);
    }

    /**
     * 新增批次陶坛关联表
     *
     * @param wineEntryApplyDTO 入酒申请参数
     */
    private void savalaoczBatchPotteryMapping(WineEntryApplyDTO wineEntryApplyDTO) {
        List<LaoczBatchPotteryMapping> laoczBatchPotteryMappings = new ArrayList<>();
        for (WineOutApplyDTO wineOutApplyDTO : wineEntryApplyDTO.getPotteryAltars()) {
            LaoczBatchPotteryMapping laoczBatchPotteryMapping = new LaoczBatchPotteryMapping();
            //批次id
            laoczBatchPotteryMapping.setLiquorBatchId(wineEntryApplyDTO.getLiquorBatchId());
            //陶坛罐id
            laoczBatchPotteryMapping.setPotteryAltarId(wineOutApplyDTO.getPotteryAltarId());
            //运行状态
            laoczBatchPotteryMapping.setRealStatus(RealStatusEnum.OCCUPY.getCode());
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
        LaoczWineDetails wineDetailsById = getWineDetailsById(wineEntryDTO.getWineDetailsId());
        if (Objects.isNull(wineDetailsById)) {
            throw new CustomException("酒操作业务详情不存在");
        }
        checkDetailType(wineDetailsById.getDetailType(), WineDetailTypeEnum.IN_WINE);
        wineIn(wineEntryDTO);
    }

    @Override
    public void wineIn(WineEntryDTO wineEntryDTO) {
        //查询酒操作详情
        LaoczWineDetails wineDetails;
        try {
            redisCache.tryLock(WINE_ENTRY_APPLY_LOCK + wineEntryDTO.getWineDetailsId(), wineEntryDTO.getWineDetailsId(), 10);
            //查询酒操作详情
            wineDetails = getWineDetailsById(wineEntryDTO.getWineDetailsId());
            if (wineDetails == null) {
                throw new CustomException("操作错误，请刷新重试");
            }
            if (wineDetails.getWeighingTank() == null || wineDetails.getPumpId() == null) {
                throw new CustomException("称重罐未配置");
            }
            //入酒状态判断
            busyStatusJudge(wineDetails, wineEntryDTO.getStatus());

            //查询称重罐测点
            List<WineDetailPointVO> weighingTankPointVOList = laoczWineDetailsMapper.selectWineDetailWeighingTankPointVOList(wineDetails.getWineDetailsId());
            //查询泵测点
            List<WineDetailPointVO> pumpPointVOList = laoczWineDetailsMapper.selectWineDetailPumpPointVOList(wineDetails.getWineDetailsId());
            //查询称重罐，需要的业务字段：称重罐重量上限，称重罐编号等。
            LaoczWeighingTank weighingTank = iLaoczWeighingTankService.getById(wineDetails.getWeighingTank());

            if (CollectionUtil.isNotEmpty(pumpPointVOList)) {
                //如果Long为null就移除
                pumpPointVOList.removeIf(Objects::isNull);
                if (CollectionUtil.isEmpty(pumpPointVOList)) {
                    throw new CustomException("泵测点未配置");
                }
            }

            if (CollectionUtil.isNotEmpty(weighingTankPointVOList)) {
                //如果Long为null就移除
                weighingTankPointVOList.removeIf(Objects::isNull);
                if (CollectionUtil.isEmpty(weighingTankPointVOList)) {
                    throw new CustomException("称重罐测点未配置");
                }
            }


            Map<String, WineDetailPointVO> pumpMap = pumpPointVOList.stream().collect(Collectors.toMap(WineDetailPointVO::getUseMark, Function.identity()));
            Map<String, WineDetailPointVO> wineDetailPointVOMap = weighingTankPointVOList.stream().collect(Collectors.toMap(WineDetailPointVO::getUseMark, Function.identity()));

            //检查测点存在与否
            pointCheck(pumpMap, wineDetailPointVOMap);


            //更新实时表状态 入酒开始
            super.updatePotteryMappingState(wineDetails.getPotteryAltarId(), RealStatusEnum.WINE_IN);


            String eventStatus = WineConstants.SUCCESS;
            try {
                switch (WineOperationTypeEnum.getByValue(wineEntryDTO.getStatus())) {
                    case START:
                        //开始
                        start(wineDetails, wineDetailPointVOMap, pumpMap, weighingTank);
                        break;
                    case EMERGENCY_STOP:
                        //急停
                        pause(wineDetails, pumpMap);
                        break;
                    case CONTINUE:
                        //继续
                        winContinue(wineDetails, pumpMap, wineDetailPointVOMap);
                        break;
                    default:
                        throw new CustomException("入酒状态错误");
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.error("入酒操作异常:{}", e.getMessage());
                eventStatus = WineConstants.FAIL;
                throw new CustomException("入酒操作异常：{}", e.getMessage());
            } finally {
                String finalEventStatus = eventStatus;
                threadPoolTaskScheduler.execute(() -> saveWineEvent(wineDetails, wineEntryDTO, weighingTankPointVOList, pumpPointVOList, finalEventStatus));
            }
        } finally {
            redisCache.deleteObject(WINE_ENTRY_APPLY_LOCK + wineEntryDTO.getWineDetailsId());
        }
    }

    private void pointCheck(Map<String, WineDetailPointVO> pumpMap, Map<String, WineDetailPointVO> wineDetailPointVOMap) {
        for (MonitorPointConfig value : MonitorPointConfig.values()) {
            if (value.getType().equals("2")) {
                if (!pumpMap.containsKey(value.getUseMark())) {
                    throw new CustomException("泵测点不存在");
                }
            }
            if (value.getType().equals("1")) {
                if (!wineDetailPointVOMap.containsKey(value.getUseMark())) {
                    throw new CustomException("称重罐测点不存在");
                }
            }
        }
        //申请重量测点
        WineDetailPointVO weightPoint = pumpMap.get(WinePointConstants.WEIGHT_POINT);
        if (weightPoint == null) {
            throw new CustomException("申请重量测点不存在");
        }
        //称重罐号测点
        WineDetailPointVO weighingTankNumberPoint = pumpMap.get(WinePointConstants.WEIGHING_TANK_NUMBER);
        if (weighingTankNumberPoint == null) {
            throw new CustomException("称重罐号测点不存在");
        }
        //启动信号测点
        WineDetailPointVO startSignalPoint = pumpMap.get(WinePointConstants.START_SIGNAL);
        if (startSignalPoint == null) {
            throw new CustomException("启动信号测点不存在");
        }

    }


    /**
     * 入酒状态判断
     *
     * @param wineDetails 酒操作详情
     * @param status      入酒状态
     */
    private void busyStatusJudge(LaoczWineDetails wineDetails, String status) {
        switch (WineOperationTypeEnum.getByValue(status)) {
            case START:
                //开始
                if (Objects.equals(wineDetails.getBusyStatus(), WineBusyStatusEnum.STARTED.getValue())) {
                    throw new CustomException("该任务正在执行中");
                }
                if (Objects.equals(wineDetails.getBusyStatus(), WineBusyStatusEnum.EMERGENCY_STOP.getValue())) {
                    throw new CustomException("该任务已经急停,通过继续按钮启动");
                }
                if (Objects.equals(wineDetails.getBusyStatus(), WineBusyStatusEnum.COMPLETED.getValue())) {
                    throw new CustomException("该任务已经完成");
                }
                break;
            case EMERGENCY_STOP:
                //急停
                if (Objects.equals(wineDetails.getBusyStatus(), WineBusyStatusEnum.NOT_STARTED.getValue())) {
                    throw new CustomException("该任务未开始");
                }
                if (Objects.equals(wineDetails.getBusyStatus(), WineBusyStatusEnum.EMERGENCY_STOP.getValue())) {
                    throw new CustomException("该任务已急停");
                }
                if (Objects.equals(wineDetails.getBusyStatus(), WineBusyStatusEnum.COMPLETED.getValue())) {
                    throw new CustomException("该任务已经完成");
                }
                break;
            case CONTINUE:
                //继续
                if (Objects.equals(wineDetails.getBusyStatus(), WineBusyStatusEnum.NOT_STARTED.getValue())) {
                    throw new CustomException("该任务未开始");
                }
                if (Objects.equals(wineDetails.getBusyStatus(), WineBusyStatusEnum.COMPLETED.getValue())) {
                    throw new CustomException("该任务已经完成");
                }
                if (Objects.equals(wineDetails.getBusyStatus(), WineBusyStatusEnum.STARTED.getValue())) {
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
     * @param wineDetailsId      酒操作详情id
     * @param WineBusyStatusEnum 酒操作状态
     */
    private void updateWineDetails(Long wineDetailsId, WineBusyStatusEnum WineBusyStatusEnum) {
        iLaoczWineDetailsService.lambdaUpdate()
                .eq(LaoczWineDetails::getWineDetailsId, wineDetailsId)
                .set(LaoczWineDetails::getBusyStatus, WineBusyStatusEnum.getValue())
                .update();
    }


    /**
     * 保存酒事件
     *
     * @param wineDetails             酒操作详情
     * @param wineEntryDTO            入酒参数
     * @param weighingTankPointVOList 称重罐测点
     * @param pumpPointVOList         泵测点
     * @param eventStatus             事件状态
     */
    private void saveWineEvent(LaoczWineDetails wineDetails, WineEntryDTO wineEntryDTO, List<WineDetailPointVO> weighingTankPointVOList, List<WineDetailPointVO> pumpPointVOList, String eventStatus) {
        List<String> weightPoints = weighingTankPointVOList.stream().map(WineDetailPointVO::getPointId).collect(Collectors.toList());
        List<String> pumpPoints = pumpPointVOList.stream().map(WineDetailPointVO::getPointId).collect(Collectors.toList());
        weightPoints.addAll(pumpPoints);
        super.saveWineEvent(wineDetails, wineEntryDTO.getStatus(), weightPoints, eventStatus);
    }

    /**
     * 入酒继续.并继续监听
     *
     * @param wineDetails          酒操作详情
     * @param pumpMap              泵测点
     * @param wineDetailPointVOMap 称重罐
     * @throws IOException 异常
     */
    private void winContinue(LaoczWineDetails wineDetails, Map<String, WineDetailPointVO> pumpMap, Map<String, WineDetailPointVO> wineDetailPointVOMap) throws IOException {
        //更新酒操作详情状态，开始
        updateWineDetails(wineDetails.getWineDetailsId(), WineBusyStatusEnum.STARTED);
        //下发继续测点
        continuePlc(pumpMap, wineDetailPointVOMap);
        //继续监听
        startListener(wineDetails.getWineDetailsId(), wineDetailPointVOMap, pumpMap);
    }

    /**
     * 下发急停，并取消监听
     *
     * @param wineDetails 酒操作详情
     * @param pumpMap     泵测点
     */
    private void pause(LaoczWineDetails wineDetails, Map<String, WineDetailPointVO> pumpMap) {
        //更新酒操作详情状态，开始
        updateWineDetails(wineDetails.getWineDetailsId(), WineBusyStatusEnum.EMERGENCY_STOP);
        //下发急停测点
        pausePlc(pumpMap);
        //取消监听
        pauseListener(wineDetails.getWineDetailsId());
    }

    /**
     * 下发启动并开始监听
     *
     * @param wineDetails          酒操作详情
     * @param wineDetailPointVOMap 称重罐测点
     * @param pumpMap              泵测点
     * @param weighingTank         称重罐
     * @throws IOException          io网络异常，测点-dview交互异常
     * @throws InterruptedException 打断异常
     */
    private void start(LaoczWineDetails wineDetails, Map<String, WineDetailPointVO> wineDetailPointVOMap, Map<String, WineDetailPointVO> pumpMap, LaoczWeighingTank weighingTank) throws IOException, InterruptedException {
        //记录开始前的重量及开始时间，更新酒操作详情状态，开始
        recordWeightAndTimeBeforeStart(wineDetails, wineDetailPointVOMap);
        //下发测点
        startPlc(wineDetails, wineDetailPointVOMap, pumpMap, weighingTank);
        //开始监听
        startListener(wineDetails.getWineDetailsId(), wineDetailPointVOMap, pumpMap);
    }

    private void recordWeightAndTimeBeforeStart(LaoczWineDetails wineDetails, Map<String, WineDetailPointVO> wineDetailPointVOMap) throws IOException {
        //称重罐重量测点
        WineDetailPointVO zlOut = wineDetailPointVOMap.get(WinePointConstants.ZL_OUT);
        String weight = DviewUtils.queryPointValue(zlOut.getPointId(), zlOut.getPointType());
        if (StrUtil.isEmpty(weight)) {
            throw new CustomException("称重罐重量未获取");
        }
        iLaoczWineDetailsService.lambdaUpdate()
                .eq(LaoczWineDetails::getWineDetailsId, wineDetails.getWineDetailsId())
                .set(LaoczWineDetails::getBeforeTime, new Date())
                .set(LaoczWineDetails::getBusyStatus, WineBusyStatusEnum.STARTED.getValue())
                .set(LaoczWineDetails::getBeforeWeight, Double.parseDouble(weight)).update();


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
            Double beforeWeight = laoczWineDetails.getBeforeWeight();
            if(beforeWeight!=null){
                String realWeight = DviewUtils.queryCachePointValue(wineDetailPointVO.getPointId(), wineDetailPointVO.getPointType());
                if(StrUtil.isNotEmpty(realWeight)){
                    wineRealDataVO.setCurrentWeight(BigDecimal.valueOf(beforeWeight).subtract(BigDecimal.valueOf(Double.parseDouble(realWeight))).setScale(BigDecimal.ROUND_HALF_EVEN, RoundingMode.CEILING).doubleValue());
                }
            }
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
        //判断是否已经有了称重罐重量以及是否已经完成
        finishCheck(laoczWineDetails);
        //更新陶坛实时关系表，入酒，更新为存储，更新实际重量（为称重罐的实际重量）
        super.updatePotteryMappingState(laoczWineDetails.getPotteryAltarId(), "+", laoczWineDetails.getWeighingTankWeight());
        //备份酒操作业务表
        super.backupWineDetails(laoczWineDetails);
        //新增数据到历史表
        super.saveHistory(laoczWineDetails);
        //查询当前业务id还有没有正在完成的任务，如果没有了，就备份酒操作业务表
        super.taskVerify(laoczWineDetails.getBusyId());
    }

    /**
     * 入酒完成，校验
     *
     * @param laoczWineDetails 酒操作详情
     */
    private void finishCheck(LaoczWineDetails laoczWineDetails) {
        //判断是否已经有了称重罐重量以及是否已经完成
        if (laoczWineDetails.getWeighingTankWeight() == null) {
            throw new CustomException("称重罐重量未获取");
        }
        if (!Objects.equals(laoczWineDetails.getBusyStatus(), WineBusyStatusEnum.COMPLETED.getValue())) {
            throw new CustomException("该任务未完成");
        }
    }

    /**
     * 取消监听称重罐重量
     *
     * @param wineDetailsId 酒操作业务详情id
     */
    private void pauseListener(Long wineDetailsId) {
        ScheduledFuture<?> scheduledFuture = threadMap.get(wineDetailsId);
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
            threadMap.remove(wineDetailsId);
        }
    }

    /**
     * 启动监听，监听称重罐重量，以及完成信号，如果完成记录称重罐重量，并更新相关数据,然后取消监听
     *
     * @param wineDetailsId        酒操作业务详情id
     * @param wineDetailPointVOMap 称重罐测点
     * @param pointVOMap           泵测点
     */
    private void startListener(Long wineDetailsId, Map<String, WineDetailPointVO> wineDetailPointVOMap, Map<String, WineDetailPointVO> pointVOMap) {
        ScheduledFuture<?> future = threadMap.get(wineDetailsId);
        if (future != null) {
            return;
        }
        //完成测点
        WineDetailPointVO finishPoint = pointVOMap.get(WinePointConstants.FINISH_POINT);
        //称重罐重量测点
        WineDetailPointVO zlOut = wineDetailPointVOMap.get(WinePointConstants.ZL_OUT);
        //监听完成测点,如果完成，查询称重罐重量并保存到数据库中
        final Runnable task = () -> {
            try {
                String finish = DviewUtils.queryPointValue(finishPoint.getPointId(), finishPoint.getPointType());
                if (StrUtil.isEmpty(finish)) {
                    log.error("入酒监听获取完成信号失败");
                    return;
                }
                if (Double.parseDouble(finish) == 1) {
                    String weight = DviewUtils.queryPointValue(zlOut.getPointId(), zlOut.getPointType());
                    if (StrUtil.isEmpty(weight)) {
                        log.error("入酒监听获取重量失败");
                        return;
                    }
                    try {
                        redisCache.tryLock(WINE_ENTRY_APPLY_LOCK + wineDetailsId, wineDetailsId, 10);
                        LaoczWineDetails wineDetails = iLaoczWineDetailsService.getById(wineDetailsId);
                        //获取实际重量= 开始前重量 - 结束后重量
                        double weighingTankWeight = BigDecimal.valueOf(wineDetails.getBeforeWeight()).subtract(BigDecimal.valueOf(Double.parseDouble(weight))).doubleValue();

                        iLaoczWineDetailsService.lambdaUpdate()
                                .eq(LaoczWineDetails::getWineDetailsId, wineDetailsId)
                                .set(LaoczWineDetails::getWeighingTankWeight, weighingTankWeight)
                                .set(LaoczWineDetails::getAfterWeight, Double.parseDouble(weight))
                                .set(LaoczWineDetails::getAfterTime, new Date())
                                .set(LaoczWineDetails::getBusyStatus, WineBusyStatusEnum.COMPLETED.getValue())
                                .update();
                        //每次完成信号为1后，读取重量，然后完成信号复位
                        DviewUtils.writePointValue(finishPoint.getPointId(),finishPoint.getPointType(),"0");
                        pauseListener(wineDetailsId);
                    } finally {
                        redisCache.delete(WINE_ENTRY_APPLY_LOCK + wineDetailsId);
                    }
                }
            } catch (IOException e) {
                log.error("入酒监听异常，酒详情ID:{},完成测点:{},称重测点:{},异常信息", wineDetailsId, finishPoint.getPointId(), zlOut.getPointId(), e);
            }
        };
        ScheduledFuture<?> scheduledFuture = threadPoolTaskScheduler.scheduleAtFixedRate(task, 0, 1, TimeUnit.SECONDS);
        threadMap.put(wineDetailsId, scheduledFuture);
    }

    /**
     * 下发启动测点
     *
     * @param wineDetails          酒操作详情
     * @param wineDetailPointVOMap 称重罐测点
     * @param pumpMap              泵测点
     * @param weighingTank         称重罐
     * @throws IOException          io网络异常，测点-dview交互异常
     * @throws InterruptedException 打断异常
     */
    private void startPlc(LaoczWineDetails wineDetails, Map<String, WineDetailPointVO> wineDetailPointVOMap, Map<String, WineDetailPointVO> pumpMap, LaoczWeighingTank weighingTank) throws IOException, InterruptedException {
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
        List<DviewPointDTO> pointIdList = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        for (Map.Entry<String, WineDetailPointVO> stringWineDetailPointVOEntry : pumpMap.entrySet()) {
            pointIdList.add(new DviewPointDTO(stringWineDetailPointVOEntry.getValue().getPointId(), stringWineDetailPointVOEntry.getValue().getPointType(), stringWineDetailPointVOEntry.getValue().getPointName(), null));
            map.put(stringWineDetailPointVOEntry.getKey(), stringWineDetailPointVOEntry.getValue().getPointId());
        }
        for (Map.Entry<String, WineDetailPointVO> stringWineDetailPointVOEntry : wineDetailPointVOMap.entrySet()) {
            pointIdList.add(new DviewPointDTO(stringWineDetailPointVOEntry.getValue().getPointId(), stringWineDetailPointVOEntry.getValue().getPointType(), stringWineDetailPointVOEntry.getValue().getPointName(), null));
            map.put(stringWineDetailPointVOEntry.getKey(), stringWineDetailPointVOEntry.getValue().getPointId());
        }

        List<DViewVarInfo> pumpPointValues = DviewUtils.queryCachePointValue(pointIdList);
        Map<String, DViewVarInfo> pointValues = pumpPointValues.stream().collect(Collectors.toMap(DViewVarInfo::getName, Function.identity()));

        for (MonitorPointConfig value : MonitorPointConfig.values()) {
            DViewVarInfo dViewVarInfo = pointValues.get(map.get(value.getUseMark()));
            //判断这次遍历是 ZL_OUT
            if (value.name().equals(MonitorPointConfig.ZL_OUT.name())) {
//                //判断是否上限
//                //称重罐上限
//                String fullTankUpperLimit = weighingTank.getFullTankUpperLimit();
//                //申请重量
//                Double potteryAltarApplyWeight = wineDetails.getPotteryAltarApplyWeight();
//                //查询已有的重量
//                Double value1 = Double.parseDouble(dViewVarInfo.getValue().toString());
//                //判断申请重量+已有重量>称重罐上限就报错
//                if (!value.getCheck().checkValue(String.valueOf(potteryAltarApplyWeight + value1), fullTankUpperLimit)) {
//                    throw new CustomException("申请重量:{},已有重量:{},称重罐上限:{}", potteryAltarApplyWeight, value1, fullTankUpperLimit);
//                }
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
        String about = weighingTank.getAbout();

        //申请重量
        Double potteryAltarApplyWeight = wineDetails.getPotteryAltarApplyWeight();

        //申请重量测点
        WineDetailPointVO weightPoint = pumpMap.get(WinePointConstants.WEIGHT_POINT);
        if (weightPoint == null) {
            throw new CustomException("申请重量测点不存在");
        }
        //称重罐号测点
        WineDetailPointVO weighingTankNumberPoint = pumpMap.get(WinePointConstants.WEIGHING_TANK_NUMBER);
        if (weighingTankNumberPoint == null) {
            throw new CustomException("称重罐号测点不存在");
        }
        //启动信号测点
        WineDetailPointVO startSignalPoint = pumpMap.get(WinePointConstants.START_SIGNAL);
        if (startSignalPoint == null) {
            throw new CustomException("启动信号测点不存在");
        }


        DviewUtilsPro.writePointValue(
                new DviewPointDTO(weightPoint.getPointId(), weightPoint.getPointType(), weightPoint.getPointName(), String.valueOf(potteryAltarApplyWeight)),
                new DviewPointDTO(weighingTankNumberPoint.getPointId(), weighingTankNumberPoint.getPointType(), weighingTankNumberPoint.getPointName(), about)
        );
        Thread.sleep(500);
        DviewUtilsPro.writePointValue(new DviewPointDTO(startSignalPoint.getPointId(), startSignalPoint.getPointType(), startSignalPoint.getPointName(), "1"));
    }

    /**
     * 下发急停功能
     *
     * @param pumpPointMap 泵测点
     */
    private void pausePlc(Map<String, WineDetailPointVO> pumpPointMap) {
        /*
            【下发测点】
            以泵为单位急停信号（待提供）
         */
        if (!pumpPointMap.containsKey(WinePointConstants.EMERGENCY_STOP)) {
            throw new CustomException("急停测点不存在");
        }
        WineDetailPointVO wineDetailPointVO = pumpPointMap.get(WinePointConstants.EMERGENCY_STOP);
        DviewUtils.writePointValue(wineDetailPointVO.getPointId(), wineDetailPointVO.getPointType(), "1");
    }

    /**
     * 继续测点
     *
     * @param pumpMap              泵测点
     * @param wineDetailPointVOMap 称重罐测点
     * @throws IOException io网络异常，测点-dview交互异常
     */
    private void continuePlc(Map<String, WineDetailPointVO> pumpMap, Map<String, WineDetailPointVO> wineDetailPointVOMap) throws IOException {
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

        List<DviewPointDTO> pointIdList = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        for (Map.Entry<String, WineDetailPointVO> stringWineDetailPointVOEntry : pumpMap.entrySet()) {
            pointIdList.add(new DviewPointDTO(stringWineDetailPointVOEntry.getValue().getPointId(), stringWineDetailPointVOEntry.getValue().getPointType(), stringWineDetailPointVOEntry.getValue().getPointName(), null));
            map.put(stringWineDetailPointVOEntry.getKey(), stringWineDetailPointVOEntry.getValue().getPointId());
        }
        for (Map.Entry<String, WineDetailPointVO> stringWineDetailPointVOEntry : wineDetailPointVOMap.entrySet()) {
            pointIdList.add(new DviewPointDTO(stringWineDetailPointVOEntry.getValue().getPointId(), stringWineDetailPointVOEntry.getValue().getPointType(), stringWineDetailPointVOEntry.getValue().getPointName(), null));
            map.put(stringWineDetailPointVOEntry.getKey(), stringWineDetailPointVOEntry.getValue().getPointId());
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
