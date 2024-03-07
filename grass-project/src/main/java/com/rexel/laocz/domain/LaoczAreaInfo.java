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
 * 场区信息对象 laocz_area_info
 *
 * @author grass-service
 * @date 2024-03-06
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LaoczAreaInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 场区信息ID
     */
    @TableId(type = IdType.AUTO)
    private Long areaId;
    /**
     * 租户ID
     */
    @Excel(name = "租户ID")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String tenantId;
    /**
     * 场区名称
     */
    @Excel(name = "场区名称")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String areaName;
    /**
     * 显示顺序
     */
    @Excel(name = "显示顺序")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long dispalyOrder;


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("areaId", getAreaId())
                .append("tenantId", getTenantId())
                .append("areaName", getAreaName())
                .append("dispalyOrder", getDispalyOrder())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
