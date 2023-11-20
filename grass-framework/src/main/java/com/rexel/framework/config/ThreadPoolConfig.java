package com.rexel.framework.config;

import com.alibaba.ttl.threadpool.TtlExecutors;
import com.rexel.common.utils.Threads;
import com.rexel.tenant.context.TenantContextHolder;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.security.concurrent.DelegatingSecurityContextRunnable;

import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池配置
 *
 * @author ids-admin
 **/
@Configuration
public class ThreadPoolConfig {
    // 核心线程池大小
    private int corePoolSize = 50;

    // 最大可创建的线程数
    private int maxPoolSize = 200;

    // 队列最大长度
    private int queueCapacity = 1000;

    // 线程池维护线程所允许的空闲时间
    private int keepAliveSeconds = 300;

    @Bean(name = "threadPoolTaskExecutor")
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setMaxPoolSize(maxPoolSize);
        executor.setCorePoolSize(corePoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setKeepAliveSeconds(keepAliveSeconds);
        // 线程池对拒绝任务(无线程可用)的处理策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //用逻辑包装一个委托，用于在调用委托之前 Runnable设置 a ，然后在委托完成后删除
        //登录用户信息传递（主线程子线程传递）
        executor.setTaskDecorator(DelegatingSecurityContextRunnable::new);
        return executor;
    }

    @Bean(name = "sendMessageExecutor")
    public ThreadPoolTaskExecutor sendMessageExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix("sendMessage线程池");
        //核心线程池大小
        executor.setCorePoolSize(corePoolSize);
        //最大线程数
        executor.setMaxPoolSize(maxPoolSize);
        //队列容量
        executor.setQueueCapacity(queueCapacity);
        //活跃时间
        executor.setKeepAliveSeconds(keepAliveSeconds);
        //线程名字前缀
        executor.setThreadNamePrefix("sendMessage-");
        // 线程池对拒绝任务(无线程可用)的处理策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }


    @Bean(name = "mqttMessageExecutor")
    public ThreadPoolTaskExecutor mqttMessageExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix("mqttMessage线程池");
        //核心线程池大小
        executor.setCorePoolSize(10);
        //最大线程数
        executor.setMaxPoolSize(50);
        //队列容量
        executor.setQueueCapacity(queueCapacity);
        //活跃时间
        executor.setKeepAliveSeconds(keepAliveSeconds);
        //线程名字前缀
        executor.setThreadNamePrefix("mqttMessage-");
        // 线程池对拒绝任务(无线程可用)的处理策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }

    @Bean(name = "threadPoolTaskScheduler")
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        ThreadPoolTaskScheduler executor = new ThreadPoolTaskScheduler();
        executor.setPoolSize(corePoolSize);
        executor.setThreadNamePrefix("taskExecutor-");
        executor.setWaitForTasksToCompleteOnShutdown(false);
        executor.setAwaitTerminationSeconds(5);
        return executor;
    }

    @Bean(name = "branchInquiryExecutor")
    public Executor querySqlExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix("branchInquiry线程池");
        //最大可创建的线程数
        int maxPoolSize = 150;
        executor.setMaxPoolSize(maxPoolSize);
        //核心线程池大小
        executor.setCorePoolSize(corePoolSize);
        //队列最大长度
        int queueCapacity = 1000;
        executor.setQueueCapacity(queueCapacity);
        //线程池维护线程所允许的空闲时间
        int keepAliveSeconds = 300;
        executor.setKeepAliveSeconds(keepAliveSeconds);
        // 线程池对拒绝任务(无线程可用)的处理策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //用逻辑包装一个委托，用于在调用委托之前 Runnable设置 a ，然后在委托完成后删除
        //登录用户信息传递（主线程子线程传递）
        executor.setTaskDecorator(DelegatingSecurityContextRunnable::new);
        return executor;
    }

    /**
     * 执行周期性或定时任务
     */
    @Bean(name = "ttlScheduledExecutorService")
    protected ScheduledExecutorService scheduledExecutorService() {
        return TtlExecutors.getTtlScheduledExecutorService(new ScheduledThreadPoolExecutor(corePoolSize,
                new BasicThreadFactory.Builder().namingPattern("schedule-pool-%d").daemon(true).build()) {
            @Override
            protected void afterExecute(Runnable r, Throwable t) {
                super.afterExecute(r, t);
                Threads.printException(r, t);
            }
        });
    }
}
