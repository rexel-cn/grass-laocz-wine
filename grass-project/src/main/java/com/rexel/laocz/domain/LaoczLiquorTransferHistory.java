package com.rexel.laocz.domain;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.rexel.common.annotation.Excel;
import com.rexel.common.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 倒坛记录对象 laocz_liquor_transfer_history
 *
 * @author grass-service
 * @date 2024-03-06
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LaoczLiquorTransferHistory extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 倒坛主键ID
     */
    private Long liquorTransferId;
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
     * 酒批次ID，外键关联laocz_liquor_batch
     */
    @Excel(name = "酒批次ID，外键关联laocz_liquor_batch")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String liquorBatchId;
    /**
     * 来源 陶坛ID，外键关联laocz_pottery_altar_management
     */
    @Excel(name = "来源 陶坛ID，外键关联laocz_pottery_altar_management")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long sourcePotteryAltarId;
    /**
     * 目标 陶坛ID，外键关联laocz_pottery_altar_management
     */
    @Excel(name = "目标 陶坛ID，外键关联laocz_pottery_altar_management")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long targetPotteryAltarId;
    /**
     * 当前陶坛申请重量
     */
    @Excel(name = "当前陶坛申请重量")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long potteryAltarApplyWeight;
    /**
     * 来源称重罐ID
     */
    @Excel(name = "来源称重罐ID")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long sourceWeighingTank;
    /**
     * 来源泵id
     */
    @Excel(name = "来源泵id")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long sourcePumpId;
    /**
     * 来源称重罐重量
     */
    @Excel(name = "来源称重罐重量")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long sourceWeighingTankWeight;
    /**
     * 目标称重罐ID
     */
    @Excel(name = "目标称重罐ID")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long targetWeighingTank;
    /**
     * 目标泵id
     */
    @Excel(name = "目标泵id")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long targetPumpId;
    /**
     * 目标称重罐重量
     */
    @Excel(name = "目标称重罐重量")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long targetWeighingTankWeight;
    /**
     * 目标称重开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "目标称重开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date targetWeighingStartTime;
    /**
     * 目标称重结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "目标称重结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date targetWeighingEndTime;
    /**
     * 出酒时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "出酒时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date outLiquorTime;
    /**
     * 入酒时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "入酒时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date storingTime;
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
                .append("liquorTransferId", getLiquorTransferId())
                .append("workOrderId", getWorkOrderId())
                .append("tenantId", getTenantId())
                .append("liquorBatchId", getLiquorBatchId())
                .append("sourcePotteryAltarId", getSourcePotteryAltarId())
                .append("targetPotteryAltarId", getTargetPotteryAltarId())
                .append("potteryAltarApplyWeight", getPotteryAltarApplyWeight())
                .append("sourceWeighingTank", getSourceWeighingTank())
                .append("sourcePumpId", getSourcePumpId())
                .append("sourceWeighingTankWeight", getSourceWeighingTankWeight())
                .append("targetWeighingTank", getTargetWeighingTank())
                .append("targetPumpId", getTargetPumpId())
                .append("targetWeighingTankWeight", getTargetWeighingTankWeight())
                .append("targetWeighingStartTime", getTargetWeighingStartTime())
                .append("targetWeighingEndTime", getTargetWeighingEndTime())
                .append("outLiquorTime", getOutLiquorTime())
                .append("storingTime", getStoringTime())
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
