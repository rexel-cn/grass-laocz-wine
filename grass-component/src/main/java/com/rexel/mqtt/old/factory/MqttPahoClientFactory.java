//package com.rexel.mqtt.old.factory;
//
//import org.eclipse.paho.client.mqttv3.IMqttAsyncClient;
//import org.eclipse.paho.client.mqttv3.IMqttClient;
//import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
//import org.eclipse.paho.client.mqttv3.MqttException;
//
///**
// * @ClassName MqttPahoProducer
// * @Description mqtt客户端工厂
// * @Author 孟开通
// * @Date 2022/8/29 14:19
// **/
//public interface MqttPahoClientFactory {
//
//    /**
//     * Retrieve a client instance.
//     *
//     * @param url      The URL.
//     * @param clientId The client id.
//     * @return The client instance.
//     * @throws MqttException Any.
//     */
//    IMqttClient getClientInstance(String url, String clientId) throws MqttException;
//
//    /**
//     * Retrieve an async client instance.
//     *
//     * @param url      The URL.
//     * @param clientId The client id.
//     * @return The client instance.
//     * @throws MqttException Any.
//     * @since 4.1
//     */
//    IMqttAsyncClient getAsyncClientInstance(String url, String clientId) throws MqttException;
//
//    /**
//     * Retrieve the connection options.
//     *
//     * @return The options.
//     */
//    MqttConnectOptions getConnectionOptions();
//
//    void removeClient(String url, String clientId);
//}
