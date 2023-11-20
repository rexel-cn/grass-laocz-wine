package com.rexel.system.mq;

import com.alibaba.fastjson2.JSON;
import com.rexel.framework.mq.redis.core.pubsub.AbstractChannelMessageListener;
import com.rexel.system.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ClassName RedisSystemConsumer
 * @Description
 * @Author 孟开通
 * @Date 2023/1/31 17:15
 **/
@Slf4j
@Component
public class RedisSystemConsumer extends AbstractChannelMessageListener<RedisSystemMessage> {
    @Autowired
    private ISysUserService sysUserService;

    /**
     * 处理消息
     *
     * @param message 消息
     */
    @Override
    public void onMessage(RedisSystemMessage message) {
        log.info("[刷新用户缓存]——>>>>接收到消息：{}", JSON.toJSON(message));
        Long userId = null;
        if (message.getBody() != null) {
            userId = Long.parseLong(message.getBody().toString());
        }
        sysUserService.refreshUserCache(userId);
    }
}
