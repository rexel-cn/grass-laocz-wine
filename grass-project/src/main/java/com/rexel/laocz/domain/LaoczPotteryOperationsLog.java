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
 * 操作记录汇总（方便查询）对象 laocz_pottery_operations_log
 *
 * @author grass-service
 * @date 2024-03-06
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LaoczPotteryOperationsLog extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long operationId;
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
     * 陶坛ID
     */
    @Excel(name = "陶坛ID")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long potteryAltarId;
    /**
     * 酒液批次号，时间戳生成
     */
    @Excel(name = "酒液批次号，时间戳生成")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String liquorBatchId;
    /**
     * 操作类型：1：入酒，2出酒，3倒坛，4取样
     */
    @Excel(name = "操作类型：1：入酒，2出酒，3倒坛，4取样")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long operationType;
    /**
     * 申请重量
     */
    @Excel(name = "申请重量")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long applyWeight;
    /**
     * 操作重量 （称重罐重量）
     */
    @Excel(name = "操作重量 ", readConverterExp = "称=重罐重量")
    private Long operatingWeight;
    /**
     * 剩余重量
     */
    @Excel(name = "剩余重量")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long remainingWeight;
    /**
     * 亏损重量
     */
    @Excel(name = "亏损重量")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long lossWeight;
    /**
     * 场区名称
     */
    @Excel(name = "场区名称")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String areaName;
    /**
     * 防火区名称
     */
    @Excel(name = "防火区名称")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String fireZoneName;
    /**
     * 陶坛管理编号
     */
    @Excel(name = "陶坛管理编号")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String potteryAltarNumber;


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("operationId", getOperationId())
                .append("tenantId", getTenantId())
                .append("workOrderId", getWorkOrderId())
                .append("potteryAltarId", getPotteryAltarId())
                .append("liquorBatchId", getLiquorBatchId())
                .append("operationType", getOperationType())
                .append("applyWeight", getApplyWeight())
                .append("operatingWeight", getOperatingWeight())
                .append("remainingWeight", getRemainingWeight())
                .append("lossWeight", getLossWeight())
                .append("areaName", getAreaName())
                .append("fireZoneName", getFireZoneName())
                .append("potteryAltarNumber", getPotteryAltarNumber())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
