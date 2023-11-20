package com.rexel.earlywarning.common.utils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.rexel.common.core.redis.RedisCache;
import com.rexel.common.utils.StringUtils;
import com.rexel.earlywarning.common.constants.EarlyConstants;
import com.rexel.earlywarning.common.enums.MqttEnums;
import com.rexel.earlywarning.service.IGrassEarlyWarningService;
import com.rexel.earlywarning.vo.DetailDataVo;
import com.rexel.earlywarning.vo.GrassEarlyWarningAlarmData;
import com.rexel.earlywarning.vo.JobDetailVo;
import com.rexel.earlywarning.vo.JobNoticeVo;
import com.rexel.mqtt.MessageHandlerAdapter;
import com.rexel.mqtt.MqttSender;
import com.rexel.mqtt.MqttService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * MqttUtils
 *
 * @author chunhui.qu
 * @date 2022-3-2
 */
@Slf4j
@Component
public class EaryWarningMqtt implements MessageHandlerAdapter {
    @Autowired
    private IGrassEarlyWarningService busyEarlyWarningService;
    @Autowired
    private MqttService mqttService;
    @Autowired
    private MqttSender mqttSender;
    @Autowired
    private RedisCache redisCache;

    /**
     * 发送消息
     *
     * @param topic topic
     * @param payload payload
     */
    public void publish(String topic, String payload) {
        if (StringUtils.isEmpty(topic) || StringUtils.isEmpty(payload)) {
            return;
        }
        mqttSender.sendWithTopic(topic, payload.getBytes());
        log.info("发送MQTT消息：topic={}, payload={}", topic, payload);
    }

    /**
     * 订阅消息
     *
     * @param topicFilter topicFilter
     */
    public void subscribe(String[] topicFilter) {
        if (topicFilter.length <= 0) {
            return;
        }
        for (String topic : topicFilter) {
            mqttService.addTopic(topic);
            log.info("订阅MQTT消息：topic={}", topic);
        }
    }

    /**
     * 取消订阅
     *
     * @param topicFilter topicFilter
     */
    public void unsubscribe(String[] topicFilter) {
        if (topicFilter.length <= 0) {
            return;
        }
        for (String topic : topicFilter) {
            mqttService.removeTopic(topic);
            log.info("取消订阅MQTT消息：topic={}", topic);
        }
    }

    /**
     * 获取Topic
     *
     * @param deviceId deviceId
     * @param usage usage
     * @return topic
     */
    public String getTopic(String deviceId, String usage) {
        return "/" + deviceId + usage;
    }

    /**
     * 缓存任务状态
     *
     * @param payload payload
     */
    private void doCacheJobDetail(String payload) {
        log.info("doCacheJobDetail. payload={}", payload);
        if (StringUtils.isEmpty(payload)) {
            return;
        }

        JobDetailVo jobDetailVo = JSON.parseObject(payload, JobDetailVo.class);
        if (jobDetailVo == null) {
            log.info("doCacheJobDetail jobDetailVo == null.");
            return;
        }

        if (!EarlyConstants.EVENT_JOB_DETAIL.equals(jobDetailVo.getEvent())) {
            log.info("doCacheJobDetail event error.");
            return;
        }

        List<DetailDataVo> dataList = jobDetailVo.getData();
        if (dataList == null || dataList.size() <= 0) {
            return;
        }

        for (DetailDataVo dataVo : dataList) {
            String jobId = dataVo.getJobId();
            String status = dataVo.getJobStatus();
            log.info("doCacheJobDetail cache. jobId={}, status={}", jobId, status);
            if (StringUtils.isNotEmpty(status)) {
                redisCache.setCacheObject(jobId, status, 1, TimeUnit.MINUTES);
            }
        }
    }

    /**
     * 重新提交任务
     *
     * @param payload payload
     */
    private void doSubmitAllJob(String payload) {
        log.info("doSubmitAllJob. payload={}", payload);
        if (StringUtils.isEmpty(payload)) {
            return;
        }

        JobNoticeVo jobNoticeVo = JSON.parseObject(payload, JobNoticeVo.class);
        if (jobNoticeVo == null) {
            log.info("doSubmitAllJob jobNoticeVo == null.");
            return;
        }

        if (!EarlyConstants.EVENT_NOTICE.equals(jobNoticeVo.getEvent())) {
            log.info("doSubmitAllJob event error.");
            return;
        }

        busyEarlyWarningService.submitAllJob();
    }

    /**
     * 写入报警数据
     *
     * @param payload payload
     */
    private void doWriteAlarmData(String payload) {
        log.info("doWriteAlarmData. payload={}", payload);
        if (StringUtils.isEmpty(payload)) {
            return;
        }

        GrassEarlyWarningAlarmData alarmData = JSONObject.parseObject(payload, GrassEarlyWarningAlarmData.class);
        if (alarmData == null) {
            log.info("doWriteAlarmData alarmData == null.");
            return;
        }

        busyEarlyWarningService.writeAlarmData(alarmData);
    }

    /**
     * 消息接收
     *
     * @param message message
     */
    @Override
    public void handleReceivedMessage(Message message) {
        MessageHeaders headers = message.getHeaders();
        if (!headers.containsKey(EarlyConstants.HEADER_TOPIC)) {
            return;
        }

        String topic = String.valueOf(headers.get(EarlyConstants.HEADER_TOPIC));
        String payload = message.getPayload().toString();
        log.debug("收到MQTT订阅消息。topic={}, message={}", topic, message);

        try {
            // 任务状态
            if (topic.endsWith(MqttEnums.USAGE.JOB_DETAIL.getValue())) {
                doCacheJobDetail(payload);
            }

            // 规则通知
            if (topic.endsWith(MqttEnums.USAGE.JOB_NOTICE.getValue())) {
                doSubmitAllJob(payload);
            }

            // 报警数据
            if (topic.endsWith(MqttEnums.USAGE.ALARM_DATA.getValue())) {
                doWriteAlarmData(payload);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
