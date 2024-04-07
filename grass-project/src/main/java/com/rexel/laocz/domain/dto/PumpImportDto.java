package com.rexel.laocz.domain.dto;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.rexel.common.annotation.Excel;
import lombok.Data;

@Data
public class PumpImportDto {
    /**
     * 泵编号
     */
    @Excel(name = "泵编号")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String pumpNumber;
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
}
