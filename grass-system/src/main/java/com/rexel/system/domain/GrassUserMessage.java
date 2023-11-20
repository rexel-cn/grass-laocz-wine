package com.rexel.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.rexel.common.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 用户站内信对象 grass_user_message
 *
 * @author grass-service
 * @date 2022-08-09
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GrassUserMessage {

    private static final long serialVersionUID = 1L;

    /**
     * 自增ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 租户ID
     */
    @Excel(name = "租户ID")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String tenantId;
    /**
     * 用户ID
     */
    @Excel(name = "用户ID")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String userId;
    /**
     * 0:设备告警,    看 com.rexel.gs.constant.ReportConstants
     */
    @Excel(name = "0:设备告警,")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long noticeType;
    /**
     * 发送内容
     */
    @Excel(name = "发送内容")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String sendContent;
    /**
     * 告警等级
     */
    @Excel(name = "告警等级")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String policeLevel;
    /**
     * 是否已读(0:未读;1:已读)
     */
    @Excel(name = "是否已读(0:未读;1:已读)")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long isRead;

    @TableField(fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NOT_EMPTY)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.UPDATE, updateStrategy = FieldStrategy.NOT_EMPTY)
    private Date updateTime;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("tenantId", getTenantId())
                .append("userId", getUserId())
                .append("noticeType", getNoticeType())
                .append("sendContent", getSendContent())
                .append("policeLevel", getPoliceLevel())
                .append("isRead", getIsRead())
                .append("createTime", getCreateTime())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
