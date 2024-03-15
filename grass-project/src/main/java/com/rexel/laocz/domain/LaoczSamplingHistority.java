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
 * 取样记录
 * 对象 laocz_sampling_histority
 *
 * @author grass-service
 * @date 2024-03-06
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LaoczSamplingHistority extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 取样主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long samplingHistorityId;
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
    private Long samplingWeight;
    /**
     * 取样时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "取样时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date samplingDate;
    /**
     * 取样文件上传时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "取样文件上传时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date samplingFileDate;
    /**
     * 取样文件上传地址
     */
    @Excel(name = "取样文件上传地址")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String samplingFile;


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("samplingHistorityId", getSamplingHistorityId())
                .append("workOrderId", getWorkOrderId())
                .append("tenantId", getTenantId())
                .append("liquorBatchId", getLiquorBatchId())
                .append("potteryAltarId", getPotteryAltarId())
                .append("samplingPurpose", getSamplingPurpose())
                .append("samplingWeight", getSamplingWeight())
                .append("samplingDate", getSamplingDate())
                .append("samplingFileDate", getSamplingFileDate())
                .append("samplingFile", getSamplingFile())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
