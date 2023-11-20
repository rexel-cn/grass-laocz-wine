package com.rexel.earlywarning.service.impl;

import java.util.*;
import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson2.JSONObject;
import com.rexel.common.core.redis.RedisCache;
import com.rexel.common.exception.ServiceException;
import com.rexel.common.utils.DateUtils;
import com.rexel.common.utils.SecurityUtils;
import com.rexel.common.utils.StringUtils;
import com.rexel.common.utils.bean.BeanUtils;
import com.rexel.earlywarning.common.constants.EarlyConstants;
import com.rexel.earlywarning.common.enums.MqttEnums;
import com.rexel.earlywarning.common.utils.*;
import com.rexel.earlywarning.domain.*;
import com.rexel.earlywarning.mapper.*;
import com.rexel.earlywarning.service.IGrassEarlyWarningService;
import com.rexel.earlywarning.vo.GrassEarlyWarningAlarmData;
import com.rexel.nsq.rule.EarlyWarningSendMsgAsync;
import com.rexel.system.domain.GrassDeviceInfo;
import com.rexel.system.domain.GrassPointInfo;
import com.rexel.system.domain.vo.RuleNoticeVO;
import com.rexel.system.mapper.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.PostConstruct;
import static java.util.stream.Collectors.toList;

/**
 * 预警规则Service业务层处理
 *
 * @author admin
 * @date 2022-01-14
 */
@Slf4j
@Service
public class GrassEarlyWarningServiceImpl implements IGrassEarlyWarningService {
    private final List<String> oldTopicList = new ArrayList<>();
    @Autowired
    private GrassEarlyWarningTriggerHisMapper grassEarlyWarningTriggerHisMapper;
    @Autowired
    private GrassEarlyWarningSuggestHisMapper grassEarlyWarningSuggestHisMapper;
    @Autowired
    private GrassEarlyWarningFinishHisMapper grassEarlyWarningFinishHisMapper;
    @Autowired
    private GrassEarlyWarningJudgeHisMapper grassEarlyWarningJudgeHisMapper;
    @Autowired
    private GrassEarlyWarningAlarmHisMapper grassEarlyWarningAlarmHisMapper;
    @Autowired
    private GrassEarlyWarningTriggerMapper grassEarlyWarningTriggerMapper;
    @Autowired
    private GrassEarlyWarningCarrierMapper grassEarlyWarningCarrierMapper;
    @Autowired
    private GrassEarlyWarningSuggestMapper grassEarlyWarningSuggestMapper;
    @Autowired
    private GrassEarlyWarningNoticeMapper grassEarlyWarningNoticeMapper;
    @Autowired
    private GrassEarlyWarningFinishMapper grassEarlyWarningFinishMapper;
    @Autowired
    private GrassEarlyWarningJudgeMapper grassEarlyWarningJudgeMapper;
    @Autowired
    private GrassEarlyWarningHisMapper grassEarlyWarningHisMapper;
    @Autowired
    private GrassEarlyWarningMapper grassEarlyWarningMapper;
    @Autowired
    private GrassDeviceInfoMapper grassDeviceInfoMapper;
    @Autowired
    private GrassPointInfoMapper grassPointInfoMapper;
    @Autowired
    private EarlyWarningSendMsgAsync sendMsgAsync;
    @Autowired
    private EaryWarningMqtt earyWarningMqtt;
    @Autowired
    private RedisCache redisCache;

    /**
     * 开机启动任务
     */
    @PostConstruct
    public void startUpEarlyWarning() {
//        refreshMqttListener();
    }

    /**
     * 查询预警规则
     *
     * @param rulesId 预警规则ID
     * @return 预警规则
     */
    @Override
    public GrassEarlyWarning selectGrassEarlyWarningById(Long rulesId) {
        // 查询预警规则
        GrassEarlyWarning earlyWarning = grassEarlyWarningMapper.selectGrassEarlyWarningById(rulesId);
        if (earlyWarning == null) {
            throw new ServiceException("预警规则不存在");
        }

        // 查询触发条件
        List<GrassEarlyWarningTrigger> triggerList =
                grassEarlyWarningTriggerMapper.selectGrassEarlyWarningTriggerByRulesId(rulesId);
        triggerList.forEach(data -> {
            data.setDeviceInfoList(getDeviceInfoList(data.getDeviceId()));
            data.setPointInfoList(getPointInfoList(data.getDeviceId()));
        });

        // 查询判断条件
        List<GrassEarlyWarningJudge> judgeList =
                grassEarlyWarningJudgeMapper.selectGrassEarlyWarningJudgeByRulesId(rulesId);
        judgeList.forEach(data -> {
            data.setDeviceInfoList(getDeviceInfoList(data.getDeviceId()));
            data.setPointInfoList(getPointInfoList(data.getDeviceId()));
        });

        // 查询结束条件
        List<GrassEarlyWarningFinish> finishList =
                grassEarlyWarningFinishMapper.selectGrassEarlyWarningFinishByRulesId(rulesId);
        finishList.forEach(data -> {
            data.setDeviceInfoList(getDeviceInfoList(data.getDeviceId()));
            data.setPointInfoList(getPointInfoList(data.getDeviceId()));
        });

        // 查询运行设备
        List<GrassEarlyWarningCarrier> carrierList =
                grassEarlyWarningCarrierMapper.selectGrassEarlyWarningCarrierListByRulesId(rulesId);

        // 查询通知模板
        List<GrassEarlyWarningNotice> noticeList =
                grassEarlyWarningNoticeMapper.selectGrassEarlyWarningNoticeByRulesId(rulesId);

        // 通知模板列表
        List<Long> noticeTempIdList = new ArrayList<>();
        noticeList.forEach(data -> noticeTempIdList.add(data.getNoticeTemplateId()));

        // 返回应答结果
        earlyWarning.setCarrierList(carrierList);
        earlyWarning.setTriggerList(triggerList);
        earlyWarning.setJudgeList(judgeList);
        earlyWarning.setFinishList(finishList);
        earlyWarning.setNoticeList(noticeList);
        earlyWarning.setNoticeTempIdList(noticeTempIdList);
        return earlyWarning;
    }

    /**
     * 查询预警规则列表
     *
     * @param grassEarlyWarning 预警规则
     * @return 预警规则
     */
    @Override
    public List<GrassEarlyWarning> selectGrassEarlyWarningList(GrassEarlyWarning grassEarlyWarning) {
        // 查询预警规则列表
        List<GrassEarlyWarning> list = grassEarlyWarningMapper.selectGrassEarlyWarningList(grassEarlyWarning);

        // 设置规则运行状态
        for (GrassEarlyWarning earlyWarning : list) {
            String jobStatus = getJobStatus(earlyWarning.getRulesId(), earlyWarning.getTenantId());
            earlyWarning.setJobStatus(jobStatus);
        }
        return list;
    }

