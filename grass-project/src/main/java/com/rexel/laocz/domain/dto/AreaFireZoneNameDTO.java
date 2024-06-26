package com.rexel.laocz.domain.dto;

import lombok.Data;

/**
 * @ClassName AreaFireZoneNameDTO
 * @Description 区域防火区名称DTO
 * @Author 孟开通
 * @Date 2024/6/26 09:34
 **/
@Data
public class AreaFireZoneNameDTO {
    /**
     * 区域名称
     */
    private String areaName;
    /**
     * 防火区名称
     */
    private String fireZoneName;
}
