package com.rexel.laocz.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class LaoczSamplingHistorityVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 取样主键ID
     */
    private Long samplingHistorityId;
    /**
     * 申请的工单id
     */
    private String workOrderId;
    /**
     * 租户ID
     */
    private String tenantId;
    /**
     * 酒品名称
     */
    private String liquorName;
    /**
     * 酒批次ID，外键关联laocz_liquor_batch
     */
    private String liquorBatchId;
    /**
     * 陶坛ID，外键关联laocz_pottery_altar_management
     */
    private Long potteryAltarId;
    /**
     * 取样用途
     */
    private String samplingPurpose;
    /**
     * 取样重量
     */
    private Double samplingWeight;
    /**
     * 取样时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date samplingDate;
    /**
     * 取样文件上传时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date samplingFileDate;
    /**
     * 取样文件上传地址
     */
    private String samplingFile;
    /**
     * 取样文件名称
     */
    private String samplingFileName;
    /**
     * 场区名称
     */
    private String areaName;
    /**
     * 防火区名称
     */
    private String fireZoneName;
    /**
     * 陶坛管理编号
     */
    private String potteryAltarNumber;
    /**
     * 显示上传还是下载 0：上传 1：下载
     */
    private Integer state;

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