    /**
     * 新增预警规则
     *
     * @param grassEarlyWarning 预警规则
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertGrassEarlyWarning(GrassEarlyWarning grassEarlyWarning) {
        // 初始化创建参数
        initEarlyWarning(grassEarlyWarning);

        // 检查请求参数
        checkInsertParam(grassEarlyWarning);

        // 创建预警规则
        grassEarlyWarning.setCreateBy(SecurityUtils.getUsername());
        grassEarlyWarning.setCreateTime(new Date());
        grassEarlyWarning.setRulesState(EarlyConstants.RULE_STATE_CLOSE);
        grassEarlyWarningMapper.insertGrassEarlyWarning(grassEarlyWarning);

        // 获取规则ID
        Long rulesId = grassEarlyWarning.getRulesId();

        // 创建运行设备
        List<GrassEarlyWarningCarrier> carrierList = grassEarlyWarning.getCarrierList();
        createCarrierList(rulesId, carrierList);

        // 创建触发条件
        List<GrassEarlyWarningTrigger> triggerList = grassEarlyWarning.getTriggerList();
        createTriggerList(rulesId, triggerList);

        // 创建判断条件
        List<GrassEarlyWarningJudge> judgeList = grassEarlyWarning.getJudgeList();
        createJudgeList(rulesId, judgeList);

        // 创建结束条件
        List<GrassEarlyWarningFinish> finishList = grassEarlyWarning.getFinishList();
        createFinishList(rulesId, finishList);

        // 创建通知模板
        List<Long> noticeTempIdList = grassEarlyWarning.getNoticeTempIdList();
        createNoticeList(rulesId, noticeTempIdList);

        // 创建引擎任务
        if (EarlyConstants.RULE_STATE_OPEN.equals(grassEarlyWarning.getRulesState())) {
            // 刷新监听对象
            refreshMqttListener();
            // 提交引擎任务
            submitJob(rulesId);
        }
    }

    /**
     * 修改预警规则
     *
     * @param grassEarlyWarning 预警规则
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateGrassEarlyWarning(GrassEarlyWarning grassEarlyWarning) {
        Long rulesId = grassEarlyWarning.getRulesId();
        if (rulesId == null) {
            throw new ServiceException("预警规则ID为空");
        }

        // 初始化创建参数
        initEarlyWarning(grassEarlyWarning);

        // 检查请求参数
        checkInsertParam(grassEarlyWarning);

        // 是否需要更新规则任务
        boolean bUpdateJob = needUpdateJob(grassEarlyWarning);

        // 删除规则任务
        if (bUpdateJob) {
            deleteJob(rulesId);
        }

        // 删除并重新创建运行设备
        grassEarlyWarningCarrierMapper.deleteGrassEarlyWarningCarrierByRulesId(rulesId);
        List<GrassEarlyWarningCarrier> carrierList = grassEarlyWarning.getCarrierList();
        createCarrierList(rulesId, carrierList);

        // 删除并重新创建触发条件
        grassEarlyWarningTriggerMapper.deleteGrassEarlyWarningTriggerByRulesId(rulesId);
        List<GrassEarlyWarningTrigger> triggerList = grassEarlyWarning.getTriggerList();
        createTriggerList(rulesId, triggerList);

        // 删除并重新创建判断条件
        grassEarlyWarningJudgeMapper.deleteGrassEarlyWarningJudgeByRulesId(rulesId);
        List<GrassEarlyWarningJudge> judgeList = grassEarlyWarning.getJudgeList();
        createJudgeList(rulesId, judgeList);

        // 删除并重新创建结束条件
        grassEarlyWarningFinishMapper.deleteGrassEarlyWarningFinishByRulesId(rulesId);
        List<GrassEarlyWarningFinish> finishList = grassEarlyWarning.getFinishList();
        createFinishList(rulesId, finishList);

        // 删除并重新创建通知模板
        grassEarlyWarningNoticeMapper.deleteGrassEarlyWarningNoticeByRulesId(rulesId);
        List<Long> noticeTempIdList = grassEarlyWarning.getNoticeTempIdList();
        createNoticeList(rulesId, noticeTempIdList);

        // 更新预警规则
        grassEarlyWarning.setUpdateBy(SecurityUtils.getUsername());
        grassEarlyWarning.setUpdateTime(new Date());
        grassEarlyWarningMapper.updateGrassEarlyWarning(grassEarlyWarning);

        // 更新规则任务
        if (bUpdateJob) {
            // 刷新监听对象
            refreshMqttListener();

            // 创建规则任务
            submitJob(rulesId);
        }
    }

    /**
     * 修改预警规则状态
     *
     * @param grassEarlyWarning 预警规则
     */
    @Override
    public void updateGrassEarlyWarningStatus(GrassEarlyWarning grassEarlyWarning) {
        Long rulesId = grassEarlyWarning.getRulesId();
        if (rulesId == null) {
            throw new ServiceException("预警规则ID为空");
        }

        // 更新预警规则
        grassEarlyWarning.setUpdateBy(SecurityUtils.getUsername());
        grassEarlyWarning.setUpdateTime(new Date());
        grassEarlyWarningMapper.updateGrassEarlyWarning(grassEarlyWarning);

        // 刷新监听对象
        refreshMqttListener();

        // 更新引擎任务
        if (EarlyConstants.RULE_STATE_CLOSE.equals(grassEarlyWarning.getRulesState())) {
            deleteJob(rulesId);
        } else {
            submitJob(rulesId);
        }
    }

    /**
     * 修改预警规则模板状态
     *
     * @param grassEarlyWarning 预警规则
     */
    @Override
    public void updateGrassEarlyWarningTemplate(GrassEarlyWarning grassEarlyWarning) {
        Long rulesId = grassEarlyWarning.getRulesId();
        if (rulesId == null) {
            throw new ServiceException("预警规则ID为空");
        }

        // 查询现行规则
        GrassEarlyWarning info = grassEarlyWarningMapper.selectGrassEarlyWarningById(rulesId);
        if (info == null) {
            throw new ServiceException("指定的预警规则ID不存在");
        }

        // 更新预警规则
        info.setIsTemplate(grassEarlyWarning.getIsTemplate());
        info.setUpdateBy(SecurityUtils.getUsername());
        info.setUpdateTime(new Date());
        grassEarlyWarningMapper.updateGrassEarlyWarning(info);
    }

    /**
     * 删除预警规则信息
     *
     * @param rulesId 预警规则ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteGrassEarlyWarningById(Long rulesId) {
        GrassEarlyWarning earlyWarning = grassEarlyWarningMapper.selectGrassEarlyWarningById(rulesId);
        if (earlyWarning == null) {
            throw new ServiceException("预警规则不存在。");
        }

        // 删除引擎任务
        deleteJob(rulesId);

        // 删除运行设备
        grassEarlyWarningCarrierMapper.deleteGrassEarlyWarningCarrierByRulesId(rulesId);

        // 删除触发条件
        grassEarlyWarningTriggerMapper.deleteGrassEarlyWarningTriggerByRulesId(rulesId);

        // 删除判断条件
        grassEarlyWarningJudgeMapper.deleteGrassEarlyWarningJudgeByRulesId(rulesId);

        // 删除结束条件
        grassEarlyWarningFinishMapper.deleteGrassEarlyWarningFinishByRulesId(rulesId);

        // 删除通知模板
        grassEarlyWarningNoticeMapper.deleteGrassEarlyWarningNoticeByRulesId(rulesId);

        // 删除预警规则
        grassEarlyWarningMapper.deleteGrassEarlyWarningByRulesId(rulesId);

        // 刷新监听对象
        refreshMqttListener();
    }

    /**
     * 删除预警规则信息
     *
     * @param tenantId tenantId
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteGrassEarlyWarningByTenantId(String tenantId) {
        GrassEarlyWarning select = new GrassEarlyWarning();
        select.setTenantId(tenantId);
        List<GrassEarlyWarning> list = grassEarlyWarningMapper.selectGrassEarlyWarningList(select);

        // 删除触发条件
        grassEarlyWarningTriggerMapper.deleteGrassEarlyWarningTriggerByTenantId(tenantId);

        // 删除判断条件
        grassEarlyWarningJudgeMapper.deleteGrassEarlyWarningJudgeByTenantId(tenantId);

        // 删除结束条件
        grassEarlyWarningFinishMapper.deleteGrassEarlyWarningFinishByTenantId(tenantId);

        // 删除通知模板
        grassEarlyWarningNoticeMapper.deleteGrassEarlyWarningNoticeByTenantId(tenantId);

        // 删除预警规则
        grassEarlyWarningMapper.deleteGrassEarlyWarningByTenantId(tenantId);

        // 删除运行设备
        for (GrassEarlyWarning earlyWarning : list) {
            grassEarlyWarningCarrierMapper.selectGrassEarlyWarningCarrierListByRulesId(earlyWarning.getRulesId());
        }

        // 刷新监听对象
        refreshMqttListener();

        // 删除引擎任务
        for (GrassEarlyWarning earlyWarning : list) {
            deleteJob(earlyWarning.getRulesId());
            doSleep();
        }
    }

    /**
     * 查询所有预警规则
     */
    @Override
    public void submitAllJob() {
        // 查询预警规则
        GrassEarlyWarning select = new GrassEarlyWarning();
        select.setRulesState(EarlyConstants.RULE_STATE_OPEN);
        List<GrassEarlyWarning> list = grassEarlyWarningMapper.selectGrassEarlyWarningList(select);

        // 遍历所有规则
        for (GrassEarlyWarning earlyWarning : list) {
            submitJob(earlyWarning.getRulesId());
            doSleep();
        }
    }

