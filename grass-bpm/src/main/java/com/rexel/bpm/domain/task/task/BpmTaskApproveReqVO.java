package com.rexel.bpm.domain.task.task;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Collection;
import java.util.Map;

@Data
public class BpmTaskApproveReqVO {

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

    /**
     * 抄送的用户编号数组
     */
    private Collection<Long> copyUserIds;

    /**
     * 变量实例（动态表单）
     */
    private Map<String, Object> variables;

}
