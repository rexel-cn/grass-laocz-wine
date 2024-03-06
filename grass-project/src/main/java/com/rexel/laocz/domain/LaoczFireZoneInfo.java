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
 * 防火区信息对象 laocz_fire_zone_info
 *
 * @author grass-service
 * @date 2024-03-06
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LaoczFireZoneInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 防火区主键ID
     */
    private Long fireZoneId;
    /**
     * 租户ID
     */
    @Excel(name = "租户ID")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String tenantId;
    /**
     * 场区信息ID
     */
    @Excel(name = "场区信息ID")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long areaId;
    /**
     * 防火区名称
     */
    @Excel(name = "防火区名称")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String fireZoneName;
    /**
     * 显示顺序
     */
    @Excel(name = "显示顺序")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long dispalyOrder;


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("fireZoneId", getFireZoneId())
                .append("tenantId", getTenantId())
                .append("areaId", getAreaId())
                .append("fireZoneName", getFireZoneName())
                .append("dispalyOrder", getDispalyOrder())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