    /**
     * 写入预警通知数据
     *
     * @param alarmData alarmData
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void writeAlarmData(GrassEarlyWarningAlarmData alarmData) {
        Long rulesId = EarlyWarningUtils.jobIdToRulesId(alarmData.getJobId());
        Date alarmTime = alarmData.getAlarmTime();
        if (alarmTime == null) {
            throw new ServiceException("预警时间为空。");
        }

        // 查询预警规则
        GrassEarlyWarning earlyWarning = grassEarlyWarningMapper.selectGrassEarlyWarningById(rulesId);
        if (earlyWarning == null) {
            return;
        }

        // 在生效时间之外，不通知
        String startTime = earlyWarning.getStartTime();
        String endTime = earlyWarning.getEndTime();
        String alarmTimeHms = DateUtils.parseDateToStr(DateUtils.HH_MM_SS, alarmTime);
        if (!DateUtils.compareHhMmSs(startTime, endTime, alarmTimeHms)) {
            log.info("预警通知发生在生效时间之外。rulesId={}, startTime={}, endTime={}, alarmTimeHms={}",
                    rulesId, startTime, endTime, alarmTimeHms);
            return;
        }

        // 在沉默周期之内，不通知
        Integer silentCycle = earlyWarning.getSilentCycle();
        if (isInSilentCycle(rulesId, silentCycle)) {
            log.info("预警通知发生在沉默周期之内。rulesId={}, silentCycle={}", rulesId, silentCycle);
            return;
        }

        // 查询处置建议
        Long suggestId = Long.parseLong(earlyWarning.getSuggestId());
        GrassEarlyWarningSuggest suggestInfo =
                grassEarlyWarningSuggestMapper.selectGrassEarlyWarningSuggestById(suggestId);
        if (suggestInfo == null) {
            log.info("处置建议ID不存在。suggestId={}", suggestId);
            return;
        }

        // 发送预警通知
        queryUserAndSend(earlyWarning, suggestInfo);

        // 写入历史数据
        writeEarlyWarningHis(alarmData, earlyWarning, suggestInfo);
    }

    /**
     * 检查设备是否正在被使用
     *
     * @param deviceId deviceId
     */
    @Override
    public void checkDeviceCanDelete(String deviceId) {
        // 运行设备
        GrassEarlyWarningCarrier selectCarrier = new GrassEarlyWarningCarrier();
        selectCarrier.setDeviceId(deviceId);
        List<GrassEarlyWarningCarrier> carrierList =
                grassEarlyWarningCarrierMapper.selectGrassEarlyWarningCarrierList(selectCarrier);
        if (carrierList.size() > 0) {
            throw new ServiceException("预警规则的运行设备正在使用该设备，无法删除。");
        }

        // 触发条件
        GrassEarlyWarningTrigger selectTrigger = new GrassEarlyWarningTrigger();
        selectTrigger.setDeviceId(deviceId);
        List<GrassEarlyWarningTrigger> triggerList =
                grassEarlyWarningTriggerMapper.selectGrassEarlyWarningTriggerList(selectTrigger);
        if (triggerList.size() > 0) {
            throw new ServiceException("预警规则的触发条件正在使用该设备，无法删除。");
        }

        // 判断条件
        GrassEarlyWarningJudge selectJudge = new GrassEarlyWarningJudge();
        selectJudge.setDeviceId(deviceId);
        List<GrassEarlyWarningJudge> judgeList =
                grassEarlyWarningJudgeMapper.selectGrassEarlyWarningJudgeList(selectJudge);
        if (judgeList.size() > 0) {
            throw new ServiceException("预警规则的判断条件正在使用该设备，无法删除。");
        }

        // 结束条件
        GrassEarlyWarningFinish selectFinish = new GrassEarlyWarningFinish();
        selectFinish.setDeviceId(deviceId);
        List<GrassEarlyWarningFinish> finishList =
                grassEarlyWarningFinishMapper.selectGrassEarlyWarningFinishList(selectFinish);
        if (finishList.size() > 0) {
            throw new ServiceException("预警规则的结束条件正在使用该设备，无法删除。");
        }
    }

    /**
     * 刷新MQTT订阅
     */
    @Override
    public void refreshMqttListener() {
        // 查询规则运行设备
        GrassEarlyWarningCarrier select = new GrassEarlyWarningCarrier();
        select.setRulesState(EarlyConstants.RULE_STATE_OPEN);
        List<GrassEarlyWarningCarrier> list =
                grassEarlyWarningCarrierMapper.selectGrassEarlyWarningCarrierList(select);
        if (list == null || list.size() <= 0) {
            return;
        }

        // 获取Topic集合
        List<String> newTopicList = new ArrayList<>();
        for (GrassEarlyWarningCarrier carrier : list) {
            String deviceId = carrier.getDeviceId();
            newTopicList.add(earyWarningMqtt.getTopic(deviceId, MqttEnums.USAGE.JOB_DETAIL.getValue()));
            newTopicList.add(earyWarningMqtt.getTopic(deviceId, MqttEnums.USAGE.JOB_NOTICE.getValue()));
            newTopicList.add(earyWarningMqtt.getTopic(deviceId, MqttEnums.USAGE.ALARM_DATA.getValue()));
        }

        // 新增的Topic
        List<String> addList =
                newTopicList.stream().filter(item -> !oldTopicList.contains(item)).collect(toList());

        // 删除的Topic
        List<String> delList =
                oldTopicList.stream().filter(item -> !newTopicList.contains(item)).collect(toList());

        // 是否发生变化
        if (addList.size() <= 0 && delList.size() <= 0) {
            return;
        }

        // 取消现有订阅
        if (oldTopicList.size() > 0) {
            earyWarningMqtt.unsubscribe(oldTopicList.toArray(new String[0]));
            oldTopicList.clear();
        }

        // 创建新的订阅
        if (newTopicList.size() > 0) {
            earyWarningMqtt.subscribe(newTopicList.toArray(new String[0]));
            oldTopicList.addAll(newTopicList);
        }
    }

