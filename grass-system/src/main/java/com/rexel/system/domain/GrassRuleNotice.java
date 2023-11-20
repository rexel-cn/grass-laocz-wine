package com.rexel.system.domain;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.rexel.common.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * 报警规则关联通知模板对象 grass_rule_notice
 *
 * @author grass-service
 * @date 2022-08-05
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GrassRuleNotice implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 规则id
     */
    @Excel(name = "规则id")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long rulesId;
    /**
     * 通知模板id
     */
    @Excel(name = "通知模板id")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long noticeTemplateId;
    /**
     * 租户ID
     */
    @Excel(name = "租户ID")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String tenantId;
    /**
     * 通知模板名称
     */
    @TableField(exist = false)
    private String noticeTemplateName;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("rulesId", getRulesId())
                .append("noticeTemplateId", getNoticeTemplateId())
                .append("tenantId", getTenantId())
                .toString();
    }
}
