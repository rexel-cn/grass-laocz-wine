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
    /**
     * 使用标识
     */
    @Excel(name = "使用标识")
    private String useMark;
    /**
     * 测点Id
     */
    @Excel(name = "测点Id")
    private String pointId;
    /**
     * 物联设备Id
     */
    @Excel(name = "物联设备Id")
    private String deviceId;
    /**
     * 测点类型
     */
    @Excel(name = "测点类型")
    private String pointType;

    /**
     * 1左，2右
     */
    @Excel(name = "左,右",readConverterExp = "1=左,2=右")
    private String about;
}