    /**
     * 检查插入参数
     *
     * @param grassEarlyWarning grassEarlyWarning
     */
    private void checkInsertParam(GrassEarlyWarning grassEarlyWarning) {
        if (StringUtils.isEmpty(grassEarlyWarning.getRulesName())) {
            throw new ServiceException("请填写规则名称");
        }
        // 触发条件
        List<GrassEarlyWarningTrigger> triggerList = grassEarlyWarning.getTriggerList();
        // 判断条件
        List<GrassEarlyWarningJudge> judgeList = grassEarlyWarning.getJudgeList();
        // 结束条件
        List<GrassEarlyWarningFinish> finishList = grassEarlyWarning.getFinishList();
        // 运行设备
        List<GrassEarlyWarningCarrier> carrierList = grassEarlyWarning.getCarrierList();

        if (triggerList == null || triggerList.size() <= 0) {
            throw new ServiceException("请至少填写1个触发条件");
        }
        if (judgeList == null || judgeList.size() <= 0) {
            throw new ServiceException("请至少填写1个判断条件");
        }
        if (carrierList == null || carrierList.size() <= 0) {
            throw new ServiceException("请至少选择1个运行设备");
        }
        if (StringUtils.isEmpty(grassEarlyWarning.getJudgeType())) {
            throw new ServiceException("请选择判断条件类型");
        }

        if (triggerList.size() > 0) {
            if (grassEarlyWarning.getTriggerFrequency() <= 0) {
                throw new ServiceException("触发条件执行频率必须大于0");
            }
        }
        if (judgeList.size() > 0) {
            if (grassEarlyWarning.getJudgeFrequency() <= 0) {
                throw new ServiceException("判断条件执行频率必须大于0");
            }
        }
        if (finishList.size() > 0) {
            if (grassEarlyWarning.getFinishFrequency() <= 0) {
                throw new ServiceException("结束条件执行频率必须大于0");
            }
        }

        for (GrassEarlyWarningTrigger trigger : triggerList) {
            String index = trigger.getTriggerIndex();
            if (StringUtils.isEmpty(index)) {
                throw new ServiceException("请输入触发条件编号。");
            }
            if (EarlyWarningUtils.checkIndexPrefix(index)) {
                throw new ServiceException("编号必须以英文字母开头。编号:" + index);
            }
            if (StringUtils.isEmpty(trigger.getDeviceId())) {
                throw new ServiceException("请选择触发条件的设备。编号:" + index);
            }
            if (StringUtils.isEmpty(trigger.getPointId())) {
                throw new ServiceException("请选择触发条件的测点。编号:" + index);
            }
            if (StringUtils.isEmpty(trigger.getJudge())) {
                throw new ServiceException("请选择触发条件的判断符号。编号:" + index);
            }
            if (trigger.getPointValue() == null) {
                throw new ServiceException("请填写触发条件的触发值。编号:" + index);
            }
        }

        for (GrassEarlyWarningJudge judge : judgeList) {
            String index = judge.getJudgeIndex();
            if (StringUtils.isEmpty(index)) {
                throw new ServiceException("请输入判断条件编号。");
            }
            if (EarlyWarningUtils.checkIndexPrefix(index)) {
                throw new ServiceException("编号必须以英文字母开头。编号:" + index);
            }
            if (StringUtils.isEmpty(judge.getDeviceId())) {
                throw new ServiceException("请选择判断条件的设备。编号:" + index);
            }
            if (StringUtils.isEmpty(judge.getPointId())) {
                throw new ServiceException("请选择判断条件的测点。编号:" + index);
            }
            if (StringUtils.isEmpty(judge.getJudge())) {
                throw new ServiceException("请选择判断条件的判断符号。编号:" + index);
            }
            if (judge.getPointValue() == null) {
                throw new ServiceException("请填写判断条件的触发值。编号:" + index);
            }
        }

        for (GrassEarlyWarningFinish finish : finishList) {
            String index = finish.getFinishIndex();
            if (StringUtils.isEmpty(index)) {
                throw new ServiceException("请输入结束条件编号。");
            }
            if (EarlyWarningUtils.checkIndexPrefix(index)) {
                throw new ServiceException("编号必须以英文字母开头。编号:" + index);
            }
            if (StringUtils.isEmpty(finish.getDeviceId())) {
                throw new ServiceException("请选择结束条件的设备。编号:" + index);
            }
            if (StringUtils.isEmpty(finish.getPointId())) {
                throw new ServiceException("请选择结束条件的测点。编号:" + index);
            }
            if (StringUtils.isEmpty(finish.getJudge())) {
                throw new ServiceException("请选择结束条件的判断符号。编号:" + index);
            }
            if (finish.getPointValue() == null) {
                throw new ServiceException("请填写结束条件的触发值。编号:" + index);
            }
        }

        // 触发条件为累加时
        if (EarlyConstants.JUDGE_TYPE_ACC.equals(grassEarlyWarning.getJudgeType())) {
            if (grassEarlyWarning.getAddUpCount() == null || grassEarlyWarning.getAddUpCount() <= 0) {
                throw new ServiceException("触发条件为累加时，累加次数必须大于0");
            }
            if (grassEarlyWarning.getAddUpDuration() == null || grassEarlyWarning.getAddUpDuration() <= 0) {
                throw new ServiceException("触发条件为累加时，总持续时长必须大于0");
            }
            if (grassEarlyWarning.getAddUpOnceMax() == null || grassEarlyWarning.getAddUpOnceMax() <= 0) {
                throw new ServiceException("触发条件为累加时，单次最大时长必须大于0");
            }
        }

        // 触发条件为连续时
        if (EarlyConstants.JUDGE_TYPE_SCC.equals(grassEarlyWarning.getJudgeType())) {
            if (grassEarlyWarning.getContinuousCount() == null || grassEarlyWarning.getContinuousCount() <= 0) {
                throw new ServiceException("触发条件为连续时，连续次数必须大于0");
            }
        }

        if (grassEarlyWarning.getNoticeTempIdList() == null || grassEarlyWarning.getNoticeTempIdList().size() <= 0) {
            throw new ServiceException("请选择通知模板");
        }

        if (StringUtils.isEmpty(grassEarlyWarning.getRulesLevel())) {
            throw new ServiceException("请选择预警级别");
        }

        if (StringUtils.isEmpty(grassEarlyWarning.getStartTime())) {
            throw new ServiceException("请选择生效开始时间");
        }

        if (StringUtils.isEmpty(grassEarlyWarning.getEndTime())) {
            throw new ServiceException("请选择生效结束时间");
        }

        if (StringUtils.isEmpty(grassEarlyWarning.getSuggestId())) {
            throw new ServiceException("请选择处置建议");
        }

        // 检查触发条件关系
        checkTriggerRelation(grassEarlyWarning.getTriggerRelation(), triggerList);

        // 检查执行条件关系
        checkJudgeRelation(grassEarlyWarning.getJudgeRelation(), judgeList);

        // 检查结束条件关系
        checkFinishRelation(grassEarlyWarning.getFinishRelation(), finishList);
    }

