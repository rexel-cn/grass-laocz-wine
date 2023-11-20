package com.rexel.earlywarning.domain;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.rexel.system.domain.GrassDeviceInfo;
import com.rexel.system.domain.GrassPointInfo;
import lombok.*;
import java.math.BigDecimal;
import java.util.List;

import com.baomidou.mybatisplus.annotation.TableField;
import com.rexel.common.core.domain.BaseEntity;

/**
 * 预警规则结束条件对象 grass_early_warning_finish
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
public class GrassEarlyWarningFinish extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 预警规则ID
     */
    private Long rulesId;
    /**
     * 条件编号
     */
    private String finishIndex;
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
    /**
     * 设备信息（前端显示用）
     */
    @TableField(exist = false)
    private List<GrassDeviceInfo> deviceInfoList;
    /**
     * 测点信息（前端显示用）
     */
    @TableField(exist = false)
    private List<GrassPointInfo> pointInfoList;
}