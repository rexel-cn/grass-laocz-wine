package com.rexel.laocz.domain.vo;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.rexel.common.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 陶坛操作记录导出
 *
 * @author Yiyang.Hu
 * @version 1.0
 * @date 2024-04-08 10:52 AM
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LaoczPotteryAltarOperationRecordExportVO {
    /**
     * 业务时间（出酒、入酒、取样时间）
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "执行时间", dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date operationTime;
    /**
     * 创建者
     */
    @Excel(name = "执行人")
    private String createBy;
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
    @Excel(name = "陶坛编号")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String potteryAltarNumber;
    /**
     * 操作详细类型：1:入酒，2出酒，3倒坛入，4倒坛出，5取样
     */
    @Excel(name = "操作详细类型：1:入酒，2:出酒，3:倒坛入，4:倒坛出，5:取样")
    private String detailType;
    /**
     * 称重罐重量
     */
    @Excel(name = "操作重量KG")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Double weighingTankWeight;
    /**
     * 剩余重量
     */
    @Excel(name = "剩余重量KG")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Double remainingWeight;
}