    /**
     * 检查触发条件关系表达式是否合法
     *
     * @param relation relation
     * @param triggerList triggerList
     */
    private void checkTriggerRelation(String relation, List<GrassEarlyWarningTrigger> triggerList) {
        // 是否指定了关系表达式
        if (StringUtils.isEmpty(relation)) {
            return;
        }

        // 只有一个条件时
        if (triggerList.size() == 1 && StringUtils.isNotEmpty(relation)) {
            throw new ServiceException("有且仅有一个触发条件时，请勿指定触发条件关系。");
        }

        // 编号数组
        List<String> triggerIndex = new ArrayList<>();
        triggerList.forEach(data -> triggerIndex.add(data.getTriggerIndex()));

        // 关系中指定的编号数组
        List<String> relationIndex = EarlyWarningUtils.getIndexList(relation);

        // 检查所有编号都被使用
        List<String> diff1 = new ArrayList<>();
        triggerIndex.forEach(index -> {
            if (!relationIndex.contains(index)) {
                diff1.add(index);
            }
        });
        if (diff1.size() > 0) {
            String join = StringUtils.join(diff1, ",");
            throw new ServiceException("您指定的触发条件关系表达式中未使用这些编号：" + join);
        }

        // 检查是否使用了不存在的编号
        List<String> diff2 = new ArrayList<>();
        relationIndex.forEach(index -> {
            if (!triggerIndex.contains(index)) {
                diff2.add(index);
            }
        });
        if (diff2.size() > 0) {
            String join = StringUtils.join(diff2, ",");
            throw new ServiceException("您指定的触发条件关系表达式中使用了不存在的编号：" + join);
        }
    }

    /**
     * 检查判断条件关系表达式是否合法
     *
     * @param relation relation
     * @param judgeList judgeList
     */
    private void checkJudgeRelation(String relation, List<GrassEarlyWarningJudge> judgeList) {
        // 是否指定了关系表达式
        if (StringUtils.isEmpty(relation)) {
            return;
        }

        // 只有一个条件时
        if (judgeList.size() == 1 && StringUtils.isNotEmpty(relation)) {
            throw new ServiceException("有且仅有一个判断条件时，请勿指定判断条件关系。");
        }

        // 编号数组
        List<String> judgeIndex = new ArrayList<>();
        judgeList.forEach(data -> judgeIndex.add(data.getJudgeIndex()));

        // 关系中指定的编号数组
        List<String> relationIndex = EarlyWarningUtils.getIndexList(relation);

        // 检查所有编号都被使用
        List<String> diff1 = new ArrayList<>();
        judgeIndex.forEach(index -> {
            if (!relationIndex.contains(index)) {
                diff1.add(index);
            }
        });
        if (diff1.size() > 0) {
            String join = StringUtils.join(diff1, ",");
            throw new ServiceException("您指定的判断条件关系表达式中未使用这些编号：" + join);
        }

        // 检查是否使用了不存在的编号
        List<String> diff2 = new ArrayList<>();
        relationIndex.forEach(index -> {
            if (!judgeIndex.contains(index)) {
                diff2.add(index);
            }
        });
        if (diff2.size() > 0) {
            String join = StringUtils.join(diff2, ",");
            throw new ServiceException("您指定的判断条件关系表达式中使用了不存在的编号：" + join);
        }
    }

    /**
     * 检查结束条件关系表达式是否合法
     *
     * @param relation relation
     * @param finishList finishList
     */
    private void checkFinishRelation(String relation, List<GrassEarlyWarningFinish> finishList) {
        // 是否指定了关系表达式
        if (StringUtils.isEmpty(relation)) {
            return;
        }

        // 只有一个条件时
        if (finishList.size() == 1 && StringUtils.isNotEmpty(relation)) {
            throw new ServiceException("有且仅有一个结束条件时，请勿指定结束条件关系。");
        }

        // 编号数组
        List<String> finishIndex = new ArrayList<>();
        finishList.forEach(data -> finishIndex.add(data.getFinishIndex()));

        // 关系中指定的编号数组
        List<String> relationIndex = EarlyWarningUtils.getIndexList(relation);

        // 检查所有编号都被使用
        List<String> diff1 = new ArrayList<>();
        finishIndex.forEach(index -> {
            if (!relationIndex.contains(index)) {
                diff1.add(index);
            }
        });
        if (diff1.size() > 0) {
            String join = StringUtils.join(diff1, ",");
            throw new ServiceException("您指定的结束条件关系表达式中未使用这些编号：" + join);
        }

        // 检查是否使用了不存在的编号
        List<String> diff2 = new ArrayList<>();
        relationIndex.forEach(index -> {
            if (!finishIndex.contains(index)) {
                diff2.add(index);
            }
        });
        if (diff2.size() > 0) {
            String join = StringUtils.join(diff2, ",");
            throw new ServiceException("您指定的结束条件关系表达式中使用了不存在的编号：" + join);
        }
    }

    /**
     * 初始化预警规则信息
     *
     * @param grassEarlyWarning grassEarlyWarning
     */
    private void initEarlyWarning(GrassEarlyWarning grassEarlyWarning) {
        if (grassEarlyWarning.getTriggerFrequency() == null) {
            grassEarlyWarning.setTriggerFrequency(0L);
        }
        if (grassEarlyWarning.getJudgeFrequency() == null) {
            grassEarlyWarning.setJudgeFrequency(0L);
        }
        if (grassEarlyWarning.getFinishFrequency() == null) {
            grassEarlyWarning.setFinishFrequency(0L);
        }
        if (grassEarlyWarning.getAddUpCount() == null) {
            grassEarlyWarning.setAddUpCount(0);
        }
        if (grassEarlyWarning.getAddUpDuration() == null) {
            grassEarlyWarning.setAddUpDuration(0L);
        }
        if (grassEarlyWarning.getAddUpOnceMax() == null) {
            grassEarlyWarning.setAddUpOnceMax(0L);
        }
        if (grassEarlyWarning.getContinuousCount() == null) {
            grassEarlyWarning.setContinuousCount(0);
        }
        if (grassEarlyWarning.getContinuousIncrease() == null) {
            grassEarlyWarning.setContinuousIncrease(0D);
        }

        List<GrassEarlyWarningTrigger> triggerList = grassEarlyWarning.getTriggerList();
        if (triggerList != null) {
            triggerList.removeIf(data -> StringUtils.isEmpty(data.getTriggerIndex()));
        }

        List<GrassEarlyWarningJudge> judgeList = grassEarlyWarning.getJudgeList();
        if (judgeList != null) {
            judgeList.removeIf(data -> StringUtils.isEmpty(data.getJudgeIndex()));
        }

        List<GrassEarlyWarningFinish> finishList = grassEarlyWarning.getFinishList();
        if (finishList != null) {
            finishList.removeIf(data -> StringUtils.isEmpty(data.getFinishIndex()));
        }

        if (EarlyConstants.JUDGE_TYPE_SCC.equals(grassEarlyWarning.getJudgeType())) {
            grassEarlyWarning.setAddUpCount(0);
            grassEarlyWarning.setAddUpDuration(0L);
            grassEarlyWarning.setAddUpOnceMax(0L);
        }

        if (EarlyConstants.JUDGE_TYPE_ACC.equals(grassEarlyWarning.getJudgeType())) {
            grassEarlyWarning.setContinuousCount(0);
            grassEarlyWarning.setContinuousIncrease(0D);
        }

        grassEarlyWarning.setTenantId(SecurityUtils.getTenantId());
        grassEarlyWarning.setUpdateBy(SecurityUtils.getUsername());
        grassEarlyWarning.setUpdateTime(new Date());
    }

