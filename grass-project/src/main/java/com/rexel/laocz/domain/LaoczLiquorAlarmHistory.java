package com.rexel.laocz.domain;

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
 * 老村长酒存储时间报警历史对象 laocz_liquor_alarm_history
 *
 * @author grass-service
 * @date 2024-03-06
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LaoczLiquorAlarmHistory extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 报警主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long liquorAlarmHistoryId;
    /**
     * 租户ID
     */
    @Excel(name = "租户ID")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String tenantId;
    /**
     * 酒液批次报警ID
     */
    @Excel(name = "规则ID")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long liquorRuleId;
    /**
     * 陶坛ID，外键关联laocz_pottery_altar_management
     */
    @Excel(name = "陶坛ID，外键关联laocz_pottery_altar_management")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long potteryAltarId;
    /**
     * 规则名称
     */
    @Excel(name = "规则名称")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String liquorRuleName;
    /**
     * 酒批次ID，外键关联laocz_liquor_batch
     */
    @Excel(name = "酒批次ID，外键关联laocz_liquor_batch")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String liquorBatchId;
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
     * 报警值
     */
    @Excel(name = "报警值")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String alarmValue;
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


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("liquorAlarmHistoryId", getLiquorAlarmHistoryId())
                .append("tenantId", getTenantId())
                .append("liquorRuleId", getLiquorRuleId())
                .append("potteryAltarId", getPotteryAltarId())
                .append("liquorRuleName", getLiquorRuleName())
                .append("liquorRuleJudge", getLiquorRuleJudge())
                .append("liquorRuleThreshold", getLiquorRuleThreshold())
                .append("alarmValue", getAlarmValue())
                .append("liquorRuleNotifyUser", getLiquorRuleNotifyUser())
                .append("liquorRuleNotifyTemplate", getLiquorRuleNotifyTemplate())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
