package com.rexel.common.core.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.rexel.common.annotation.Excel;
import com.rexel.common.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


/**
 * @version V1.0
 * @package sys
 * @title: 租户管理控制器
 * @description: 租户管理控制器
 * @author: 未知
 * @date: 2019-11-28 06:24:52
 */

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@TableName("sys_tenant")
public class SysTenant extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 租户标识
     */
    @Excel(name = "租户标识")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String tenantId;

    @Excel(name = "租户状态", readConverterExp = "0=正常,1=停用")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Integer status;
    /**
     * 租户存储空间
     */
    @Excel(name = "租户存储空间")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String bucketId;
    /**
     * 租户logo
     */
    @Excel(name = "租户logo")
    @TableField(updateStrategy = FieldStrategy.NOT_NULL)
    private String logo;
    /**
     * 租户名称
     */
    @Excel(name = "租户名称")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String tenantName;
    /**
     * 英文名称
     */
    @Excel(name = "英文名称")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String engName;
    /**
     * 用户Id
     */
    @Excel(name = "用户Id")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long userId;
    /**
     * 经纬坐标
     */
    @Excel(name = "经纬坐标")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String coordinate;
    /**
     * 删除标识(0代表存在 2代表删除)
     */
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String delFlag;
    /**
     * 租户类型(超级管理员:0、平台管理员：1、普通租户：2)
     */
    @Excel(name = "租户类型(超级管理员:0、平台管理员：1、普通租户：2)")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String tenantType;
    /**
     * 租户父id（确定哪个租户创建的）
     */
    @Excel(name = "租户父id", readConverterExp = "确=定哪个租户创建的")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String parentTenantId;



    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("tenantId", getTenantId())
                .append("bucketId", getBucketId())
                .append("logo", getLogo())
                .append("tenantName", getTenantName())
                .append("engName", getEngName())
                .append("userId", getUserId())
                .append("coordinate", getCoordinate())
                .append("delFlag", getDelFlag())
                .append("tenantType", getTenantType())
                .append("parentTenantId", getParentTenantId())
                .append("remark", getRemark())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
