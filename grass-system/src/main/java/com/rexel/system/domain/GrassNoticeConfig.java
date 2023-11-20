package com.rexel.system.domain;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.rexel.common.annotation.Excel;
import com.rexel.common.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 通知配置主对象 grass_notice_config
 *
 * @author grass-service
 * @date 2022-07-29
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GrassNoticeConfig extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 自增ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 推送类型(邮件、站内信、短信....)
     */
    @Excel(name = "推送类型(邮件、站内信、短信....)")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String pushType;
    /**
     * 是否开通(0:未开通;1:开通)
     */
    @Excel(name = "是否开通(0:未开通;1:开通)")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String isOpen;
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
                .append("pushType", getPushType())
                .append("isOpen", getIsOpen())
                .append("tenantId", getTenantId())
                .append("remark", getRemark())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
