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
 * 短信-钉钉-邮件发送记录对象 grass_user_sms_mailbox_dingding
 *
 * @author grass-service
 * @date 2022-08-09
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GrassUserSmsMailboxDingding {

    private static final long serialVersionUID = 1L;

    /**
     * 自增ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 租户id
     */
    @Excel(name = "租户id")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String tenantId;
    /**
     * 发送用户id
     */
    @Excel(name = "发送用户id")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String userId;
    /**
     * 推送类型
     */
    @Excel(name = "推送类型")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String pushType;
    /**
     * 发送内容
     */
    @Excel(name = "发送内容")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String sendContent;
    /**
     * 发送结果(0:成功;1:失败)
     */
    @Excel(name = "发送结果(0:成功;1:失败)")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String sendRes;
    /**
     * 发送失败原因
     */
    @Excel(name = "发送失败原因")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String failReason;

    @TableField(fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NOT_EMPTY)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("tenantId", getTenantId())
                .append("userId", getUserId())
                .append("pushType", getPushType())
                .append("sendContent", getSendContent())
                .append("sendRes", getSendRes())
                .append("failReason", getFailReason())
                .append("createTime", getCreateTime())
                .toString();
    }
}
