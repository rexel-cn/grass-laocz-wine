package com.rexel.laocz.domain;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * 酒历史对象 laocz_wine_history
 *
 * @author grass-service
 * @date 2024-03-12
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LaoczWineHistory extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long winHisId;
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
     * 酒液批次号，时间戳生成
     */
    @Excel(name = "酒液批次号，时间戳生成")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String liquorBatchId;
    /**
     * 陶坛ID，外键关联laocz_pottery_altar_management
     */
    @Excel(name = "陶坛ID，外键关联laocz_pottery_altar_management")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long potteryAltarId;
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
    private Double potteryAltarApplyWeight;
    /**
     * 剩余重量
     */
    @Excel(name = "剩余重量")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Double remainingWeight;
    /**
     * 亏损重量
     */
    @Excel(name = "亏损重量")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Double lossWeight;
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
     * 业务时间（出酒、入酒、取样时间）
     */
    @Excel(name = "业务时间", readConverterExp = "出=酒、入酒、取样时间")
    private Date operationTime;
    /**
     * 取样用途
     */
    @Excel(name = "取样用途")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String samplingPurpose;
    /**
     * 取样重量
     */
    @Excel(name = "取样重量")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Double samplingWeight;
    /**
     * 场区信息ID
     */
    @Excel(name = "场区信息ID")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long areaId;
    /**
     * 场区名称
     */
    @Excel(name = "场区名称")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String areaName;
    /**
     * 防火区主键ID
     */
    @Excel(name = "防火区主键ID")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long fireZoneId;
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
    /**
     * 陶坛二维码地址
     */
    @Excel(name = "陶坛二维码地址")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String potteryAltarQrCodeAddress;
    /**
     * 满坛重量
     */
    @Excel(name = "满坛重量")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Double potteryAltarFullAltarWeight;
    /**
     * 酒品名称
     */
    @Excel(name = "酒品名称")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String liquorName;
    /**
     * 酒类等级——酒品字典维护
     */
    @Excel(name = "酒类等级——酒品字典维护")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String liquorLevel;
    /**
     * 酒业轮次
     */
    @Excel(name = "酒业轮次")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String liquorRound;
    /**
     * 酒香名称——酒品字典维护
     */
    @Excel(name = "酒香名称——酒品字典维护")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String liquorFlavorName;
    /**
     * 酒液来源
     */
    @Excel(name = "酒液来源")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String liquorSource;
    /**
     * 酒液年份
     */
    @Excel(name = "酒液年份")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String liquorYear;
    /**
     * 酒液酿造时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "酒液酿造时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date liquorBrewingTime;
    /**
     * 酒精度数
     */
    @Excel(name = "酒精度数")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String liquorContent;


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("winHisId", getWinHisId())
                .append("busyId", getBusyId())
                .append("tenantId", getTenantId())
                .append("workOrderId", getWorkOrderId())
                .append("liquorBatchId", getLiquorBatchId())
                .append("potteryAltarId", getPotteryAltarId())
                .append("operationType", getOperationType())
                .append("potteryAltarApplyWeight", getPotteryAltarApplyWeight())
                .append("remainingWeight", getRemainingWeight())
                .append("lossWeight", getLossWeight())
                .append("weighingTank", getWeighingTank())
                .append("pumpId", getPumpId())
                .append("weighingTankWeight", getWeighingTankWeight())
                .append("operationTime", getOperationTime())
                .append("samplingPurpose", getSamplingPurpose())
                .append("samplingWeight", getSamplingWeight())
                .append("areaName", getAreaName())
                .append("fireZoneName", getFireZoneName())
                .append("potteryAltarNumber", getPotteryAltarNumber())
                .append("potteryAltarQrCodeAddress", getPotteryAltarQrCodeAddress())
                .append("potteryAltarFullAltarWeight", getPotteryAltarFullAltarWeight())
                .append("liquorName", getLiquorName())
                .append("liquorLevel", getLiquorLevel())
                .append("liquorRound", getLiquorRound())
                .append("liquorFlavorName", getLiquorFlavorName())
                .append("liquorSource", getLiquorSource())
                .append("liquorYear", getLiquorYear())
                .append("liquorBrewingTime", getLiquorBrewingTime())
                .append("liquorContent", getLiquorContent())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
