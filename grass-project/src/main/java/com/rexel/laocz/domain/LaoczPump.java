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
 * 泵管理对象 laocz_pump
 *
 * @author grass-service
 * @date 2024-03-06
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LaoczPump extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 泵主键ID
     */
    private Long pumpId;
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
     * 泵编号
     */
    @Excel(name = "泵编号")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long pumpNumber;


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("pumpId", getPumpId())
                .append("tenantId", getTenantId())
                .append("fireZoneId", getFireZoneId())
                .append("pumpNumber", getPumpNumber())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
