package com.rexel.mqtt;

import com.rexel.mqtt.source.support.MqttHeaders;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

/**
 * @ClassName MqttProperties
 * @Description mqtt发送消息接口
 * @Author 孟开通
 * @Date 2023/8/31 11:06
 **/
@Component
@MessagingGateway(defaultRequestChannel = "mqttOutboundChannel")
public interface MqttSender {
    void sendToMqtt(Object o);

    void sendWithTopic(@Header(MqttHeaders.TOPIC) String topic, Object o);

    void sendWithTopicAndQos(@Header(MqttHeaders.TOPIC) String topic, @Header(MqttHeaders.QOS) Integer qos, Object o);
}
