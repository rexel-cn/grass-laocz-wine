package com.rexel.framework.mq.redis.config;

import com.rexel.framework.mq.redis.core.RedisMQTemplate;
import com.rexel.framework.mq.redis.core.interceptor.RedisMessageInterceptor;
import com.rexel.framework.mq.redis.core.pubsub.AbstractChannelMessageListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.List;

/**
 * 消息队列配置类
 *
 * @author
 */
@Slf4j
@Configuration
public class MQAutoConfiguration {

    @Bean
    public RedisMQTemplate redisMQTemplate(StringRedisTemplate redisTemplate,
                                           List<RedisMessageInterceptor> interceptors) {
        RedisMQTemplate redisMQTemplate = new RedisMQTemplate(redisTemplate);
        // 添加拦截器
        interceptors.forEach(redisMQTemplate::addInterceptor);
        return redisMQTemplate;
    }

    // ========== 消费者相关 ==========

    /**
     * 创建 Redis Pub/Sub 广播消费的容器
     */
    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(
            RedisMQTemplate redisMQTemplate, List<AbstractChannelMessageListener<?>> listeners) {
        // 创建 RedisMessageListenerContainer 对象
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        // 设置 RedisConnection 工厂。
        container.setConnectionFactory(redisMQTemplate.getRedisTemplate().getRequiredConnectionFactory());
        // 添加监听器
        listeners.forEach(listener -> {
            listener.setRedisMQTemplate(redisMQTemplate);
            container.addMessageListener(listener, new ChannelTopic(listener.getChannel()));
            log.info("[redisMessageListenerContainer][注册 Channel({}) 对应的监听器({})]",
                    listener.getChannel(), listener.getClass().getName());
        });
        container.setTaskExecutor(redisTaskExecutor());
        return container;
    }

    @Bean
    ThreadPoolTaskExecutor redisTaskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        // 1.核心线程数
        taskExecutor.setCorePoolSize(5);
        // 2.最大线程数
        taskExecutor.setMaxPoolSize(100);
        // 3.队列最大长度
        taskExecutor.setQueueCapacity(5000);
        // 4.线程池维护线程允许的空闲时间，默认60s
        taskExecutor.setKeepAliveSeconds(60);
        return taskExecutor;
    }
}
