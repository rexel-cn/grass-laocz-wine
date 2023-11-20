package com.rexel.framework.websocket;/*
package com.rexel.framework.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

*/
/**
 * @author donghai
 * <p>
 * 初始化redis监听容器
 * @param connectionFactory RedisConnectionFactory
 * @return RedisMessageListenerContainer
 * <p>
 * 创建线程池
 * 用于redis消息监听
 * @return ThreadPoolTaskExecutor
 * <p>
 * 监听器1，监听频道：read-msg
 * @param redisReceiver RedisReceiverChannel
 * @return MessageListenerAdapter
 * <p>
 * 初始化redis操作模板
 * @param connectionFactory RedisConnectionFactory
 * @return StringRedisTemplate
 * <p>
 * 初始化redis监听容器
 * @param connectionFactory RedisConnectionFactory
 * @return RedisMessageListenerContainer
 * <p>
 * 创建线程池
 * 用于redis消息监听
 * @return ThreadPoolTaskExecutor
 * <p>
 * 监听器1，监听频道：read-msg
 * @param redisReceiver RedisReceiverChannel
 * @return MessageListenerAdapter
 * <p>
 * 初始化redis操作模板
 * @param connectionFactory RedisConnectionFactory
 * @return StringRedisTemplate
 *//*

@Configuration
public class RedisListenerConfig {
    */
/**
 * 初始化redis监听容器
 *
 * @param connectionFactory RedisConnectionFactory
 * @return RedisMessageListenerContainer
 *//*

    @Bean("redisMessageListenerContainer")
    RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);


        // 添加指定通道使用。比如：全体人员通知  container.addMessageListener(listenerAdapter, new PatternTopic(REDIS_CHANNEL));

        container.setTaskExecutor(redisTaskExecutor());
        return container;
    }

    */
/**
 * 创建线程池
 * 用于redis消息监听
 *
 * @return ThreadPoolTaskExecutor
 *//*

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

    */
/**
 * 监听器1，监听频道：read-msg
 *
 * @param redisReceiver RedisReceiverChannel
 * @return MessageListenerAdapter
 *//*

    @Bean("listenerAdapter")
    MessageListenerAdapter listenerAdapter(RedisReceiverChannel redisReceiver) {
        return new MessageListenerAdapter(redisReceiver, "onMessage");
    }

    */
/**
 * 初始化redis操作模板
 *
 * @param connectionFactory RedisConnectionFactory
 * @return StringRedisTemplate
 *//*

    @Bean
    StringRedisTemplate stringRedisTemplate(RedisConnectionFactory connectionFactory) {
        return new StringRedisTemplate(connectionFactory);
    }
}
*/
