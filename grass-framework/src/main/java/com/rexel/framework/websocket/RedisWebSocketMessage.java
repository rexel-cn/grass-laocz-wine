package com.rexel.framework.websocket;

import com.rexel.framework.mq.redis.core.pubsub.AbstractChannelMessage;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @ClassName RedisWebSocketMessage
 * @Description
 * @Author 孟开通
 * @Date 2023/1/17 15:01
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class RedisWebSocketMessage extends AbstractChannelMessage {
    /**
     * 获得 Redis Channel
     *
     * @return Channel
     */
    @Override
    public String getChannel() {
        return "websocket.send";
    }
}
