package com.rexel.laocz.domain.dto;

import lombok.Data;

import java.util.Date;

/**
 * @ClassName TaskTodoPageDTO
 * @Description 待办事项分页 DTO
 * @Author 孟开通
 * @Date 2024/5/9 14:50
 **/
@Data
public class TaskPageDTO {
    /**
     * 申请开始时间
     */
    private Date beginTime;
    /**
     * 申请结束时间
     */
    private Date endTime;
    /**
     * 操作类型
     */
    private String operationType;
    /**
     * 酒液批次
     */
    private String liquorBatchId;


}
