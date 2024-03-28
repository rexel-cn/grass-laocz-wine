package com.rexel.laocz.domain.dto;

import com.rexel.laocz.domain.vo.WeighingTankAddVo;
import lombok.Data;

import java.util.List;

@Data
public class PumpAddDto {
    /**
     * 泵主键Id;
     */
    private Long pumpId;
    /**
     * 防火区Id
     */
    private Long fireZoneId;
    /**
     * 泵编号
     */
    private String pumpNumber;
    /**
     * 区域名称
     */
    private String areaName;

    /**
     * 防火区名称
     */
    private String fireZoneName;

    /**
     * 场区Id
     */
    private Long areaId;

    /**
     * 动态列
     */
    private List<WeighingTankAddVo> weighingTankAddVos;

}
