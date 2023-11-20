package com.rexel.system.domain;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.rexel.common.annotation.Excel;
import com.rexel.common.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;

/**
 * 资产类型对象 grass_asset_type
 *
 * @author grass-service
 * @date 2022-07-21
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GrassAssetType extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(type = IdType.INPUT)
    private String id;
    /**
     * 祖级列表
     */
    private String ancestors = "0";
    /**
     * 父id
     */
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String parentId = "0";
    /**
     * 资产类型名称
     */
    @Excel(name = "资产类型名称")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String assetTypeName;
    /**
     * 资产等级
     */
    @Excel(name = "资产等级")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long assetGrade;
    /**
     * 资产等级描述
     */
    @TableField(exist = false)
    private String assetGradeDesc;
    /**
     * icon
     */
    @Excel(name = "icon")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String icon;
    /**
     * 租户ID
     */
    @Excel(name = "租户ID")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String tenantId;

    /**
     * 资产数量
     */
    @TableField(exist = false)
    private Integer AssetCount;

    @TableField(exist = false)
    private String dictValue;

    @TableField(exist = false)
    private List<GrassAssetType> children = new ArrayList<>();


    public GrassAssetType(String assetTypeName) {
        this.assetTypeName = assetTypeName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("assetTypeName", getAssetTypeName())
                .append("assetGrade", getAssetGrade())
                .append("icon", getIcon())
                .append("tenantId", getTenantId())
                .append("remark", getRemark())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }

}
