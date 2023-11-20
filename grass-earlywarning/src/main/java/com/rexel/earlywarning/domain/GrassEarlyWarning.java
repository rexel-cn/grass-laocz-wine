package com.rexel.earlywarning.domain;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import lombok.*;
import java.util.List;

import com.baomidou.mybatisplus.annotation.TableField;
import com.rexel.common.core.domain.BaseEntity;

/**
 * 预警规则对象 grass_early_warning
 *
 * @author grass-service
 * @date 2023-09-15
 */
@Builder
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GrassEarlyWarning extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 自增ID
     */
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
     * 规则状态名称
     */
    @TableField(exist = false)
    private String rulesStateName;
    /**
     * 预警级别
     */
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String rulesLevel;
    /**
     * 预警级别名称
     */
    @TableField(exist = false)
    private String rulesLevelName;
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
    private Long finishFrequency;
    /**
     * 触发条件类型
     */
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String judgeType;
    /**
     * 累加次数
     */
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Integer addUpCount;
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
    private Integer continuousCount;
    /**
     * 连续增长参数
     */
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Double continuousIncrease;
    /**
     * 沉默周期
     */
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Integer silentCycle;
    /**
     * 沉默周期名称
     */
    @TableField(exist = false)
    private String silentCycleName;
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
     * 是否为模板
     */
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Boolean isTemplate;
    /**
     * 建议分类
     */
    @TableField(exist = false)
    private String suggestType;
    /**
     * 建议分类名称
     */
    @TableField(exist = false)
    private String suggestTypeName;
    /**
     * 建议标题
     */
    @TableField(exist = false)
    private String suggestTitle;
    /**
     * 建议内容
     */
    @TableField(exist = false)
    private String suggestContent;
    /**
     * 报警数量
     */
    @TableField(exist = false)
    private Integer alarmCount;
    /**
     * 租户ID
     */
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String tenantId;
    /**
     * 运行状态
     */
    @TableField(exist = false)
    private String jobStatus;
    /**
     * 运行设备
     */
    @TableField(exist = false)
    private List<GrassEarlyWarningCarrier> carrierList;
    /**
     * 触发条件
     */
    @TableField(exist = false)
    private List<GrassEarlyWarningTrigger> triggerList;
    /**
     * 判断条件
     */
    @TableField(exist = false)
    private List<GrassEarlyWarningJudge> judgeList;
    /**
     * 结束条件
     */
    @TableField(exist = false)
    private List<GrassEarlyWarningFinish> finishList;
    /**
     * 通知模板
     */
    @TableField(exist = false)
    private List<GrassEarlyWarningNotice> noticeList;
    /**
     * 通知模板ID列表
     */
    @TableField(exist = false)
    private List<Long> noticeTempIdList;
}
