package com.rexel.laocz.domain;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.rexel.common.annotation.Excel;
import com.rexel.common.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 酒液批次存储报警规则信息对象 laocz_liquor_rule_info
 *
 * @author grass-service
 * @date 2024-03-06
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LaoczLiquorRuleInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 酒液批次报警ID
     */
    private Long liquorRuleId;
    /**
     * 酒液批次ID
     */
    @Excel(name = "酒液批次ID")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long liquorBatchId;
    /**
     * 租户ID
     */
    @Excel(name = "租户ID")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String tenantId;
    /**
     * 规则名称
     */
    @Excel(name = "规则名称")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String liquorRuleName;
    /**
     * 是否启用，1启用，0停止
     */
    @Excel(name = "是否启用，1启用，0停止")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long liquorRuleEnable;
    /**
     * 规则判断条件
     */
    @Excel(name = "规则判断条件")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String liquorRuleJudge;
    /**
     * 报警阈值
     */
    @Excel(name = "报警阈值")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String liquorRuleThreshold;
    /**
     * 报警通知人员
     */
    @Excel(name = "报警通知人员")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String liquorRuleNotifyUser;
    /**
     * 报警通知模板
     */
    @Excel(name = "报警通知模板")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String liquorRuleNotifyTemplate;
    /**
     * 是否删除，0：不删除，1：删除
     */
    @Excel(name = "是否删除，0：不删除，1：删除")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long deleteFlag;


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("liquorRuleId", getLiquorRuleId())
                .append("liquorBatchId", getLiquorBatchId())
                .append("tenantId", getTenantId())
                .append("liquorRuleName", getLiquorRuleName())
                .append("liquorRuleEnable", getLiquorRuleEnable())
                .append("liquorRuleJudge", getLiquorRuleJudge())
                .append("liquorRuleThreshold", getLiquorRuleThreshold())
                .append("liquorRuleNotifyUser", getLiquorRuleNotifyUser())
                .append("liquorRuleNotifyTemplate", getLiquorRuleNotifyTemplate())
                .append("deleteFlag", getDeleteFlag())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
