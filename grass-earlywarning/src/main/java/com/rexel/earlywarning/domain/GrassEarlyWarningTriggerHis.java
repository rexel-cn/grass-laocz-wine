package com.rexel.earlywarning.domain;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import lombok.*;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableField;
import com.rexel.common.core.domain.BaseEntity;

/**
 * 预警规则触发条件历史对象 grass_early_warning_trigger_his
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
public class GrassEarlyWarningTriggerHis extends BaseEntity {

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
     * 条件编号
     */
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String triggerIndex;
    /**
     * 物联设备ID
     */
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String deviceId;
    /**
     * 测点ID
     */
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String pointId;
    /**
     * 比较符号
     */
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String judge;
    /**
     * 触发值
     */
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private BigDecimal pointValue;
    /**
     * 租户ID
     */
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String tenantId;
}
