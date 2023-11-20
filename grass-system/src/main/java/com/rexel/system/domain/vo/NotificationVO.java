package com.rexel.system.domain.vo;

import lombok.Data;

import java.util.List;

/**
 * 通知范围
 */
@Data
public class NotificationVO {
    /**
     * 通知类型
     */
    private String notificationType;
    /**
     * 通知类型名称
     */
    private String notificationTypeName;

    /**
     * 通知人员内容信息
     */
    private List<NotifyingOfficerVO> notifyingOfficerList;
}
