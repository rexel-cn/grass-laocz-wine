package com.rexel.system.domain;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.rexel.common.annotation.Excel;
import com.rexel.common.core.domain.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 测点信息(同步pulse)对象 grass_point_info
 *
 * @author grass-service
 * @date 2022-07-07
 */
@Data
public class GrassPointInfo extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 自增ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 租户ID
     */
    @Excel(name = "租户ID")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String tenantId;
    /**
     * 测点标识
     */
    @Excel(name = "测点标识")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String pointUnique;
    /**
     * 设备ID
     */
    @Excel(name = "设备ID")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String deviceId;
    /**
     * 设备名称
     */
    @Excel(name = "设备名称")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String deviceName;
    /**
     * 测点ID
     */
    @Excel(name = "测点ID")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String pointId;
    /**
     * 测点类型
     */
    @Excel(name = "测点类型")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String pointType;
    /**
     * 测点名称
     */
    @Excel(name = "测点名称")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String pointName;
    /**
     * 测点单位
     */
    @Excel(name = "测点单位")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String pointUnit;
    /**
     * 变量
     */
    @Excel(name = "变量")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String variable;
    /**
     * 是否生效
     */
    @Excel(name = "是否删除")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long isDelete;
    /**
     * 输入最小值
     */
    @Excel(name = "输入最小值")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Double inMin;
    /**
     * 输入最大值
     */
    @Excel(name = "输入最大值")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Double inMax;
    /**
     * 输出最小值
     */
    @Excel(name = "输出最小值")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Double outMin;
    /**
     * 输出最大值
     */
    @Excel(name = "输出最大值")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Double outMax;

    @TableField(exist = false)
    private String assetId;

    @TableField(exist = false)
    private Integer isAssociated;
    @TableField(exist = false)
    private String tagKey;
    @TableField(exist = false)
    private String tagValue;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("tenantId", getTenantId())
                .append("pointUnique", getPointUnique())
                .append("deviceId", getDeviceId())
                .append("deviceName", getDeviceName())
                .append("pointId", getPointId())
                .append("pointType", getPointType())
                .append("pointName", getPointName())
                .append("pointUnit", getPointUnit())
                .append("variable", getVariable())
                .append("isEnable", getIsDelete())
                .append("inMin", getInMin())
                .append("inMax", getInMax())
                .append("outMin", getOutMin())
                .append("outMax", getOutMax())
                .append("remark", getRemark())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
