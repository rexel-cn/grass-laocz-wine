package com.rexel.nsq.rule;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONReader;
import com.github.brainlag.nsq.NSQMessage;
import com.rexel.common.constant.Constants;
import com.rexel.common.core.redis.RedisCache;
import com.rexel.common.enums.SendContentTemplateEnum;
import com.rexel.nsq.rule.domain.dto.AlarmDTO;
import com.rexel.nsq.rule.domain.dto.SendDTO;
import com.rexel.send.factory.PushFactory;
import com.rexel.system.domain.GrassAlarmHistory;
import com.rexel.system.domain.vo.AlarmRulesDetailVo;
import com.rexel.system.domain.vo.RuleNoticeVO;
import com.rexel.system.service.IGrassAlarmHistoryService;
import com.rexel.system.service.IGrassRulesInfoService;
import com.rexel.tenant.util.TenantUtils;
import io.netty.channel.ChannelFutureListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * ClassName ids-service
 * Description
 * Author 孟开通
 * Date 2022/4/24
 **/
@Component
@Slf4j
public class NsqSendMsgAsync {
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private IGrassRulesInfoService grassRulesInfoService;
    @Autowired
    private PushFactory pushFactory;
    @Autowired
    private IGrassAlarmHistoryService grassAlarmHistoryService;

    /**
     * 消费数据
     *
     * @param message message
     */
    @Async("sendMessageExecutor")
    public void sendMsgAsync(NSQMessage message) {
        String key = new String(message.getId());
        long timeOut = 600;
        if (redisCache.tryLock(Constants.REDIS_PREFIX_NSQ_ALARM + key, key, timeOut)) {
            try {
                saveAndNotice(message.getMessage());
                finished(message, key);
            } catch (Exception e) {
                requeue(message, key);
                e.printStackTrace();
                log.error(e.getMessage());
            }
        }
    }

    /**
     * 构造通知消息体
     *
     * @param alarmDTO alarmDTO
     * @param ruleNoticeVO ruleNoticeVO
     */
    private void queryUserAndSend(AlarmDTO alarmDTO, RuleNoticeVO ruleNoticeVO) {
        SendDTO sendDTO = new SendDTO();
        sendDTO.setPointName(alarmDTO.getPointId());
        sendDTO.setRulesLevel(alarmDTO.getRulesLevel());
        sendDTO.setCurrTime(alarmDTO.getAlarmTime());
        sendDTO.setRtVal(alarmDTO.getAlarmValue());
        sendDTO.setSilentCycle(alarmDTO.getSilentCycle());
        pushFactory.startSend(ruleNoticeVO.getUserList(),
                ruleNoticeVO.getNoticeModeInfoList(), sendDTO,
                SendContentTemplateEnum.DEVICE_RULES_ALARM_CONTENT_TEMPLATE, Constants.ALARM_DEVICE);
    }

    /**
     * 消费逻辑
     *
     * @param body body
     */
    private void saveAndNotice(byte[] body) {
        log.info("Nsq报警消息体：{}", new String(body));
        AlarmDTO alarmDTO;
        try {
            alarmDTO = JSON.parseObject(body, AlarmDTO.class, JSONReader.Feature.SupportSmartMatch);
        } catch (Exception e) {
            log.error("消息格式错误{}", new String(body));
            return;
        }
        if (ObjectUtil.isNull(alarmDTO)) {
            return;
        }

        if (alarmDTO.getTenantId() == null
                || alarmDTO.getRulesId() == null
                || alarmDTO.getPointId() == null
                || alarmDTO.getRulesLevel() == null
                || alarmDTO.getAlarmTime() == null
                || alarmDTO.getAlarmValue() == null) {
            log.error("报警数据不完整{}", alarmDTO);
            return;
        }

        AlarmDTO finalAlarmDTO = alarmDTO;
        TenantUtils.execute(alarmDTO.getTenantId(), () -> {
            // 根据规则id查询通知方式和通知范围
            AlarmRulesDetailVo alarmRulesDetailVoByRulesId =
                    grassRulesInfoService.getAlarmRulesDetailVoByRulesId(finalAlarmDTO.getRulesId());
            if (ObjectUtil.isNull(alarmRulesDetailVoByRulesId)) {
                log.error("规则id找不到:{}", finalAlarmDTO.getRulesId());
                return;
            }

            List<RuleNoticeVO> ruleNoticeVOList = alarmRulesDetailVoByRulesId.getRuleNoticeVOList();
            if (CollectionUtil.isEmpty(ruleNoticeVOList)) {
                log.error("规则id：{},没有配置通知模板", finalAlarmDTO.getRulesId());
                return;
            }

            finalAlarmDTO.setRulesLevel(alarmRulesDetailVoByRulesId.getRulesLevel());
            finalAlarmDTO.setSilentCycle(alarmRulesDetailVoByRulesId.getSilentCycle());

            // 构建告警历史保存
            createAlarmHistory(finalAlarmDTO, alarmRulesDetailVoByRulesId);

            // 根据通知方式和通知范围进行消息推送
            ruleNoticeVOList.forEach(alarmRulesDetailVo -> queryUserAndSend(finalAlarmDTO, alarmRulesDetailVo));
        });
    }

    /**
     * 创建报警历史
     *
     * @param alarmDTO alarmDTO
     * @param alarmRulesDetailVoByRulesId alarmRulesDetailVoByRulesId
     */
    private void createAlarmHistory(AlarmDTO alarmDTO, AlarmRulesDetailVo alarmRulesDetailVoByRulesId) {
        GrassAlarmHistory alarmHistory = new GrassAlarmHistory();
        alarmHistory.setRulesId(Long.valueOf(alarmDTO.getRulesId()));
        alarmHistory.setRulesName(alarmRulesDetailVoByRulesId.getRulesName());
        alarmHistory.setRulesUnique(alarmRulesDetailVoByRulesId.getRulesUnique());
        alarmHistory.setDeviceId(alarmDTO.getDeviceId());
        alarmHistory.setAssetId(alarmDTO.getAssetId());
        alarmHistory.setPointId(alarmDTO.getPointId());
        alarmHistory.setPointJudge(alarmDTO.getPointJudge());
        alarmHistory.setPointValue(Double.valueOf(alarmDTO.getPointValue()));
        alarmHistory.setAlarmTime(alarmDTO.getAlarmTime());
        alarmHistory.setAlarmValue(Double.valueOf(alarmDTO.getAlarmValue()));
        alarmHistory.setRulesLevel(alarmDTO.getRulesLevel());
        alarmHistory.setRulesType(alarmRulesDetailVoByRulesId.getRulesType());
        grassAlarmHistoryService.save(alarmHistory);
    }

    /**
     * 消费成功
     *
     * @param message message
     * @param key key
     */
    private void finished(NSQMessage message, String key) {
        message.finished().addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                redisCache.delete(Constants.REDIS_PREFIX_NSQ_ALARM + key);
            }
        });
    }

    /**
     * 消费失败，重新消费
     *
     * @param message message
     * @param key key
     */
    private void requeue(NSQMessage message, String key) {
        if (message.getAttempts() <= 10) {
            message.requeue().addListener((ChannelFutureListener) future -> {
                if (future.isSuccess()) {
                    redisCache.delete(Constants.REDIS_PREFIX_NSQ_ALARM + key);
                }
            });
            return;
        }
        finished(message, key);
        log.error("消费失败" + message.getAttempts() + "次，消息体：" + new String(message.getMessage()));
    }
}
