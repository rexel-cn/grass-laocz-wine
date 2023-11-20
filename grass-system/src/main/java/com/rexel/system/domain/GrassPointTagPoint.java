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

/**
 * 测点标签对象 grass_point_tag_point
 *
 * @author grass-service
 * @date 2022-08-25
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GrassPointTagPoint extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 自增ID
     */
    @TableId(type = IdType.INPUT)
    private String id;
    /**
     * 测点标签信息id
     */
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String pointTagInfoId;
    /**
     * 物联设备id
     */
    @Excel(name = "物联设备id")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String deviceId;
    /**
     * 测点标识
     */
    @Excel(name = "测点标识")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String pointId;
    /**
     * 测点类型
     */
    @Excel(name = "测点类型")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String pointType;
    /**
     * 租户ID
     */
    @Excel(name = "租户ID")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String tenantId;


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("deviceId", getDeviceId())
                .append("pointId", getPointId())
                .append("tenantId", getTenantId())
                .append("remark", getRemark())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
