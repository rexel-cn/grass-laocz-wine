package com.rexel.laocz.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName ProcessInstancePageVO
 * @Description
 * @Author 孟开通
 * @Date 2024/5/10 16:14
 **/
@Data
public class ProcessInstancePageVO {
    /**
     * 申请时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date applyTime;
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
