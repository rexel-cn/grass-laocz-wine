package com.rexel.bpm.domain.task.activity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BpmActivityRespVO {

    /**
     * 流程活动的标识
     */
    private String key;
    /**
     *流程活动的类型
     */
    private String type;
    /**
     *流程活动的开始时间
     */
    private Long startTime;
    /**
     * 流程活动的结束时间
     */
    private Long endTime;

    /**
     * 关联的流程任务的编号
     */
    private String taskId; // 关联的流程任务，只有 UserTask 等类型才有

}
