package com.rexel.system.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName GrassAssetPointTimeDataDTO
 * @Description 资产设备 测点实时数据查询
 * @Author 孟开通
 * @Date 2022/10/20 10:45
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GrassAssetPointDTO {
    /**
     * 设备id
     */
    private Long id;
    /**
     * 测点
     */
    private String pointId;
    /**
     * 测点 名
     */
    private String pointName;

    private Long userId;

    public GrassAssetPointDTO(Long id) {
    }
}
