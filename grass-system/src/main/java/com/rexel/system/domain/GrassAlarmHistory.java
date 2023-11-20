package com.rexel.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.rexel.common.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 告警历史对象 grass_alarm_history
 *
 * @author grass-service
 * @date 2022-08-18
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GrassAlarmHistory {

    private static final long serialVersionUID = 1L;

    /**
     * 自增ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 租户ID
     */
    @Excel(name = "租户ID")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String tenantId;
    /**
     * 规则ID
     */
    @Excel(name = "规则ID")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long rulesId;
    /**
     * 规则名称
     */
    @Excel(name = "规则名称")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String rulesName;
    /**
     * 规则标识
     */
    @Excel(name = "规则标识")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String rulesUnique;
    /**
     * 设备ID
     */
    @Excel(name = "设备ID")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String deviceId;
    /**
     * 资产id
     */
    @Excel(name = "资产id")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String assetId;
    /**
     * 测点ID
     */
    @Excel(name = "测点ID")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String pointId;
    /**
     * 判断条件
     */
    @Excel(name = "判断条件")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String pointJudge;
    /**
     * 测点值
     */
    @Excel(name = "测点值")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Double pointValue;
    /**
     * 报警时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "报警时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date alarmTime;
    /**
     * 报警值
     */
    @Excel(name = "报警值")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Double alarmValue;
    /**
     * 报警级别
     */
    @Excel(name = "报警级别")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String rulesLevel;
    /**
     * 报警分类
     */
    @Excel(name = "报警分类")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String rulesType;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NOT_EMPTY)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.UPDATE, updateStrategy = FieldStrategy.NOT_EMPTY)
    private Date updateTime;
}
