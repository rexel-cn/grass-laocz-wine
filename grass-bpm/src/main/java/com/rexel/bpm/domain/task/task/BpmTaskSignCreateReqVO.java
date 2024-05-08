package com.rexel.bpm.domain.task.task;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Data
public class BpmTaskSignCreateReqVO {

    /**
     * 需要加签的任务编号
     */
    @NotEmpty(message = "任务编号不能为空")
    private String id;

    /**
     * 加签的用户编号
     */
    @NotEmpty(message = "加签用户不能为空")
    private Set<Long> userIds;

    /**
     * 加签类型
     */
    @NotEmpty(message = "加签类型不能为空")
    private String type; // 参见 BpmTaskSignTypeEnum 枚举

    /**
     * 加签原因
     */
    @NotEmpty(message = "加签原因不能为空")
    private String reason;

}
