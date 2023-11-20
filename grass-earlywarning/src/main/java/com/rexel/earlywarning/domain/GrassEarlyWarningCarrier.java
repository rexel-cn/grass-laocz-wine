package com.rexel.earlywarning.domain;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.*;

/**
 * 预警规则运行载体对象 grass_early_warning_carrier
 *
 * @author grass-service
 * @date 2023-09-15
 */
@Builder
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class GrassEarlyWarningCarrier {
    private static final long serialVersionUID = 1L;

    /**
     * 自增ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 预警规则ID
     */
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long rulesId;
    /**
     * 物联设备ID
     */
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String deviceId;
    /**
     * 规则状态
     */
    @TableField(exist = false)
    private Long rulesState;
    /**
     * 物联设备名称
     */
    @TableField(exist = false)
    private String deviceName;
}
