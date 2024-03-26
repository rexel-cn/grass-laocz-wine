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
 * 酒操作业务历史对象 laocz_wine_operations_his
 *
 * @author grass-service
 * @date 2024-03-26
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LaoczWineOperationsHis extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Long wineOperationsId;
    /**
     * 业务标识
     */
    @Excel(name = "业务标识")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String busyId;
    /**
     * 租户ID
     */
    @Excel(name = "租户ID")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String tenantId;
    /**
     * 申请的工单id
     */
    @Excel(name = "申请的工单id")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String workOrderId;
    /**
     * 操作类型：1：入酒，2出酒，3倒坛，4取样
     */
    @Excel(name = "操作类型：1：入酒，2出酒，3倒坛，4取样")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long operationType;


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("wineOperationsId", getWineOperationsId())
                .append("busyId", getBusyId())
                .append("tenantId", getTenantId())
                .append("workOrderId", getWorkOrderId())
                .append("operationType", getOperationType())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
