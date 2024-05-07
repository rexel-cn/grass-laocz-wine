package com.rexel.bpm.domain.task.instance;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class BpmProcessInstanceCancelReqVO {

    /**
     * 流程实例的编号
     */
    @NotEmpty(message = "流程实例的编号不能为空")
    private String id;

    /**
     * 取消原因
     */
    @NotEmpty(message = "取消原因不能为空")
    private String reason;

}
