package com.rexel.laocz.domain.vo;


import lombok.Data;

/**
 * @ClassName AreaFireZoneInfo
 * @Description 区域防火区信息
 * @Author 孟开通
 * @Date 2024/6/26 09:36
 **/
@Data
public class AreaFireZoneInfo {
    /**
     * 防火区id
     */
    private Long fireZoneId;
    /**
     * 防火区名称
     */
    private String fireZoneName;
    /**
     * 场区名称
     */
    private String areaName;
    /**
     * 被仓泵关联的数量
     */
    private Integer pumpCount;
}
