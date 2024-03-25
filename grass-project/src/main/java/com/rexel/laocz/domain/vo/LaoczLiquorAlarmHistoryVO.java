package com.rexel.laocz.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rexel.common.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 报警记录
 *
 * @author Yiyang.Hu
 * @version 1.0
 * @date 2024-03-25 11:41 AM
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LaoczLiquorAlarmHistoryVO {
    /**
     * 报警时间
     */
    @Excel(name = "报警时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**
     * 规则名称
     */
    @Excel(name = "规则名称")
    private String liquorRuleName;
    /**
     * 酒液批次报警ID
     */
    @Excel(name = "酒液批次")
    private String liquorRuleId;
    /**
     * 酒品名称
     */
    @Excel(name = "酒品名称")
    private String liquorName;
    /**
     * 场区名称
     */
    @Excel(name = "归属区域")
    private String areaName;
    /**
     * 防火区名称
     */
    @Excel(name = "归属防火区")
    private String fireZoneName;
    /**
     * 陶坛管理编号
     */
    @Excel(name = "陶坛编号")
    private String potteryAltarNumber;
    /**
     * 规则判断条件
     */
    @Excel(name = "判断条件")
    private String liquorRuleJudge;
    /**
     * 报警阈值
     */
    @Excel(name = "报警阈值天")
    private String liquorRuleThreshold;
    /**
     * 报警值
     */
    @Excel(name = "报警值天")
    private String alarmValue;

}
