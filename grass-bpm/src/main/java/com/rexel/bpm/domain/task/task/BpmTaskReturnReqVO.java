package com.rexel.bpm.domain.task.task;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class BpmTaskReturnReqVO {

    /**
     * 任务编号
     */
    @NotEmpty(message = "任务编号不能为空")
    private String id;

    /**
     * 回退到的任务 Key
     */
    @NotEmpty(message = "回退到的任务 Key 不能为空")
    private String targetTaskDefinitionKey;

    /**
     * 回退意见
     */
    @NotEmpty(message = "回退意见不能为空")
    private String reason;

}
