package com.rexel.system.domain;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rexel.common.annotation.Excel;
import com.rexel.common.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 通知钉钉配置对象 grass_notice_dingding
 *
 * @author grass-service
 * @date 2022-07-29
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GrassNoticeDingding extends BaseEntity {

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
     * 名称
     */
    @Excel(name = "名称")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String name;
    /**
     * 机器人webhook地址
     */
    @Excel(name = "机器人webhook地址")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String webhook;
    /**
     * 秘钥(钉钉机器人页面配置获取)
     */
    @Excel(name = "秘钥(钉钉机器人页面配置获取)")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String secret;
    /**
     * 租户ID
     */
    @Excel(name = "租户ID")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    @JsonIgnore
    private String tenantId;


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("noticeConfigId", getNoticeConfigId())
                .append("name", getName())
                .append("webhook", getWebhook())
                .append("secret", getSecret())
                .append("tenantId", getTenantId())
                .append("remark", getRemark())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
