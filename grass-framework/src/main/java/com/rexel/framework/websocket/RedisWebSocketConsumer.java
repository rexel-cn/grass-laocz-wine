package com.rexel.framework.websocket;

import com.alibaba.fastjson2.JSON;
import com.rexel.framework.mq.redis.core.pubsub.AbstractChannelMessageListener;
import com.rexel.framework.websocket.readmsg.PushWebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @ClassName RedisWebSocketServer
 * @Description
 * @Author 孟开通
 * @Date 2023/1/17 15:00
 **/

@Slf4j
@Component
public class RedisWebSocketConsumer extends AbstractChannelMessageListener<RedisWebSocketMessage> {
    /**
     * 处理消息
     *
     * @param message 消息
     */
    @Override
    public void onMessage(RedisWebSocketMessage message) {
        log.info("接收到消息：{}", JSON.toJSON(message));
        PushWebSocketServer.sendAsyInfo(message.getBody() + "", message.getHeader("userId"));
    }
}
