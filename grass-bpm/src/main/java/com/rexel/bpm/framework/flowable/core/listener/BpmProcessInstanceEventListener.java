package com.rexel.bpm.framework.flowable.core.listener;

import com.google.common.collect.ImmutableSet;
import com.rexel.bpm.service.task.BpmProcessInstanceService;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEntityEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEventType;
import org.flowable.engine.delegate.event.AbstractFlowableEngineEventListener;
import org.flowable.engine.delegate.event.FlowableCancelledEvent;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;

/**
 * 监听 {@link ProcessInstance} 的状态变更，更新其对应的 status 状态
 *
 * @author jason
 */
@Component
public class BpmProcessInstanceEventListener extends AbstractFlowableEngineEventListener {

    public static final Set<FlowableEngineEventType> PROCESS_INSTANCE_EVENTS = ImmutableSet.<FlowableEngineEventType>builder()
            .add(FlowableEngineEventType.PROCESS_CANCELLED)
            .add(FlowableEngineEventType.PROCESS_COMPLETED)
            .build();
    @Resource
    @Lazy
    private BpmProcessInstanceService processInstanceService;

    public BpmProcessInstanceEventListener() {
        super(PROCESS_INSTANCE_EVENTS);
    }

    /**
     * 取消时调用
     * @param event
     */
    @Override
    protected void processCancelled(FlowableCancelledEvent event) {
        processInstanceService.updateProcessInstanceWhenCancel(event);
    }

    /**
     * 完成时调用
     * @param event
     */
    @Override
    protected void processCompleted(FlowableEngineEntityEvent event) {
        processInstanceService.updateProcessInstanceWhenApprove((ProcessInstance) event.getEntity());
    }

}
