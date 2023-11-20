package com.rexel.system.domain.vo;/**
 * @Author 董海
 * @Date 2022/7/20 15:38
 * @version 1.0
 */

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName GrassAssetVO
 * @Description GrassAssetVO
 * @Author Hai.Dong
 * @Date 2022/7/20 15:38
 **/
@Data
public class GrassAssetTreeVO {

    private Long id;

    private String assetName;
    private Long parentId;
    private String ancestors;
    private String assetTypeName;
    private String icon;
    private Integer count;


    private List<GrassAssetTreeVO> children = new ArrayList<GrassAssetTreeVO>();
}
