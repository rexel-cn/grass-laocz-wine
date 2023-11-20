//package com.rexel.mqtt.old;
//
//import cn.hutool.core.collection.CollectionUtil;
//import cn.hutool.core.date.DatePattern;
//import cn.hutool.core.date.DateUtil;
//import cn.hutool.core.util.ObjectUtil;
//import cn.hutool.core.util.StrUtil;
//import com.rexel.common.utils.StringUtils;
//import com.rexel.mqtt.old.factory.MqttPahoClientFactory;
//import lombok.extern.slf4j.Slf4j;
//import org.eclipse.paho.client.mqttv3.MqttException;
//import org.eclipse.paho.client.mqttv3.MqttMessage;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PreDestroy;
//import java.util.Date;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.Executor;
//
///**
// * @ClassName MqttUtil2
// * @Description
// * @Author 孟开通
// * @Date 2022/8/30 17:29
// **/
//@Slf4j
//@Component
//public class MqttUtil2 {
//
//    private final Map<String, MqttPaho> mqttClientMap = new ConcurrentHashMap<>();
//    @Autowired
//    private MqttPahoClientFactory mqttPahoClientFactory;
//    @Autowired
//    @Qualifier("sendMessageExecutor")
//    private Executor executor;
//
//    @PreDestroy
//    private void close() {
//        if (CollectionUtil.isEmpty(mqttClientMap)) {
//            return;
//        }
//        for (Map.Entry<String, MqttPaho> next : mqttClientMap.entrySet()) {
//            String clientId = next.getKey();
//            MqttPaho mqttClient = next.getValue();
//            executor.execute(() -> {
//                try {
//                    if (mqttClient.isRunning()) {
//                        mqttClient.stop();
//                    }
//                } finally {
//                    mqttClientMap.remove(clientId);
//                    log.info("关闭MQTT客户端:" + clientId);
//                }
//            });
//        }
//    }
//
//    /**
//     * 发送消息
//     *
//     * @param topic   topic
//     * @param payload payload
//     */
//    public void publish(String clientId, String topic, String payload) {
//        if (StrUtil.isEmpty(topic) || StrUtil.isEmpty(payload) || StrUtil.isEmpty(clientId)) {
//            return;
//        }
//        MqttPaho mqttClient;
//        try {
//            mqttClient = getClient(clientId);
//        } catch (MqttException e) {
//            log.error("获取客户端失败", e);
//            throw new RuntimeException(e);
//        }
//        try {
//            MqttMessage mqttMessage = new MqttMessage();
//            mqttMessage.setPayload(payload.getBytes());
//            mqttMessage.setQos(1);
//            mqttMessage.setRetained(false);
//            mqttClient.publish(topic, mqttMessage);
//            log.info(":发送MQTT消息：topic={}, payload={}", topic, payload);
//        } catch (Exception e) {
//            log.error(":mqtt2客户端发送异常");
//            e.printStackTrace();
//            throw new RuntimeException(e);
//        }
//
//    }
//
//    /**
//     * 订阅
//     *
//     * @param clientId
//     * @param topicFilter
//     */
//    public void subscribe(String clientId, String topicFilter) {
//        if (StringUtils.isEmpty(clientId) || StringUtils.isEmpty(topicFilter)) {
//            return;
//        }
//        MqttPaho mqttClient;
//        try {
//            mqttClient = getClient(clientId);
//        } catch (MqttException e) {
//            log.error("获取客户端失败", e);
//            throw new RuntimeException(e);
//        }
//        try {
//            mqttClient.addTopic(topicFilter, 1);
//            log.info(DateUtil.format(new Date(), DatePattern.NORM_DATETIME_PATTERN) + ":订阅MQTT消息：topicFilters={}", topicFilter);
//        } catch (Exception e) {
//            log.error(DateUtil.format(new Date(), DatePattern.NORM_DATETIME_PATTERN) + ":mqtt2客户端订阅异常");
//            e.printStackTrace();
//        }
//
//    }
//
//    /**
//     * 获取客户端并初始化连接
//     *
//     * @param clientId
//     * @return
//     * @throws MqttException
//     */
//    private synchronized MqttPaho getClient(String clientId) throws MqttException {
//        MqttPaho cacheClient;
//        if (ObjectUtil.isNotNull(cacheClient = mqttClientMap.get(clientId))) {
//            return cacheClient;
//        }
//        cacheClient = new MqttPaho(clientId, mqttPahoClientFactory);
//        cacheClient.setAsync(true);
//        cacheClient.start();
//        mqttClientMap.put(clientId, cacheClient);
//        return cacheClient;
//    }
//}
