package com.rexel.system.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName GrassAssetQueryDTO
 * @Description
 * @Author 孟开通
 * @Date 2022/10/14 17:34
 **/
@Data
public class GrassAssetQueryDTO implements Serializable {
    private String assetTypeId;
    private String assetName;
    private String assetStatus;
    private String tenantId;
    private Integer pageNum;
    private Integer pageSize;

    public Integer getPageNum() {
        if (pageNum != null && pageSize != null) {
            return (pageNum - 1) * pageSize;
        }
        return null;

    }
}
