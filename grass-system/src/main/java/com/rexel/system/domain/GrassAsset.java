package com.rexel.system.domain;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.rexel.common.annotation.Excel;
import com.rexel.common.core.domain.BaseEntity;
import com.rexel.common.core.domain.entity.GrassAssetPoint;
import lombok.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;
import java.util.List;

/**
 * 资产对象 grass_asset
 *
 * @author grass-service
 * @date 2022-07-19
 */
@EqualsAndHashCode(callSuper = true)
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GrassAsset extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(type = IdType.INPUT)
    private String id;
    /**
     * 资产类型
     */
    @Excel(name = "资产设备类型")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String assetTypeId;
    /**
     * 资产名称
     */
    @Excel(name = "资产设备名称")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String assetName;
    /**
     * 资产设备英文名称
     */
    @Excel(name = "资产设备英文名称")
    private String assetEngName;
    /**
     * 资产设备状态：0使用中，1已报废
     */
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String assetStatus;
    @Excel(name = "资产设备状态")
    @TableField(exist = false)
    private String assetStatusDesc;
    /**
     * 资产设备型号
     */
    @Excel(name = "资产设备型号")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String assetModel;
    /**
     * 资产设备厂家
     */
    @Excel(name = "资产设备厂家")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String assetManufacturers;
    /**
     * 采购时间
     */
    @Excel(name = "采购时间", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date purchaseTime;
    /**
     * 租户ID
     */
    @Excel(name = "租户ID")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String tenantId;
    /**
     * 关联测点
     */
    @TableField(exist = false)
    private List<GrassAssetPoint> pointList;
    /**
     * 关联测点数
     */
    @TableField(exist = false)
    private Integer pointCount;

    @TableField(exist = false)
    private Integer pageNum;

    @TableField(exist = false)
    private Integer pageSize;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("assetTypeId", getAssetTypeId())
                .append("assetName", getAssetName())
                .append("assetEngName", getAssetEngName())
                .append("assetStatus", getAssetStatus())
                .append("asset_model", getAssetModel())
                .append("assetManufacturers", getAssetManufacturers())
                .append("purchaseTime", getPurchaseTime())
                .append("tenantId", getTenantId())
                .append("remark", getRemark())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
