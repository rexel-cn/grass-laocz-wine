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
 * 出酒记录对象 laocz_out_liquor_history
 *
 * @author grass-service
 * @date 2024-03-06
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LaoczOutLiquorHistory extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 出酒记录id
     */
    private Long outLiquorHistoryId;
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
     * 陶坛ID，外键关联laocz_pottery_altar_management
     */
    @Excel(name = "陶坛ID，外键关联laocz_pottery_altar_management")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long potteryAltarId;
    /**
     * 0：未开始，1：开始，2已完成
     */
    @Excel(name = "0：未开始，1：开始，2已完成")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long state;
    /**
     * 称重罐ID
     */
    @Excel(name = "称重罐ID")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long weighingTank;
    /**
     * 称重罐重量
     */
    @Excel(name = "称重罐重量")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long weighingTankWeight;
    /**
     * 出酒时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "出酒时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date outLiquorTime;
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
                .append("outLiquorHistoryId", getOutLiquorHistoryId())
                .append("workOrderId", getWorkOrderId())
                .append("tenantId", getTenantId())
                .append("liquorBatchId", getLiquorBatchId())
                .append("potteryAltarId", getPotteryAltarId())
                .append("state", getState())
                .append("weighingTank", getWeighingTank())
                .append("weighingTankWeight", getWeighingTankWeight())
                .append("outLiquorTime", getOutLiquorTime())
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
