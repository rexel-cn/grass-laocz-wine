package com.rexel.laocz.domain;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.rexel.common.annotation.Excel;
import com.rexel.common.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 称重罐相关测点维护对象 laocz_weighing_tank_point
 *
 * @author grass-service
 * @date 2024-03-06
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LaoczWeighingTankPoint extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 设备测点关联表
     */
    private Long equipmentPointId;
    /**
     * 租户ID
     */
    @Excel(name = "租户ID")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String tenantId;
    /**
     * 使用标识
     */
    @Excel(name = "使用标识")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String useMark;
    /**
     * 称重罐ID，外键关联laocz_weighing_tank
     */
    @Excel(name = "称重罐ID，外键关联laocz_weighing_tank")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long weighingTankId;
    /**
     * 点位主键id
     */
    @Excel(name = "点位主键id")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long pointPrimaryKey;


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("equipmentPointId", getEquipmentPointId())
                .append("tenantId", getTenantId())
                .append("useMark", getUseMark())
                .append("weighingTankId", getWeighingTankId())
                .append("pointPrimaryKey", getPointPrimaryKey())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
