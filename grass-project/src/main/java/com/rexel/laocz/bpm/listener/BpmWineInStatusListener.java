package com.rexel.laocz.bpm.listener;

import com.rexel.bpm.framework.flowable.core.event.BpmProcessInstanceStatusEvent;
import com.rexel.bpm.framework.flowable.core.event.BpmProcessInstanceStatusEventListener;
import com.rexel.laocz.enums.WineProcessDefinitionKeyEnum;
import com.rexel.laocz.service.ILaoczWineOperationsService;
import com.rexel.laocz.service.WineEntryApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ClassName BpmWineStatusListener
 * @Description 入酒结束监听器
 * @Author 孟开通
 * @Date 2024/5/8 10:40
 **/
@Component
public class BpmWineInStatusListener extends BpmProcessInstanceStatusEventListener {
    @Autowired
    private ILaoczWineOperationsService iLaoczWineOperationsService;

    /**
     * @return 返回监听的流程定义 Key
     */
    @Override
    protected String getProcessDefinitionKey() {
        return WineProcessDefinitionKeyEnum.IN_WINE.getKey();
    }

    /**
     * 处理事件
     *
     * @param event 事件
     */
    @Override
    protected void onEvent(BpmProcessInstanceStatusEvent event) {
        iLaoczWineOperationsService.confirmApprovalStatus(event.getBusinessKey(),event.getStatus());
    }
}
