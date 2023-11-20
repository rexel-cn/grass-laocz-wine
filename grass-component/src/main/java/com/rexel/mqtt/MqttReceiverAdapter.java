package com.rexel.mqtt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MqttReceiverAdapter {
    private final MessageHandlerAdapter messageHandlerAdapter;

    public MqttReceiverAdapter(@Autowired(required = false) MessageHandlerAdapter messageHandlerAdapter) {
        this.messageHandlerAdapter = messageHandlerAdapter;
    }

    @ServiceActivator(inputChannel = "mqttInboundChannel")
    public void handleMessage(Message message) {
        if (messageHandlerAdapter != null) {
            messageHandlerAdapter.handleReceivedMessage(message);
            return;
        }
        log.info("MessageHandlerAdapter接口没有实现, message is {}", message);
    }
}
