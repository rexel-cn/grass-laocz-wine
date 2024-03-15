package com.rexel.laocz.domain;

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
 * 称重罐管理对象 laocz_weighing_tank
 *
 * @author grass-service
 * @date 2024-03-06
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LaoczWeighingTank extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 称重罐ID
     */

    @TableId(type = IdType.AUTO)
    private Long weighingTankId;
    /**
     * 租户ID
     */
    @Excel(name = "租户ID")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String tenantId;
    /**
     * 防火区主键ID，外键关联laocz_fire_zone_info
     */
    @Excel(name = "防火区主键ID，外键关联laocz_fire_zone_info")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long fireZoneId;
    /**
     * 称重罐编号
     */
    @Excel(name = "称重罐编号")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long weighingTankNumber;
    /**
     * 满罐上限值
     */
    @Excel(name = "满罐上限值")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String fullTankUpperLimit;


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("weighingTankId", getWeighingTankId())
                .append("tenantId", getTenantId())
                .append("fireZoneId", getFireZoneId())
                .append("weighingTankNumber", getWeighingTankNumber())
                .append("fullTankUpperLimit", getFullTankUpperLimit())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