    /**
     * 检查并开始沉默周期
     *
     * @param rulesId rulesId
     * @param silentCycle silentCycle
     * @return true：在沉默周期之内、false：不在沉默周期之内
     */
    private boolean isInSilentCycle(Long rulesId, Integer silentCycle) {
        // 未设置沉默周期
        if (silentCycle <= 0) {
            return false;
        }

        // 在沉默周期之内
        String cacheKey = EarlyConstants.REDIS_PREFIX + rulesId;
        if (redisCache.hasKey(cacheKey)) {
            return true;
        }

        // 开始沉默周期
        redisCache.setCacheObject(cacheKey, cacheKey, silentCycle, TimeUnit.SECONDS);
        return false;
    }

    /**
     * 创建判断条件
     *
     * @param rulesId rulesId
     * @param triggerList triggerList
     */
    private void createTriggerList(Long rulesId, List<GrassEarlyWarningTrigger> triggerList) {
        if (triggerList == null || triggerList.size() <= 0) {
            return ;
        }
        triggerList.forEach(data -> {
            data.setRulesId(rulesId);
            data.setTenantId(SecurityUtils.getTenantId());
            data.setCreateBy(SecurityUtils.getUsername());
            data.setCreateTime(new Date());
            data.setUpdateBy(SecurityUtils.getUsername());
            data.setUpdateTime(new Date());
        });
        grassEarlyWarningTriggerMapper.batchInsertGrassEarlyWarningTrigger(triggerList);
    }

    /**
     * 创建判断条件
     *
     * @param rulesId rulesId
     * @param judgeList judgeList
     */
    private void createJudgeList(Long rulesId, List<GrassEarlyWarningJudge> judgeList) {
        if (judgeList == null || judgeList.size() <= 0) {
            return ;
        }
        judgeList.forEach(data -> {
            data.setRulesId(rulesId);
            data.setTenantId(SecurityUtils.getTenantId());
            data.setCreateBy(SecurityUtils.getUsername());
            data.setCreateTime(new Date());
            data.setUpdateBy(SecurityUtils.getUsername());
            data.setUpdateTime(new Date());
        });
        grassEarlyWarningJudgeMapper.batchInsertGrassEarlyWarningJudge(judgeList);
    }

    /**
     * 创建结束条件
     *
     * @param rulesId rulesId
     * @param finishList finishList
     */
    private void createFinishList(Long rulesId, List<GrassEarlyWarningFinish> finishList) {
        if (finishList == null || finishList.size() <= 0) {
            return ;
        }
        finishList.forEach(data -> {
            data.setRulesId(rulesId);
            data.setTenantId(SecurityUtils.getTenantId());
            data.setCreateBy(SecurityUtils.getUsername());
            data.setCreateTime(new Date());
            data.setUpdateBy(SecurityUtils.getUsername());
            data.setUpdateTime(new Date());
        });
        grassEarlyWarningFinishMapper.batchInsertGrassEarlyWarningFinish(finishList);
    }

    /**
     * 创建运行设备
     *
     * @param rulesId rulesId
     * @param carrierList carrierList
     */
    private void createCarrierList(Long rulesId, List<GrassEarlyWarningCarrier> carrierList) {
        if (carrierList == null || carrierList.size() <= 0) {
            return;
        }
        carrierList.forEach(data -> data.setRulesId(rulesId));
        grassEarlyWarningCarrierMapper.batchInsertGrassEarlyWarningCarrier(carrierList);
    }

    /**
     * 创建通知范围
     *
     * @param rulesId rulesId
     * @param noticeTempIdList noticeTempIdList
     */
    private void createNoticeList(Long rulesId, List<Long> noticeTempIdList) {
        if (noticeTempIdList == null || noticeTempIdList.size() <= 0) {
            return;
        }
        List<GrassEarlyWarningNotice> noticeList = new ArrayList<>();
        noticeTempIdList.forEach(tempId -> {
            GrassEarlyWarningNotice notice = new GrassEarlyWarningNotice();
            notice.setRulesId(rulesId);
            notice.setNoticeTemplateId(tempId);
            notice.setTenantId(SecurityUtils.getTenantId());
            notice.setCreateBy(SecurityUtils.getUsername());
            notice.setCreateTime(new Date());
            notice.setUpdateBy(SecurityUtils.getUsername());
            notice.setUpdateTime(new Date());
            noticeList.add(notice);
        });
        if (noticeList.size() > 0) {
            grassEarlyWarningNoticeMapper.batchInsertGrassEarlyWarningNotice(noticeList);
        }
    }

    /**
     * 执行预警通知
     *
     * @param earlyWarning earlyWarning
     * @param suggestInfo suggestInfo
     */
    private void queryUserAndSend(GrassEarlyWarning earlyWarning, GrassEarlyWarningSuggest suggestInfo) {
        // 查询通知对象
        List<RuleNoticeVO> noticeUserList =
                grassEarlyWarningNoticeMapper.selectEarlyWarningNoticeUserList(earlyWarning.getRulesId());

        // 获取通知内容
        String noticeContent = makeNoticeContent(earlyWarning, suggestInfo);

        // 发送预警通知
        for (RuleNoticeVO vo : noticeUserList) {
            sendMsgAsync.sendMsgAsync(vo.getNoticeModeInfoList(), vo.getUserList(), noticeContent);
        }
    }

    /**
     * 查询设备信息列表
     *
     * @param deviceId deviceId
     * @return 结果
     */
    private List<GrassDeviceInfo> getDeviceInfoList(String deviceId) {
        GrassDeviceInfo select = new GrassDeviceInfo();
        select.setDeviceId(deviceId);
        return grassDeviceInfoMapper.selectGrassDeviceInfoList(select);
    }

    /**
     * 查询测点信息列表
     *
     * @param deviceId deviceId
     * @return 结果
     */
    private List<GrassPointInfo> getPointInfoList(String deviceId) {
        GrassPointInfo select = new GrassPointInfo();
        select.setDeviceId(deviceId);
        return grassPointInfoMapper.selectGrassPointInfoList(select);
    }

    /**
     * 创建规则引擎任务
     *
     * @param rulesId rulesId
     */
    private void submitJob(Long rulesId) {
        // 查询预警规则
        GrassEarlyWarning earlyWarning = grassEarlyWarningMapper.selectGrassEarlyWarningById(rulesId);
        if (earlyWarning == null) {
            return;
        }

        // 查询触发条件
        List<GrassEarlyWarningTrigger> triggerList =
                grassEarlyWarningTriggerMapper.selectGrassEarlyWarningTriggerByRulesId(rulesId);

        // 查询执行条件
        List<GrassEarlyWarningJudge> judgeList =
                grassEarlyWarningJudgeMapper.selectGrassEarlyWarningJudgeByRulesId(rulesId);

        // 查询运行设备
        List<GrassEarlyWarningCarrier> carrierList =
                grassEarlyWarningCarrierMapper.selectGrassEarlyWarningCarrierListByRulesId(rulesId);

        // 获取事件类型
        String usage;
        if (EarlyConstants.JUDGE_TYPE_SCC.equals(earlyWarning.getJudgeType())) {
            usage = MqttEnums.USAGE.SUBMIT_JOB_SCC.getValue();
        } else {
            usage = MqttEnums.USAGE.SUBMIT_JOB_ACC.getValue();
        }

        // 获取发送JSON
        JSONObject jobJson;
        if (EarlyConstants.JUDGE_TYPE_SCC.equals(earlyWarning.getJudgeType())) {
            jobJson = EarlyWarningUtils.makeSendDownJsonScc(earlyWarning, triggerList, judgeList);
        } else {
            jobJson = EarlyWarningUtils.makeSendDownJsonAcc(earlyWarning, triggerList, judgeList);
        }

        // 执行消息通知
        carrierList.forEach(data -> {
            String topic = earyWarningMqtt.getTopic(data.getDeviceId(), usage);
            earyWarningMqtt.publish(topic, jobJson.toJSONString());
        });
    }

