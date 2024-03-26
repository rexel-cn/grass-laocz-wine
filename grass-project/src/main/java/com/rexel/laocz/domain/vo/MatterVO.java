package com.rexel.laocz.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName MatterVO
 * @Description 移动端我的事项
 * @Author 孟开通
 * @Date 2024/3/13 09:29
 **/
@Data
public class MatterVO {

    /**
     * 酒操作业务表 主键
     */
    private Long wineOperationsId;

    /**
     * 工单id
     */
    private String workOrderId;
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
     * 操作类型编号
     */
    private Long operationTypeNumber;

    /**
     * 审批结果
     */
    private String approvalResult;
}
