package com.rexel.mqtt;

/**
 * @ClassName MqttService
 * @Description
 * @Author 孟开通
 * @Date 2023/8/30 18:05
 **/
public interface MqttService {

    public void addTopic(String topic);

    public void removeTopic(String topic);
}
