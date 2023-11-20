package com.rexel.system.domain.dto;

import lombok.Data;


/**
 * @ClassName GrassPointUserStarDTO
 * @Description 资产设备测点置顶
 * @Author 孟开通
 * @Date 2022/10/21 11:32
 **/
@Data
public class GrassPointUserStarDTO {
    /**
     * 资产设备id
     */
    private String assetId;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 测点唯一标识
     */
    private String pointPrimaryKey;
}
