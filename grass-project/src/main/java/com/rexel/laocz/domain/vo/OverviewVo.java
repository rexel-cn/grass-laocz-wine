package com.rexel.laocz.domain.vo;

import lombok.Data;

@Data
public class OverviewVo extends BoardDataVO {
    /**
     * 区域名称
     */
    private String areaName;
    /**
     * 防火区名称
     */
    private String fireZoneName;
}
