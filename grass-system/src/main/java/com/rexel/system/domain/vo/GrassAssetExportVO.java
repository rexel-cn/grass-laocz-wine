package com.rexel.system.domain.vo;

import com.rexel.common.annotation.Excel;
import lombok.Data;

import java.util.Date;

/**
 * 资产设备导出
 *
 * @author grass-service
 * @date 2022-10-25
 */
@Data
public class GrassAssetExportVO {
    private String id;
    private String parentId;


    @Excel(name = "资产设备类型")
    private String assetTypeName;
    @Excel(name = "上级资产设备名称")
    private String parentAssetTypeName;
    @Excel(name = "资产设备名称")
    private String assetName;
    @Excel(name = "资产设备英文名称")
    private String assetEngName;
    @Excel(name = "资产设备状态", dictType = "asset_status")
    private String assetStatus;
    @Excel(name = "资产设备厂家")
    private String assetManufacturers;
    @Excel(name = "资产设备型号")
    private String assetModel;
    @Excel(name = "采购时间", dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date purchaseTime;
    @Excel(name = "物联设备编号")
    private String deviceId;
    @Excel(name = "测点编号")
    private String pointId;
}