    /**
     * 删除规则引擎任务
     *
     * @param rulesId rulesId
     */
    private void deleteJob(Long rulesId) {
        // 查询预警规则
        GrassEarlyWarning earlyWarning = grassEarlyWarningMapper.selectGrassEarlyWarningById(rulesId);
        if (earlyWarning == null) {
            return;
        }

        // 查询运行设备
        List<GrassEarlyWarningCarrier> carrierList =
                grassEarlyWarningCarrierMapper.selectGrassEarlyWarningCarrierListByRulesId(rulesId);

        // 获取任务ID
        String tenantId = earlyWarning.getTenantId();
        String jobId = EarlyWarningUtils.rulesIdToJobId(rulesId, tenantId);

        // 获取事件类型
        String usage = MqttEnums.USAGE.DELETE_JOB.getValue();

        // 生成任务数据
        JSONObject dataJson = new JSONObject();
        dataJson.put(EarlyConstants.DELETE_JOB_ID, jobId);
        JSONObject sendJson = new JSONObject();
        sendJson.put(EarlyConstants.TIME, System.currentTimeMillis());
        sendJson.put(EarlyConstants.EVENT, usage);
        sendJson.put(EarlyConstants.DATA, dataJson);

        // 执行消息通知
        carrierList.forEach(data -> {
            String topic = earyWarningMqtt.getTopic(data.getDeviceId(), usage);
            earyWarningMqtt.publish(topic, sendJson.toJSONString());
        });

        // 删除任务状态
        redisCache.delete(jobId);
    }

    /**
     * 获取任务状态
     *
     * @param rulesId rulesId
     * @param tenantId tenantId
     */
    private String getJobStatus(Long rulesId, String tenantId) {
        // 获取任务ID
        String jobId = EarlyWarningUtils.rulesIdToJobId(rulesId, tenantId);
        if (!redisCache.hasKey(jobId)) {
            return "";
        }

        // 任务状态
        String status = redisCache.getCacheObject(jobId);

        // 转换名称
        return EarlyWarningUtils.toStatusName(status);
    }

