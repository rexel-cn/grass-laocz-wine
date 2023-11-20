package com.rexel.system.domain;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.rexel.common.annotation.Excel;
import com.rexel.common.core.domain.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 通知邮箱配置对象 grass_notice_mailbox
 *
 * @author grass-service
 * @date 2022-07-29
 */
@Data
public class GrassNoticeMailbox extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 自增ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 通知配置关联id
     */
    @Excel(name = "通知配置关联id")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long noticeConfigId;
    /**
     * 邮箱地址
     */
    @Excel(name = "邮箱地址")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String mailboxAddress;
    /**
     * 服务器密码
     */
    @Excel(name = "服务器密码")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String serverPassword;
    /**
     * SMTP服务器地址
     */
    @Excel(name = "SMTP服务器地址")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String smtpServerUrl;
    /**
     * SMTP服务器端口号
     */
    @Excel(name = "SMTP服务器端口号")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String smtpServerPort;
    /**
     * 租户ID
     */
    @Excel(name = "租户ID")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String tenantId;


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("noticeConfigId", getNoticeConfigId())
                .append("mailboxAddress", getMailboxAddress())
                .append("serverPassword", getServerPassword())
                .append("smtpServerUrl", getSmtpServerUrl())
                .append("smtpServerPort", getSmtpServerPort())
                .append("tenantId", getTenantId())
                .append("remark", getRemark())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
