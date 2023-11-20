//package com.rexel.mqtt.old;
//
//import com.rexel.common.utils.spring.SpringUtils;
//import com.rexel.mqtt.old.factory.MqttPahoClientFactory;
//import org.eclipse.paho.client.mqttv3.*;
//import org.springframework.core.log.LogAccessor;
//import org.springframework.messaging.MessagingException;
//import org.springframework.scheduling.TaskScheduler;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
//import org.springframework.util.Assert;
//
//import java.util.Arrays;
//import java.util.Date;
//import java.util.concurrent.ScheduledFuture;
//import java.util.concurrent.locks.ReentrantLock;
//
///**
// * @ClassName MqttPaho
// * @Description mqtt客户端 发布订阅
// * @Author 孟开通
// * @Date 2022/8/29 14:19
// **/
//public class MqttPaho extends AbstractMqttPahoAdapter implements MqttCallback {
//    public static final long DEFAULT_COMPLETION_TIMEOUT = 30_000L;
//    public static final long DISCONNECT_COMPLETION_TIMEOUT = 5_000L;
//    private static final int DEFAULT_RECOVERY_INTERVAL = 10_000;
//    protected final LogAccessor logger = new LogAccessor(getClass());
//    /**
//     * mqtt工厂
//     */
//    private final MqttPahoClientFactory clientFactory;
//    private int recoveryInterval = DEFAULT_RECOVERY_INTERVAL;
//    private long completionTimeout = DEFAULT_COMPLETION_TIMEOUT;
//    private long disconnectCompletionTimeout = DISCONNECT_COMPLETION_TIMEOUT;
//    /**
//     * mqtt客户端
//     */
//    private volatile IMqttAsyncClient client;
//    private volatile ScheduledFuture<?> reconnectFuture;
//
//    protected final ReentrantLock connectionStatus = new ReentrantLock();
//
//    private boolean async;
//    private volatile boolean connected;
//    private ThreadPoolTaskScheduler taskScheduler;
//
//    public MqttPaho(String clientId, MqttPahoClientFactory clientFactory) {
//        super(null, clientId);
//        this.clientFactory = clientFactory;
//    }
//
//    public void setAsync(boolean async) {
//        this.async = async;
//    }
//
//    public void setCompletionTimeout(long completionTimeout) {
//        // NOSONAR (sync)
//        this.completionTimeout = completionTimeout;
//    }
//
//    public void setDisconnectCompletionTimeout(long completionTimeout) {
//        // NOSONAR (sync)
//        this.disconnectCompletionTimeout = completionTimeout;
//    }
//
//
//    /**
//     * 连接异常
//     *
//     * @param cause the reason behind the loss of connection.
//     */
//    @Override
//    public synchronized void connectionLost(Throwable cause) {
//        if (isRunning()) {
//            this.logger.error(() -> "Lost connection: " + cause.getMessage() + "; retrying...");
//            this.connected = false;
//            if (this.client != null) {
//                try {
//                    this.client.setCallback(null);
//                    this.client.close();
//                } catch (MqttException e) {
//                    e.printStackTrace();
//                    // NOSONAR
//                }
//            }
//            this.client = null;
//            this.clientFactory.removeClient(getUrl(), getClientId());
//            scheduleReconnect();
//        }
//    }
//
//
//    private synchronized void cancelReconnect() {
//        if (this.reconnectFuture != null) {
//            this.reconnectFuture.cancel(false);
//            this.reconnectFuture = null;
//        }
//    }
//
//    /**
//     * 定时重连
//     */
//    private synchronized void scheduleReconnect() {
//        cancelReconnect();
//        if (isActive()) {
//            try {
//                this.reconnectFuture = getTaskScheduler().schedule(() -> {
//                    try {
//                        this.connectionStatus.lock();
//                        logger.warn("Attempting reconnect");
//                        if (!MqttPaho.this.connected) {
//                            connectAndSubscribe();
//                            MqttPaho.this.reconnectFuture = null;
//                        }
//                    } catch (MqttException ex) {
//                        ex.printStackTrace();
//                        logger.error(ex, "Exception while connecting and subscribing");
//                        scheduleReconnect();
//                    } finally {
//                        this.connectionStatus.unlock();
//                    }
//                }, new Date(System.currentTimeMillis() + DEFAULT_RECOVERY_INTERVAL));
//            } catch (Exception ex) {
//                logger.error(ex, "Failed to schedule reconnect");
//            }
//        }
//    }
//
//
//    /**
//     * 推送之前检查连接并获取客户端
//     *
//     * @return
//     * @throws MqttException
//     */
//    private synchronized IMqttAsyncClient checkConnection() throws MqttException {
//        try {
//            this.connectionStatus.lock();
//            if (this.client != null && !this.client.isConnected()) {
//                this.client.setCallback(null);
//                this.client.close();
//                this.client = null;
//                this.clientFactory.removeClient(getUrl(), getClientId());
//            }
//            if (this.client == null) {
//                try {
//                    createClientAndConnect();
//                    logger.debug("Client connected");
//                } catch (MqttException e) {
//                    if (this.client != null) {
//                        this.client.close();
//                        this.client = null;
//                        this.clientFactory.removeClient(getUrl(), getClientId());
//                        //logger.warn("手动重连客户端赋空");
//                    }
//                    throw new MessagingException("Failed to connect", e);
//                }
//            }
//        } catch (MqttException e) {
//            e.printStackTrace();
//        } finally {
//            this.connectionStatus.unlock();
//        }
//        return this.client;
//    }
//
//    /**
//     * 推送消息
//     */
//    public synchronized void publish(String topic, MqttMessage message) {
//        try {
//            IMqttDeliveryToken token = checkConnection().publish(topic, message);
//            if (!this.async) {
//                token.waitForCompletion(this.completionTimeout);
//            }
//        } catch (MqttException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 添加topic并订阅
//     *
//     * @param topic
//     * @param qos
//     */
//    @Override
//    public synchronized void addTopic(String topic, int qos) {
//        this.topicLock.lock();
//        try {
//            super.addTopic(topic, qos);
//            if (this.client != null && this.client.isConnected()) {
//                this.client.subscribe(topic, qos);
//            }
//        } catch (MqttException e) {
//            super.removeTopic(topic);
//            throw new MessagingException("Failed to subscribe to topic " + topic, e);
//        } finally {
//            this.topicLock.unlock();
//        }
//    }
//
//    /**
//     * 删除topic，取消订阅
//     *
//     * @param topic
//     */
//    @Override
//    public void removeTopic(String... topic) {
//        this.topicLock.lock();
//        try {
//            if (this.client != null && this.client.isConnected()) {
//                this.client.unsubscribe(topic);
//            }
//            super.removeTopic(topic);
//        } catch (MqttException e) {
//            throw new MessagingException("Failed to unsubscribe from topic(s) " + Arrays.toString(topic), e);
//        } finally {
//            this.topicLock.unlock();
//        }
//    }
//
//    /**
//     * 连接并订阅
//     *
//     * @throws MqttException
//     */
//    private void connectAndSubscribe() throws MqttException {
//        String[] topics = getTopic();
//        try {
//            this.topicLock.lock();
//            createClientAndConnect();
//            int[] requestedQos = getQos();
//            int[] grantedQos = Arrays.copyOf(requestedQos, requestedQos.length);
//            this.client.subscribe(topics, grantedQos);
//        } catch (MqttException ex) {
//            logger.error(ex, () -> "Error connecting or subscribing to " + Arrays.toString(topics));
//            // Could be reset during event handling before
//            if (this.client != null) {
//                this.client.disconnectForcibly(this.disconnectCompletionTimeout);
//                try {
//                    this.client.setCallback(null);
//                    this.client.close();
//                } catch (MqttException e1) {
//                    e1.printStackTrace();
//                    // NOSONAR
//                }
//                this.client = null;
//                this.clientFactory.removeClient(getUrl(), getClientId());
//                //logger.error("重连订阅失败！！！！！");
//            }
//            throw ex;
//        } finally {
//            this.topicLock.unlock();
//        }
//        if (this.client.isConnected()) {
//            this.connected = true;
//            String message = "Connected and subscribed to " + Arrays.toString(topics);
//            logger.debug(message);
//        }
//    }
//
//    /**
//     * 接收消息
//     *
//     * @param topic   name of the topic on the message was published to
//     * @param message the actual message.
//     * @throws Exception
//     */
//    @Override
//    public void messageArrived(String topic, MqttMessage message) throws Exception {
//    }
//
//    /**
//     * 发送消息回调
//     *
//     * @param token the delivery token associated with the message.
//     */
//    @Override
//    public void deliveryComplete(IMqttDeliveryToken token) {
//        logger.info("发送成功:" + token.isComplete());
//    }
//
//
//    /**
//     * 初始化
//     *
//     * @throws MqttException
//     */
//    @Override
//    protected void doStart() throws MqttException {
//        Assert.state(getTaskScheduler() != null, "A 'taskScheduler' is required");
//        createClientAndConnect();
//    }
//
//    /**
//     * 销毁
//     */
//    @Override
//    protected synchronized void doStop() {
//        cancelReconnect();
//        if (this.client != null) {
//            try {
//                if (this.client.isConnected()) {
//                    this.client.unsubscribe(getTopic());
//                }
//            } catch (MqttException ex) {
//                logger.error(ex, "Exception while unsubscribing");
//            }
//            try {
//                this.client.disconnectForcibly(this.disconnectCompletionTimeout);
//            } catch (MqttException ex) {
//                logger.error(ex, "Exception while disconnecting");
//            }
//
//            this.client.setCallback(null);
//
//            try {
//                this.client.close();
//            } catch (MqttException ex) {
//                logger.error(ex, "Exception while closing");
//            }
//            this.connected = false;
//            this.client = null;
//            this.clientFactory.removeClient(getUrl(), getClientId());
//        }
//    }
//
//    protected synchronized TaskScheduler getTaskScheduler() {
//        if (this.taskScheduler == null) {
//            this.taskScheduler = SpringUtils.getBean("threadPoolTaskScheduler");
//        }
//        return this.taskScheduler;
//    }
//
//
//    private void createClientAndConnect() throws MqttException {
//        MqttConnectOptions connectionOptions = this.clientFactory.getConnectionOptions();
//        this.client = this.clientFactory.getAsyncClientInstance(this.getUrl(), this.getClientId());
//        this.client.setCallback(this);
//        if (!client.isConnected()) {
//            this.client.connect(connectionOptions).waitForCompletion(this.completionTimeout);
//        }
//    }
//}
