package com.rexel.system.domain;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.rexel.common.annotation.Excel;
import com.rexel.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 报警规则对象 grass_rules_info
 *
 * @author grass-service
 * @date 2022-08-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GrassRulesInfo extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 自增ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 租户ID
     */
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String tenantId;
    /**
     * 资产设备id
     */
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String assetId;
    /**
     * 规则标识
     */
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String rulesUnique;
    /**
     * 规则名称
     */
    @Excel(name = "规则名称")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String rulesName;
    /**
     * 设备ID
     */
    @Excel(name = "设备ID")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String deviceId;
    /**
     * 测点ID
     */
    @Excel(name = "测点ID")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String pointId;
    /**
     * 判断条件
     */
    @Excel(name = "判断条件")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String pointJudge;
    /**
     * 测点值
     */
    @Excel(name = "测点值")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private BigDecimal pointValue;
    /**
     * 沉默周期
     */
    @Excel(name = "沉默周期")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String silentCycle;
    /**
     * 生效开始时间
     */
    @Excel(name = "生效开始时间")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String startTime;
    /**
     * 生效结束时间
     */
    @Excel(name = "生效结束时间")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String endTime;
    /**
     * 报警级别
     */
    @Excel(name = "报警级别")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String rulesLevel;
    /**
     * 报警分类
     */
    @Excel(name = "报警分类")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String rulesType;
    /**
     * 是否生效
     */
    @Excel(name = "是否生效")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String isEnable;
    /**
     * 是否为模板
     */
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Boolean isTemplate;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("tenantId", getTenantId())
                .append("rulesUnique", getRulesUnique())
                .append("rulesName", getRulesName())
                .append("deviceId", getDeviceId())
                .append("pointId", getPointId())
                .append("pointJudge", getPointJudge())
                .append("pointValue", getPointValue())
                .append("silentCycle", getSilentCycle())
                .append("startTime", getStartTime())
                .append("endTime", getEndTime())
                .append("rulesLevel", getRulesLevel())
                .append("rulesType", getRulesType())
                .append("isEnable", getIsEnable())
                .append("isTemplate", getIsTemplate())
                .append("remark", getRemark())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
