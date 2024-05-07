package com.rexel.bpm.domain.task.task;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class BpmTaskDelegateReqVO {

    /**
     * 任务编号
     */
    @NotEmpty(message = "任务编号不能为空")
    private String id;

    /**
     * 被委派人 ID
     */
    @NotNull(message = "被委派人 ID 不能为空")
    private Long delegateUserId;

    /**
     * 委派原因
     */
    @NotEmpty(message = "委派原因不能为空")
    private String reason;

}
