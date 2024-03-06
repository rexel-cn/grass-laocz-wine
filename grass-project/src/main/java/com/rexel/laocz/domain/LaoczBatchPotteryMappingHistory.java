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
 * 陶坛与批次历史关系
 * 对象 laocz_batch_pottery_mapping_history
 *
 * @author grass-service
 * @date 2024-03-06
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LaoczBatchPotteryMappingHistory extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 自增主键id
     */
    private Long mappingHisId;
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
     * 陶坛管理编号
     */
    @Excel(name = "陶坛管理编号")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String potteryAltarNumber;
    /**
     * 陶坛状态，1：使用，2封存
     */
    @Excel(name = "陶坛状态，1：使用，2封存")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long potteryAltarState;
    /**
     * 满坛重量
     */
    @Excel(name = "满坛重量")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long potteryAltarFullAltarWeight;
    /**
     * 陶坛二维码地址
     */
    @Excel(name = "陶坛二维码地址")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String potteryAltarQrCodeAddress;
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


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("mappingHisId", getMappingHisId())
                .append("tenantId", getTenantId())
                .append("workOrderId", getWorkOrderId())
                .append("liquorBatchId", getLiquorBatchId())
                .append("potteryAltarId", getPotteryAltarId())
                .append("potteryAltarNumber", getPotteryAltarNumber())
                .append("potteryAltarState", getPotteryAltarState())
                .append("potteryAltarFullAltarWeight", getPotteryAltarFullAltarWeight())
                .append("potteryAltarQrCodeAddress", getPotteryAltarQrCodeAddress())
                .append("areaName", getAreaName())
                .append("fireZoneName", getFireZoneName())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
