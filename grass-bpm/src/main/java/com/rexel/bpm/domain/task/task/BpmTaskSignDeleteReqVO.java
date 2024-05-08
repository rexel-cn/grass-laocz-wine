package com.rexel.bpm.domain.task.task;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class BpmTaskSignDeleteReqVO {

    /**
     * 被减签的任务编号
     */
    @NotEmpty(message = "任务编号不能为空")
    private String id;

    /**
     * 加签原因
     */
    @NotEmpty(message = "加签原因不能为空")
    private String reason;

}
