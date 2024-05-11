package com.rexel.laocz.domain;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.rexel.common.annotation.Excel;
import com.rexel.common.core.domain.BaseEntity;
import liquibase.pro.packaged.I;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 酒操作业务对象 laocz_wine_operations
 *
 * @author grass-service
 * @date 2024-03-12
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class LaoczWineOperations extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
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
    /**
     * 审批结果:1:通过,2:不通过,3:取消,4:退回
     */
    @Excel(name = "审批结果:1:通过,2:不通过,3:取消,4:退回")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Integer approvalResults;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getWineOperationsId())
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
