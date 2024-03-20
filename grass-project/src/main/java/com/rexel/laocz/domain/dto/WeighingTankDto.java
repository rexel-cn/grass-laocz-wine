package com.rexel.laocz.domain.dto;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.rexel.common.annotation.Excel;
import lombok.Data;

@Data
public class WeighingTankDto {
    /**
     * 称重罐编号
     */
    @Excel(name = "称重罐编号")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String weighingTankNumber;
    /**
     * 区域名称
     */
    @Excel(name = "归属区域")
    private String areaName;

    /**
     * 防火区名称
     */
    @Excel(name = "归属防火区")
    private String fireZoneName;
    /**
     * 满罐上限值
     */
    @Excel(name = "满罐上限值")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String fullTankUpperLimit;
    /**
     * 防火区主键ID，外键关联laocz_fire_zone_info
     */
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long fireZoneId;
}
