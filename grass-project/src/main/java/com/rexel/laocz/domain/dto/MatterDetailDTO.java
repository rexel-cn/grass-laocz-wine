package com.rexel.laocz.domain.dto;

import lombok.Data;

/**
 * @ClassName MatterDetailDTO
 * @Description 操作详情DTO
 * @Author 孟开通
 * @Date 2024/4/12 18:04
 **/
@Data
public class MatterDetailDTO {
    /**
     * 酒操作业务表 主键
     */
    private Long wineOperationsId;
    /**
     * 区域id
     */
    private Long areaId;
    /**
     * 防火区Id
     */
    private Long fireZoneId;
}
