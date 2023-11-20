package com.rexel.earlywarning.domain;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import lombok.*;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableField;
import com.rexel.common.core.domain.BaseEntity;

/**
 * 预警规则历史对象 grass_early_warning_his
 *
 * @author grass-service
 * @date 2023-10-17
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GrassEarlyWarningHis extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 自增ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 报警历史ID
     */
    private Long hisId;
    /**
     * 预警规则ID
     */
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long rulesId;
    /**
     * 规则名称
     */
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String rulesName;
    /**
     * 规则状态
     */
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long rulesState;
    /**
     * 预警级别
     */
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String rulesLevel;
    /**
     * 触发条件关系
     */
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String triggerRelation;
    /**
     * 触发条件执行频率
     */
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long triggerFrequency;
    /**
     * 判断条件关系
     */
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String judgeRelation;
    /**
     * 判断条件执行频率
     */
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long judgeFrequency;
    /**
     * 结束条件关系
     */
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String finishRelation;
    /**
     * 结束条件执行频率
     */
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String finishFrequency;
    /**
     * 触发条件类型
     */
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String judgeType;
    /**
     * 累加次数
     */
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long addUpCount;
    /**
     * 累加持续时间
     */
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long addUpDuration;
    /**
     * 累加单次最大时长
     */
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long addUpOnceMax;
    /**
     * 连续次数
     */
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long continuousCount;
    /**
     * 连续增长参数
     */
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private BigDecimal continuousIncrease;
    /**
     * 沉默周期
     */
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long silentCycle;
    /**
     * 生效开始时间
     */
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String startTime;
    /**
     * 生效结束时间
     */
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String endTime;
    /**
     * 处置建议ID
     */
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String suggestId;
    /**
     * 租户ID
     */
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String tenantId;
}
