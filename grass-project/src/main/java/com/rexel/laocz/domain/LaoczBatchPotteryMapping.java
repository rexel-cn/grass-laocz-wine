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
 * 陶坛与批次实时关系对象 laocz_batch_pottery_mapping
 *
 * @author grass-service
 * @date 2024-03-12
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LaoczBatchPotteryMapping extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 关联ID，主键
     */
    @TableId(type = IdType.AUTO)
    private Long mappingId;
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
     * 该批次在此陶坛中的实际酒重量
     */
    @Excel(name = "该批次在此陶坛中的实际酒重量")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Double actualWeight;
    /**
     * 运行状态（0：占用，1：存储，2：入酒中，3：出酒中，4：倒坛中）
     */
    @Excel(name = "运行状态", readConverterExp = "0=：占用，1：存储，2：入酒中，3：出酒中，4：倒坛中")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long realStatus;
    /**
     * 入酒时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "入酒时间", width = 30, dateFormat = "yyyy-MM-dd")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Date storingTime;


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("mappingId", getMappingId())
                .append("tenantId", getTenantId())
                .append("liquorBatchId", getLiquorBatchId())
                .append("potteryAltarId", getPotteryAltarId())
                .append("actualWeight", getActualWeight())
                .append("operatingState", getRealStatus())
                .append("storingTime", getStoringTime())
                .append("createTime", getCreateTime())
                .append("createBy", getCreateBy())
                .append("updateTime", getUpdateTime())
                .append("updateBy", getUpdateBy())
                .append("remark", getRemark())
                .toString();
    }
}
