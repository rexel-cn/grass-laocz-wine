package com.rexel.mqtt;

import org.springframework.messaging.Message;

public interface MessageHandlerAdapter {
    void handleReceivedMessage(Message message);
}
