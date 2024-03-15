package com.rexel.laocz.dview;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import static com.rexel.laocz.dview.DviewUtils.refreshCache;

/**
 * @ClassName DviewInit
 * @Description Dview 初始化
 * @Author 孟开通
 * @Date 2024/1/4 10:46
 **/
@Slf4j
@Component
public class DviewInit {
    @EventListener(ApplicationReadyEvent.class)
    @Async // 异步，保证项目的启动过程，毕竟非关键流程
    public void init() {
        try {
            log.info("[初始化 Dview 组件]");
            refreshCache();
        } catch (Exception e) {
            log.error("[初始化失败]：{}", e.getMessage());
            // 处理异常...
        }
    }
}
