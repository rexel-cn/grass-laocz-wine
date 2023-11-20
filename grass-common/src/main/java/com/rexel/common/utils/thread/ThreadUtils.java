package com.rexel.common.utils.thread;

import com.rexel.common.utils.spring.SpringUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * ClassName ThreadUtils
 * Description
 * Author 孟开通
 * Date 2022/7/20 14:43
 **/
public class ThreadUtils {
    private final ThreadPoolTaskExecutor executor = SpringUtils.getBean("threadPoolTaskExecutor");
    private static ThreadUtils me = new ThreadUtils();

    public static ThreadUtils me() {
        return me;
    }
    public void execute(Runnable  runnable) {
            executor.execute(runnable);
    }

}
