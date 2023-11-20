package com.rexel.system.mq;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.rexel.framework.mq.redis.core.RedisMQTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

import static com.rexel.framework.web.util.WebFrameworkUtils.HEADER_TENANT_ID;

/**
 * @ClassName RedisSystemProuder
 * @Description redis 用户权限信息刷新
 * @Author 孟开通
 * @Date 2023/1/31 17:20
 **/
@Component
public class RedisSystemProducer {

    /**
     * 是否发送消息 true 发送消息  false 不发送消息
     */
    public static final ThreadLocal<Boolean> IS_SEND_MESSAGE = new TransmittableThreadLocal<>();


    /**
     * 临时保存 需要刷新的用户信息
     */
    public static final ThreadLocal<Map<String, RedisSystemMessage>> LIST_THREAD_LOCAL = new TransmittableThreadLocal<>();


    @Resource
    private RedisMQTemplate redisMQTemplate;

    /**
     * 发送用户刷新消息
     *
     * @param userId   用户id
     * @param tenantId 租户id
     */
    public void sendUserRefreshMessage(Long userId, String tenantId) {
        //如果不发送消息 则将消息保存到临时变量中
        if (IS_SEND_MESSAGE.get() != null && !IS_SEND_MESSAGE.get()) {
            addRefreshMessage(userId, tenantId);
            return;
        }
        RedisSystemMessage message = new RedisSystemMessage();
        message.setBody(userId);
        message.addHeader(HEADER_TENANT_ID, tenantId);
        redisMQTemplate.send(message);
    }

    /**
     * 添加刷新消息
     *
     * @param userId   用户id
     * @param tenantId 租户id
     */
    private void addRefreshMessage(Long userId, String tenantId) {
        RedisSystemMessage message = new RedisSystemMessage();
        message.setBody(userId);
        message.addHeader(HEADER_TENANT_ID, tenantId);
        Map<String, RedisSystemMessage> stringRedisSystemMessageMap = LIST_THREAD_LOCAL.get();
        if (stringRedisSystemMessageMap == null) {
            stringRedisSystemMessageMap = new HashMap<>();
            LIST_THREAD_LOCAL.set(stringRedisSystemMessageMap);
        }
        stringRedisSystemMessageMap.put(userId + tenantId, message);
    }

    /**
     * 刷新消息
     */
    public void flushRefreshMessage() {
        try {
            Map<String, RedisSystemMessage> stringRedisSystemMessageMap = LIST_THREAD_LOCAL.get();
            if (stringRedisSystemMessageMap == null) {
                return;
            }
            stringRedisSystemMessageMap.values().forEach(redisSystemMessage -> redisMQTemplate.send(redisSystemMessage));
        } finally {
            clear();
        }
    }

    /**
     * 清除临时变量
     */
    public void clear() {
        IS_SEND_MESSAGE.remove();
        LIST_THREAD_LOCAL.remove();
    }
}
