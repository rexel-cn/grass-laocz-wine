package com.rexel.bpm.domain.task.task;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class BpmTaskTransferReqVO {

    /**
     * 任务编号
     */
    @NotEmpty(message = "任务编号不能为空")
    private String id;

    /**
     * 新审批人的用户编号
     */
    @NotNull(message = "新审批人的用户编号不能为空")
    private Long assigneeUserId;

    /**
     * 转办原因
     */
    @NotEmpty(message = "转办原因不能为空")
    private String reason;

}
