package com.rexel.laocz.domain.dto;


import com.rexel.laocz.domain.vo.WeighingTankAddVo;
import lombok.Data;

import java.util.List;

@Data
public class WeighingTankAddDto {

    /**
     * 称重罐Id;
     */
    private Long weighingTankId;
    /**
     * 防火区Id
     */
    private Long fireZoneId;
    /**
     * 称重罐编号
     */
    private String weighingTankNumber;
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
     * 满罐上限值
     */
    private String fullTankUpperLimit;


    private List<WeighingTankAddVo> weighingTankAddVos;

    /**
     * 1左，2右
     */
    private String about;
}
