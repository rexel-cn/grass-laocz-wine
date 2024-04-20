package com.rexel.laocz.domain;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.rexel.common.annotation.Excel;
import com.rexel.common.core.domain.BaseEntity;
import lombok.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 流转过程对象 laocz_wine_details
 *
 * @author grass-service
 * @date 2024-03-12
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LaoczWineDetails extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long wineDetailsId;
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
     * 业务标识
     */
    @Excel(name = "业务标识")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String busyId;
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
     * 0：未开始，1：开始，2：急停，3已完成
     */
    @Excel(name = "0：未开始，1：开始，2：急停，3已完成")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long busyStatus;
    /**
     * 操作详细类型：1:入酒，2出酒，3倒坛入，4倒坛出，5取样
     */
    @Excel(name = "1:入酒，2出酒，3倒坛入，4倒坛出，5取样")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long detailType;
    /**
     * 申请重量
     */
    @Excel(name = "申请重量")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Double potteryAltarApplyWeight;
    /**
     * 称重罐ID
     */
    @Excel(name = "称重罐ID")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long weighingTank;
    /**
     * 泵id
     */
    @Excel(name = "泵id")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long pumpId;
    /**
     * 称重罐重量
     */
    @Excel(name = "称重罐重量")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Double weighingTankWeight;


    /**
     * 开始之前重量
     */
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    @Excel(name = "开始之前重量")
    private Double beforeWeight;
    /**
     * 结束之后重量
     */
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    @Excel(name = "结束之后重量")
    private Double afterWeight;
    /**
     * 开始之前时间
     */
    @Excel(name = "开始之前时间")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Date beforeTime;
    /**
     * 结束之后时间
     */
    @Excel(name = "结束之后时间")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Date afterTime;
    /**
     * 业务时间（出酒、入酒、取样时间）
     */
    @Excel(name = "业务时间", readConverterExp = "出=酒、入酒、取样时间")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Date operationTime;
    /**
     * 取样用途
     */
    @Excel(name = "取样用途")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String samplingPurpose;


}
