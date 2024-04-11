package com.rexel.laocz.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rexel.common.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 批次亏损报表导出
 *
 * @author Yiyang.Hu
 * @version 1.0
 * @date 2024-04-08 11:07 AM
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LaoczBatchLossReportExportVO {
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
    private String areaName;
    /**
     * 防火区名称
     */
    @Excel(name = "防火区名称")
    private String fireZoneName;
    /**
     * 陶坛管理编号
     */
    @Excel(name = "陶坛编号")
    private String potteryAltarNumber;
    /**
     * 操作详细类型：1:入酒，2出酒，3倒坛入，4倒坛出，5取样
     */
    @Excel(name = "操作详细类型：1:入酒，2:出酒，3:倒坛入，4:倒坛出，5:取样")
    private String detailType;
    /**
     * 申请重量
     */
    @Excel(name = "申请重量KG")
    private Double potteryAltarApplyWeight;
    /**
     * 称重罐重量
     */
    @Excel(name = "操作重量KG")
    private Double weighingTankWeight;
    /**
     * 剩余重量
     */
    @Excel(name = "剩余重量KG")
    private Double remainingWeight;
}
