package com.rexel.system.domain.dto;

import com.rexel.system.domain.GrassAsset;
import lombok.Data;

/**
 * 资产设备添加修改DTO
 *
 * @author grass-service
 * @date 2022-10-14
 */
@Data
public class GrassAssetDTO {

    /**
     * 资产设备
     */
    private GrassAsset asset;
    /**
     * 测点集合
     */
    private Long[] pointIds;

}
