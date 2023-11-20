//package com.rexel.mqtt.old.factory;
//
//import com.rexel.mqtt.source.core.MqttPahoClientFactory;
//import org.eclipse.paho.client.mqttv3.*;
//import org.springframework.util.Assert;
//
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
///**
// * @ClassName MqttPahoProducer
// * @Description mqtt客户端工厂
// * @Author 孟开通
// * @Date 2022/8/29 14:19
// **/
//public class DefaultMqttPahoClientFactory implements MqttPahoClientFactory {
//
//    private MqttConnectOptions options = new MqttConnectOptions();
//
//    private MqttClientPersistence persistence;
//    private Map<String, IMqttAsyncClient> iMqttAsyncClientMap = new ConcurrentHashMap<>();
//
//    /**
//     * Set the persistence to pass into the client constructor.
//     *
//     * @param persistence the persistence to set.
//     */
//    public void setPersistence(MqttClientPersistence persistence) {
//        this.persistence = persistence;
//    }
//
//
//    @Override
//    public IMqttClient getClientInstance(String uri, String clientId) throws MqttException {
//        // Client validates URI even if overridden by options
//        return new MqttClient(uri == null ? "tcp://NO_URL_PROVIDED" : uri, clientId, this.persistence);
//    }
//
//    @Override
//    public synchronized IMqttAsyncClient getAsyncClientInstance(String uri, String clientId) throws MqttException {
//        String key = uri + clientId;
//        IMqttAsyncClient iMqttAsyncClient = iMqttAsyncClientMap.get(key);
//        if (iMqttAsyncClient == null) {
//            iMqttAsyncClient = new MqttAsyncClient(uri == null ? "tcp://NO_URL_PROVIDED" : uri, clientId, this.persistence);
//            iMqttAsyncClientMap.put(key, iMqttAsyncClient);
//        }
//        // Client validates URI even if overridden by options
//        return iMqttAsyncClient;
//    }
//
//    @Override
//    public MqttConnectOptions getConnectionOptions() {
//        return this.options;
//    }
//
//    /**
//     * Set the preconfigured {@link MqttConnectOptions}.
//     *
//     * @param options the options.
//     * @since 4.3.16
//     */
//    public void setConnectionOptions(MqttConnectOptions options) {
//        Assert.notNull(options, "MqttConnectOptions cannot be null");
//        this.options = options;
//    }
//
//    @Override
//    public void removeClient(String url, String clientId) {
//        String key = url + clientId;
//        iMqttAsyncClientMap.remove(key);
//    }
//
//    public static class Will {
//
//        private final String topic;
//
//        private final byte[] payload;
//
//        private final int qos;
//
//        private final boolean retained;
//
//        public Will(String topic, byte[] payload, int qos, boolean retained) { //NOSONAR
//            this.topic = topic;
//            this.payload = payload; //NOSONAR
//            this.qos = qos;
//            this.retained = retained;
//        }
//
//        protected String getTopic() {
//            return this.topic;
//        }
//
//        protected byte[] getPayload() {
//            return this.payload; //NOSONAR
//        }
//
//        protected int getQos() {
//            return this.qos;
//        }
//
//        protected boolean isRetained() {
//            return this.retained;
//        }
//
//    }
//
//}
