package com.rexel.laocz.domain.vo;

import com.rexel.common.annotation.Excel;
import com.rexel.laocz.domain.LaoczWeighingTank;
import lombok.Data;

@Data
public class WeighingTankVo extends LaoczWeighingTank {

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

}
