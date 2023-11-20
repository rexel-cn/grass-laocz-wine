package com.rexel.system.domain.vo;

import com.rexel.system.domain.GrassAssetType;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName GrassAssetTypeTreeVO
 * @Description
 * @Author 孟开通
 * @Date 2022/10/14 09:44
 **/
@Data
public class GrassAssetTypeTreeVO implements Serializable {

    /**
     * 主键id
     */
    private String id;
    /**
     * 资产类型名称
     */
    private String assetTypeName;

    private Integer count;

    private List<GrassAssetTypeTreeVO> children = new ArrayList<>();

    public GrassAssetTypeTreeVO() {

    }

    public GrassAssetTypeTreeVO(GrassAssetType grassAssetType) {
        this.id = grassAssetType.getId();
        this.assetTypeName = grassAssetType.getAssetTypeName();
        this.count = grassAssetType.getAssetCount();
        this.children = grassAssetType.getChildren().stream().map(GrassAssetTypeTreeVO::new).collect(Collectors.toList());
    }
}
