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
 * 酒液批次相关信息对象 laocz_liquor_batch
 *
 * @author grass-service
 * @date 2024-03-06
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LaoczLiquorBatch extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 酒批次ID
     */
    private String liquorBatchId;
    /**
     * 申请的工单id
     */
    @Excel(name = "申请的工单id")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String workOrderId;
    /**
     * 租户ID
     */
    @Excel(name = "租户ID")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String tenantId;
    /**
     * 申请的重量
     */
    @Excel(name = "申请的重量")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Double applyWeight;
    /**
     * 酒品管理ID，外键关联laocz_liquor_management
     */
    @Excel(name = "酒品管理ID，外键关联laocz_liquor_management")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long liquorManagementId;


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("liquorBatchId", getLiquorBatchId())
                .append("workOrderId", getWorkOrderId())
                .append("tenantId", getTenantId())
                .append("applyWeight", getApplyWeight())
                .append("liquorManagementId", getLiquorManagementId())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
