package com.rexel.framework.mq.redis.core.message;

import lombok.Data;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

/**
 * Redis 消息抽象基类
 *
 * @author
 */
@Data
@ToString
public abstract class AbstractRedisMessage {

    /**
     * 头
     */
    private Map<String, String> headers = new HashMap<>();

    private Object body;

    public String getHeader(String key) {
        return headers.get(key);
    }

    public void addHeader(String key, String value) {
        headers.put(key, value);
    }

}
