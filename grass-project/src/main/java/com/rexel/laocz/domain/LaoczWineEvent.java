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
 * 操作酒事件对象 laocz_wine_event
 *
 * @author grass-service
 * @date 2024-03-12
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LaoczWineEvent extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 事件id
     */
    @TableId(type = IdType.AUTO)
    private Long wineEventId;
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
     * 事件ID（开始入酒、急停入酒、继续入酒）
     */
    @Excel(name = "事件ID", readConverterExp = "开=始入酒、急停入酒、继续入酒")
    private String eventId;
    /**
     * 事件触发时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "事件触发时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date eventTime;
    /**
     * 事件日志
     */
    @Excel(name = "事件日志")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String eventLog;
    /**
     * 事件状态
     */
    @Excel(name = "事件状态")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String eventStatus;
    /**
     * 事件请求参数
     */
    @Excel(name = "事件请求参数")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String eventParam;
    /**
     * 事件相关测点
     */
    @Excel(name = "事件相关测点")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String pointArray;


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("wineEventId", getWineEventId())
                .append("workOrderId", getWorkOrderId())
                .append("tenantId", getTenantId())
                .append("busyId", getBusyId())
                .append("liquorBatchId", getLiquorBatchId())
                .append("potteryAltarId", getPotteryAltarId())
                .append("eventId", getEventId())
                .append("eventTime", getEventTime())
                .append("eventLog", getEventLog())
                .append("eventStatus", getEventStatus())
                .append("eventParam", getEventParam())
                .append("pointArray", getPointArray())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
