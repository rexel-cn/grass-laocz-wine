package com.rexel.bpm.domain.task.task;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class BpmTaskRejectReqVO {

    /**
     * 任务编号
     */
    @NotEmpty(message = "任务编号不能为空")
    private String id;

    /**
     * 审批意见
     */
    @NotEmpty(message = "审批意见不能为空")
    private String reason;

}
