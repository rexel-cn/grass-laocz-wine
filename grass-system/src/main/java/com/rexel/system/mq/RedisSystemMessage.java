package com.rexel.system.mq;

import com.rexel.framework.mq.redis.core.pubsub.AbstractChannelMessage;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @ClassName RedisSystemMessage
 * @Description
 * @Author 孟开通
 * @Date 2023/1/31 17:15
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class RedisSystemMessage extends AbstractChannelMessage {
    @Override
    public String getChannel() {
        return "user.refresh";
    }
}
