package com.rexel.common.utils;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.rexel.common.utils.spring.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 多线程事务工具类
 *
 * @Date 2022/6/23 2:40 PM
 * @Author 孟开通
 */
@Slf4j
public class ExecutorTransactionUtil {
    //事务控制管理器
    private static final DataSourceTransactionManager dataSourceTransactionManager = SpringUtils.getBean(DataSourceTransactionManager.class);
    private static final ThreadPoolTaskExecutor threadPoolExecutor = SpringUtils.getBean("threadPoolTaskExecutor");
    /**
     * 多线程执行
     *
     * @param dataList  数据列表
     * @param sFunction 执行操作
     * @param <T>       入参
     * @param <R>       出参
     */
    public static <T, R extends Boolean> void execute(List<T> dataList, SFunction<List<T>, R> sFunction) {
        TimeInterval timer = DateUtil.timer();
        int threadNum = getThreadNum(dataList.size());
        //记录线程是否有异常
        AtomicBoolean isError = new AtomicBoolean(false);
        //子线程执行监控
        CountDownLatch childMonitor = new CountDownLatch(threadNum);
        //主线程执行监控
        CountDownLatch mainMonitor = new CountDownLatch(1);

        //计算每个线程池处理的数据量
        final int dataPartitionLength = (dataList.size() + threadNum - 1) / threadNum;
        //数据分区
        List<List<T>> lists = ListUtil.split(dataList, dataPartitionLength);
        //开启threadNum个线程执行任务
        for (int i = 0; i < threadNum; i++) {
            int finalI = i;
            threadPoolExecutor.execute(() -> {
                //线程id
                long threadId = Thread.currentThread().getId();
                try {
                    //自定义事务,一定要放到子线程中定义
                    // 获取事务定义
                    DefaultTransactionDefinition def = new DefaultTransactionDefinition();
                    // 设置事务隔离级别，创建一个新的事务并挂起当前事务
                    def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
                    // 得到事务状态
                    TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(def);

                    log.info("ExecutorTransactionUtil.execute: 第:{}个线程:{},开始执行", finalI, threadId);
                    //执行业务
                    boolean isOk = sFunction.apply(lists.get(finalI));
                    if (!isOk) {
                        log.info("ExecutorTransactionUtil.execute: 第:{}个线程:{},执行业务失败", finalI, threadId);
                        isError.set(true);
                    }
                    //业务执行结束
                    childMonitor.countDown();
                    log.info("ExecutorTransactionUtil.execute: 第:{}个线程:{},等待其他线程结束,判定事务提交", finalI, threadId);
                    mainMonitor.await();
                    if (isError.get()) {
                        dataSourceTransactionManager.rollback(transactionStatus);
                        log.info("ExecutorTransactionUtil.execute: 第:{}个线程:{},有线程出现异常,回滚操作", finalI, threadId);
                    } else {
                        dataSourceTransactionManager.commit(transactionStatus);
                        log.info("ExecutorTransactionUtil.execute: 第:{}个线程:{},提交事务", finalI, threadId);
                    }
                } catch (Exception e) {
                    childMonitor.countDown();
                    isError.set(true);
                    log.error("ExecutorTransactionUtil.execute 第:{}个线程:{},异常: ", finalI, threadId, e);
                } finally {
                    log.info("ExecutorTransactionUtil.execute: 第:{}个线程:{},线程执行完成", finalI, threadId);
                }
            });
        }

        try {
            //主线程等待所有子线程执行完毕
            childMonitor.await();
            //子线程全部完毕后,主线程释放,执行事务操作
            mainMonitor.countDown();
        } catch (Exception e) {
            log.error("ExecutorTransactionUtil.execute 异常: ", e);
        }
        log.info("ExecutorTransactionUtil.execute: 所有线程执行完成,总耗时:{}ms", timer.interval());
    }

    /**
     * 根据数据大小设置线程数
     *
     * @param size
     * @return
     */
    public static int getThreadNum(int size) {
        int maxPoolSize = threadPoolExecutor.getMaxPoolSize();
        int threadNum = size / 1000;
        if (threadNum <= 0) {
            threadNum = 1;
        }
        if (threadNum >= maxPoolSize) {
            threadNum = maxPoolSize;
        }
        return threadNum;
    }
}
