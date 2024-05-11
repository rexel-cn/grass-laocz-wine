package com.rexel.laocz.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName TaskPageVO
 * @Description 待办事项分页 VO
 * @Author 孟开通
 * @Date 2024/5/9 14:39
 **/
@Data
public class TaskPageVO {
    /**
     * 任务编号
     */
    private String taskId;
    /**
     * 申请时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date applyTime;
    /**
     * 申请人
     */
    private String applyUser;
    /**
     * 操作类型
     */
    private String operationType;
    /**
     * 酒品名称
     */
    private String liquorName;
    /**
     * 酒品批次
     */
    private String liquorBatchId;

    /**
     * 审批状态
     */
    private Integer status; // 参见 BpmTaskStatusEnum 枚举

    /**
     * 流程实例id
     */
    String processInstanceId;

}
