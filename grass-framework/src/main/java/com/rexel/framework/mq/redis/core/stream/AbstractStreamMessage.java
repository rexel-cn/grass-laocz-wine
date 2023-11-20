package com.rexel.framework.mq.redis.core.stream;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rexel.framework.mq.redis.core.message.AbstractRedisMessage;

/**
 * Redis Stream Message 抽象类
 *
 * @author
 */
public abstract class AbstractStreamMessage extends AbstractRedisMessage {

    /**
     * 获得 Redis Stream Key
     *
     * @return Channel
     */
    @JsonIgnore // 避免序列化
    public abstract String getStreamKey();

}
