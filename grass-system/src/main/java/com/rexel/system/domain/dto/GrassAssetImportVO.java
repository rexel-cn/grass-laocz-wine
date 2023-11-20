package com.rexel.system.domain.dto;

import com.rexel.common.annotation.Excel;
import lombok.Data;

/**
 * 资产对象 grass_asset
 * 导入vo
 * @author grass-service
 * @date 2022-08-09
 */
@Data
public class GrassAssetImportVO  {

    private static final long serialVersionUID = 1L;

    /**
     *
     */
  //  @Excel(name = "上级资产")
   // private String parentName;
    /**
     * 资产名称
     */
    @Excel(name = "资产名称")
    private String assetName;

    @Excel(name = "资产类型")
    private String assetTypeName;

    @Excel(name = "资产标签key")
    private String tagKey;

    @Excel(name = "资产标签value")
    private String tagValue;

    @Excel(name = "物联设备")
    private String deviceId;

    @Excel(name = "数据测点")
    private String pointId;

}
