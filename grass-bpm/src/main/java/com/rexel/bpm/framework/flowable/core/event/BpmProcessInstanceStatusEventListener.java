package com.rexel.bpm.framework.flowable.core.event;

import cn.hutool.core.util.StrUtil;
import org.springframework.context.ApplicationListener;

/**
 * {@link BpmProcessInstanceStatusEvent} 的监听器
 *
 * @author 芋道源码
 */
public abstract class BpmProcessInstanceStatusEventListener
        implements ApplicationListener<BpmProcessInstanceStatusEvent> {

    @Override
    public final void onApplicationEvent(BpmProcessInstanceStatusEvent event) {
        String processDefinitionKey = getProcessDefinitionKey();

        if (!StrUtil.equals(event.getProcessDefinitionKey(), processDefinitionKey)) {
            return;
        }
        onEvent(event);
    }

    /**
     * @return 返回监听的流程定义 Key
     */
    protected abstract String getProcessDefinitionKey();

    /**
     * 处理事件
     *
     * @param event 事件
     */
    protected abstract void onEvent(BpmProcessInstanceStatusEvent event);

}
