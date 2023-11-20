//package com.rexel.mqtt.old;
//
//import cn.hutool.core.collection.CollectionUtil;
//import cn.hutool.core.util.ObjectUtil;
//import cn.hutool.core.util.StrUtil;
//import com.rexel.mqtt.old.config.MqttConf;
//import lombok.extern.slf4j.Slf4j;
//import org.eclipse.paho.client.mqttv3.*;
//import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PreDestroy;
//import java.util.Arrays;
//import java.util.Date;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.ScheduledFuture;
//
///**
// * MqttUtils
// *
// * @author chunhui.qu
// * @date 2022-3-2
// */
//@Slf4j
//@Component
//public class MqttUtils implements MqttCallback {
//    private int recoveryInterval = 10_000;
//    private volatile ScheduledFuture<?> reconnectFuture;
//    private final Map<String, MqttClient> mqttClientMap = new ConcurrentHashMap<>();
//    @Autowired
//    private MqttConf mqttConf;
//    @Autowired
//    @Qualifier("threadPoolTaskScheduler")
//    private ThreadPoolTaskScheduler threadPoolTaskScheduler;
//
//    /**
//     * 关闭所有客户端
//     */
//    @PreDestroy
//    private void close() {
//        if (CollectionUtil.isEmpty(mqttClientMap)) {
//            return;
//        }
//        for (Map.Entry<String, MqttClient> next : mqttClientMap.entrySet()) {
//            String clientId = next.getKey();
//            MqttClient mqttClient = next.getValue();
//            try {
//                if (mqttClient.isConnected()) {
//                    mqttClient.disconnect();
//                    log.info("断开MQTT连接成功。" + clientId);
//                }
//                mqttClient.close();
//                log.info("关闭MQTT客户端。");
//            } catch (MqttException e) {
//                e.printStackTrace();
//            } finally {
//                mqttClientMap.remove(clientId);
//            }
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
//        MqttClient mqttClient = getClient(clientId);
//        MqttMessage mqttMessage = new MqttMessage();
//        mqttMessage.setPayload(payload.getBytes());
//        mqttMessage.setQos(1);
//        mqttMessage.setRetained(false);
//        try {
//            mqttClient.publish(topic, mqttMessage);
//            log.info("发送MQTT消息：topic={}, payload={}", topic, payload);
//        } catch (MqttException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void subscribe(String clientId, String[] topicFilter) {
//        if (topicFilter.length == 0) {
//            return;
//        }
//        MqttClient mqttClient = getClient(clientId);
//        int[] qos = new int[topicFilter.length];
//        Arrays.fill(qos, 1);
//        try {
//            mqttClient.subscribe(topicFilter, qos);
//            log.info("订阅MQTT消息：topicFilters={}", Arrays.toString(topicFilter));
//        } catch (MqttException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 取消订阅
//     *
//     * @param topicFilter topicFilter
//     */
//    public void unsubscribe(String clientId, String[] topicFilter) {
//        if (topicFilter.length == 0) {
//            return;
//        }
//        MqttClient mqttClient = getClient(clientId);
//        if (mqttClient == null) {
//            return;
//        }
//        try {
//            mqttClient.unsubscribe(topicFilter);
//            log.info("取消订阅MQTT消息：topicFilters={}", Arrays.toString(topicFilter));
//        } catch (MqttException e) {
//            e.printStackTrace();
//        }
//    }
//    /**
//     * getClient
//     *
//     * @return 结果
//     */
//    private synchronized MqttClient getClient(String clientId) {
//        MqttClient cacheClient;
//        if (ObjectUtil.isNotNull(cacheClient = mqttClientMap.get(clientId))) {
//            return cacheClient;
//        }
//
//        String serverUrl = mqttConf.getServerUrl();
//        String username = mqttConf.getUsername();
//        String password = mqttConf.getPassword();
//
//        MqttConnectOptions options = new MqttConnectOptions();
//        // 设置连接的用户名
//        options.setUserName(username);
//        // 设置连接的密码
//        options.setPassword(password.toCharArray());
//        // 设置是否清空session
//        // 设置为false表示服务器会保留客户端的连接记录
//        // 设置为true表示每次连接到服务器都以新的身份连接
//        options.setCleanSession(false);
//        // 设置会话心跳时间
//        options.setAutomaticReconnect(true);
//        // 设置超时时间,单位为秒.设置为0，防止Timed out as no activity错误
//        options.setConnectionTimeout(30);
//        // 设置会话心跳时间,单位为秒
//        // 服务器会每隔1.5*20秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
//        options.setKeepAliveInterval(60);
//        try {
//            // host:服务连接地址
//            // clientIId:连接MQTT的客户端ID，一般以唯一标识符表示
//            // MemoryPersistence:clientId的保存形式，默认为以内存保存
//            MqttClient client = new MqttClient(serverUrl, clientId, new MemoryPersistence());
//            // 设置监听对象
//            client.setCallback(this);
//            // 建立客户端连接
//            client.connect(options);
//            // 保存客户端连接
//            mqttClientMap.put(clientId, client);
//            return client;
//        } catch (MqttException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    @Override
//    public void deliveryComplete(IMqttDeliveryToken token) {
//        log.info("发送成功:" + token.isComplete());
//    }
//
//    @Override
//    public void connectionLost(Throwable cause) {
//        for (Map.Entry<String, MqttClient> next : mqttClientMap.entrySet()) {
//            scheduleReconnect(next.getValue());
//        }
//    }
//
//    @Override
//    public void messageArrived(String topic, MqttMessage message) {
//        log.debug("收到MQTT订阅消息。topic={}, message={}", topic, message);
//    }
//
//    private synchronized void scheduleReconnect(MqttClient mqttClient) {
//        cancelReconnect();
//        reconnectFuture = threadPoolTaskScheduler.schedule(() -> {
//            synchronized (this) {
//                if (mqttClient == null) {
//                    return;
//                }
//                if (mqttClient.isConnected()) {
//                    log.info("MQTT连接状态正常。");
//                    return;
//                }
//                try {
//                    mqttClient.reconnect();
//                    log.info("重新建立客户端连接。");
//                    this.reconnectFuture = null;
//                } catch (MqttException e) {
//                    e.printStackTrace();
//                    scheduleReconnect(mqttClient);
//                }
//            }
//        }, new Date(System.currentTimeMillis() + recoveryInterval));
//
//    }
//
//
//    private synchronized void cancelReconnect() {
//        if (this.reconnectFuture != null) {
//            this.reconnectFuture.cancel(false);
//            this.reconnectFuture = null;
//        }
//    }
//}