    /**
     * 获取通知内容
     *
     * @param earlyWarning earlyWarning
     * @param suggestInfo suggestInfo
     * @return 结果
     */
    private String makeNoticeContent(GrassEarlyWarning earlyWarning, GrassEarlyWarningSuggest suggestInfo) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("rulesName", earlyWarning.getRulesName());
        jsonObject.put("suggestTitle", suggestInfo.getSuggestTitle());
        return jsonObject.toJSONString();
    }

    /**
     * 是否需要更新引擎任务
     *
     * @param newEarlyWarning newEarlyWarning
     * @return 结果
     */
    private boolean needUpdateJob(GrassEarlyWarning newEarlyWarning) {
        Long rulesId = newEarlyWarning.getRulesId();

        // 查询预警规则
        GrassEarlyWarning oldEarlyWarning = grassEarlyWarningMapper.selectGrassEarlyWarningById(rulesId);
        if (EarlyConstants.RULE_STATE_CLOSE.equals(oldEarlyWarning.getRulesState())) {
            return false;
        }

        // 检查规则参数
        if (!oldEarlyWarning.getTriggerRelation().equals(newEarlyWarning.getTriggerRelation())
                || !oldEarlyWarning.getJudgeRelation().equals(newEarlyWarning.getJudgeRelation())
                || !oldEarlyWarning.getFinishRelation().equals(newEarlyWarning.getFinishRelation())
                || !oldEarlyWarning.getTriggerFrequency().equals(newEarlyWarning.getTriggerFrequency())
                || !oldEarlyWarning.getJudgeFrequency().equals(newEarlyWarning.getJudgeFrequency())
                || !oldEarlyWarning.getFinishFrequency().equals(newEarlyWarning.getFinishFrequency())
                || !oldEarlyWarning.getJudgeType().equals(newEarlyWarning.getJudgeType())
                || !oldEarlyWarning.getAddUpCount().equals(newEarlyWarning.getAddUpCount())
                || !oldEarlyWarning.getAddUpOnceMax().equals(newEarlyWarning.getAddUpOnceMax())
                || !oldEarlyWarning.getAddUpDuration().equals(newEarlyWarning.getAddUpDuration())
                || !oldEarlyWarning.getContinuousCount().equals(newEarlyWarning.getContinuousCount())
                || !oldEarlyWarning.getContinuousIncrease().equals(newEarlyWarning.getContinuousIncrease())) {
            return true;
        }

        // 查询运行设备
        List<GrassEarlyWarningCarrier> oldCarrierList =
                grassEarlyWarningCarrierMapper.selectGrassEarlyWarningCarrierListByRulesId(rulesId);

        // 检查运行设备
        List<GrassEarlyWarningCarrier> newCarrierList = newEarlyWarning.getCarrierList();
        if (newCarrierList.size() != oldCarrierList.size()) {
            return true;
        }
        for (GrassEarlyWarningCarrier newCarrier : newCarrierList) {
            if (checkCarrierUpdate(newCarrier, oldCarrierList)) {
                return true;
            }
        }

        // 查询触发条件
        List<GrassEarlyWarningTrigger> oldTriggerList =
                grassEarlyWarningTriggerMapper.selectGrassEarlyWarningTriggerByRulesId(rulesId);

        // 检查触发条件
        List<GrassEarlyWarningTrigger> newTriggerList = newEarlyWarning.getTriggerList();
        if (newTriggerList.size() != oldTriggerList.size()) {
            return true;
        }
        for (GrassEarlyWarningTrigger newTrigger : newTriggerList) {
            if (checkTriggerUpdate(newTrigger, oldTriggerList)) {
                return true;
            }
        }

        // 查询判断条件
        List<GrassEarlyWarningJudge> oldJudgeList =
                grassEarlyWarningJudgeMapper.selectGrassEarlyWarningJudgeByRulesId(rulesId);

        // 检查判断条件
        List<GrassEarlyWarningJudge> newJudgeList = newEarlyWarning.getJudgeList();
        if (newJudgeList.size() != oldJudgeList.size()) {
            return true;
        }
        for (GrassEarlyWarningJudge newJudge : newJudgeList) {
            if (checkJudgeUpdate(newJudge, oldJudgeList)) {
                return true;
            }
        }

        // 查询结束条件
        List<GrassEarlyWarningFinish> oldFinishList =
                grassEarlyWarningFinishMapper.selectGrassEarlyWarningFinishByRulesId(rulesId);

        // 检查结束条件
        List<GrassEarlyWarningFinish> newFinishList = newEarlyWarning.getFinishList();
        if (newFinishList.size() != oldFinishList.size()) {
            return true;
        }
        for (GrassEarlyWarningFinish newFinish : newFinishList) {
            if (checkFinishUpdate(newFinish, oldFinishList)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 检查触发条件是否发生变更
     *
     * @param newTrigger newTrigger
     * @param oldTriggerList oldTriggerList
     * @return 结果
     */
    private boolean checkTriggerUpdate(
            GrassEarlyWarningTrigger newTrigger, List<GrassEarlyWarningTrigger> oldTriggerList) {
        // 查找触发条件
        GrassEarlyWarningTrigger oldTrigger = null;
        for (GrassEarlyWarningTrigger trigger : oldTriggerList) {
            if (trigger.getTriggerIndex().equals(newTrigger.getTriggerIndex())) {
                oldTrigger = trigger;
                break;
            }
        }

        // 无旧触发条件
        if (oldTrigger == null) {
            return true;
        }

        // 是否发生变更
        return !newTrigger.getDeviceId().equals(oldTrigger.getDeviceId())
                || !newTrigger.getPointId().equals(oldTrigger.getPointId())
                || !newTrigger.getJudge().equals(oldTrigger.getJudge())
                || !newTrigger.getPointValue().equals(oldTrigger.getPointValue());
    }

    /**
     * 检查判断条件是否发生变更
     *
     * @param newJudge newJudge
     * @param oldJudgeList oldJudgeList
     * @return 结果
     */
    private boolean checkJudgeUpdate(
            GrassEarlyWarningJudge newJudge, List<GrassEarlyWarningJudge> oldJudgeList) {
        // 查找触发条件
        GrassEarlyWarningJudge oldJudge = null;
        for (GrassEarlyWarningJudge judge : oldJudgeList) {
            if (judge.getJudgeIndex().equals(newJudge.getJudgeIndex())) {
                oldJudge = judge;
                break;
            }
        }

        // 无旧判断条件
        if (oldJudge == null) {
            return true;
        }

        // 是否发生变更
        return !newJudge.getDeviceId().equals(oldJudge.getDeviceId())
                || !newJudge.getPointId().equals(oldJudge.getPointId())
                || !newJudge.getJudge().equals(oldJudge.getJudge())
                || !newJudge.getPointValue().equals(oldJudge.getPointValue());
    }

    /**
     * 检查结束条件是否发生变更
     *
     * @param newFinish newFinish
     * @param oldFinishList oldFinishList
     * @return 结果
     */
    private boolean checkFinishUpdate(
            GrassEarlyWarningFinish newFinish, List<GrassEarlyWarningFinish> oldFinishList) {
        // 查找触发条件
        GrassEarlyWarningFinish oldFinish = null;
        for (GrassEarlyWarningFinish finish : oldFinishList) {
            if (finish.getFinishIndex().equals(newFinish.getFinishIndex())) {
                oldFinish = finish;
                break;
            }
        }

        // 无旧结束条件
        if (oldFinish == null) {
            return true;
        }

        // 是否发生变更
        return !newFinish.getDeviceId().equals(oldFinish.getDeviceId())
                || !newFinish.getPointId().equals(oldFinish.getPointId())
                || !newFinish.getJudge().equals(oldFinish.getJudge())
                || !newFinish.getPointValue().equals(oldFinish.getPointValue());
    }

    /**
     * 检查运行设备是否发生变更
     *
     * @param newCarrier newCarrier
     * @param oldCarrierList oldCarrierList
     * @return 结果
     */
    private boolean checkCarrierUpdate(
            GrassEarlyWarningCarrier newCarrier, List<GrassEarlyWarningCarrier> oldCarrierList) {
        for (GrassEarlyWarningCarrier carrier : oldCarrierList) {
            if (carrier.getDeviceId().equals(newCarrier.getDeviceId())) {
                return false;
            }
        }
        return true;
    }

    /**
     * 暂停指定毫秒
     */
    private void doSleep() {
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建报警历史数据
     *
     * @param alarmData alarmData
     * @param earlyWarning earlyWarning
     * @param suggest suggest
     */
    private void writeEarlyWarningHis(
            GrassEarlyWarningAlarmData alarmData, GrassEarlyWarning earlyWarning, GrassEarlyWarningSuggest suggest) {
        Long rulesId = earlyWarning.getRulesId();
        log.info("插入预警通知历史。{}", alarmData);

        // 预警通知历史
        GrassEarlyWarningAlarmHis alarmHis = new GrassEarlyWarningAlarmHis();
        alarmHis.setRulesId(earlyWarning.getRulesId());
        alarmHis.setAlarmTime(alarmData.getAlarmTime());
        grassEarlyWarningAlarmHisMapper.insertGrassEarlyWarningAlarmHis(alarmHis);

        // 报警历史ID
        Long hisId = alarmHis.getHisId();

        // 预警规则历史
        GrassEarlyWarningHis warningHis = new GrassEarlyWarningHis();
        BeanUtils.copyBeanProp(warningHis, earlyWarning);
        warningHis.setHisId(hisId);
        grassEarlyWarningHisMapper.insert(warningHis);

        // 处置建议历史
        GrassEarlyWarningSuggestHis suggestHis = new GrassEarlyWarningSuggestHis();
        BeanUtils.copyBeanProp(suggestHis, suggest);
        suggestHis.setHisId(hisId);
        grassEarlyWarningSuggestHisMapper.insert(suggestHis);

        // 触发条件历史
        List<GrassEarlyWarningTrigger> triggerList =
                grassEarlyWarningTriggerMapper.selectGrassEarlyWarningTriggerByRulesId(rulesId);
        List<GrassEarlyWarningTriggerHis> triggerHisList = new ArrayList<>();
        for (GrassEarlyWarningTrigger trigger : triggerList) {
            GrassEarlyWarningTriggerHis triggerHis = new GrassEarlyWarningTriggerHis();
            BeanUtils.copyBeanProp(triggerHis, trigger);
            triggerHis.setHisId(hisId);
            triggerHisList.add(triggerHis);
        }
        if (triggerHisList.size() > 0) {
            grassEarlyWarningTriggerHisMapper.batchGrassEarlyWarningTriggerHis(triggerHisList);
        }

        // 判断条件历史
        List<GrassEarlyWarningJudge> judgeList =
                grassEarlyWarningJudgeMapper.selectGrassEarlyWarningJudgeByRulesId(rulesId);
        List<GrassEarlyWarningJudgeHis> judgeHisList = new ArrayList<>();
        for (GrassEarlyWarningJudge judge : judgeList) {
            GrassEarlyWarningJudgeHis judgeHis = new GrassEarlyWarningJudgeHis();
            BeanUtils.copyBeanProp(judgeHis, judge);
            judgeHis.setHisId(hisId);
            judgeHisList.add(judgeHis);
        }
        if (judgeHisList.size() > 0) {
            grassEarlyWarningJudgeHisMapper.batchGrassEarlyWarningJudgeHis(judgeHisList);
        }

        // 结束条件历史
        List<GrassEarlyWarningFinish> finishList =
                grassEarlyWarningFinishMapper.selectGrassEarlyWarningFinishByRulesId(rulesId);
        List<GrassEarlyWarningFinishHis> finishHisList = new ArrayList<>();
        for (GrassEarlyWarningFinish finish : finishList) {
            GrassEarlyWarningFinishHis finishHis = new GrassEarlyWarningFinishHis();
            BeanUtils.copyBeanProp(finishHis, finish);
            finishHis.setHisId(hisId);
            finishHisList.add(finishHis);
        }
        if (finishHisList.size() > 0) {
            grassEarlyWarningFinishHisMapper.batchGrassEarlyWarningFinishHis(finishHisList);
        }
    }
}
