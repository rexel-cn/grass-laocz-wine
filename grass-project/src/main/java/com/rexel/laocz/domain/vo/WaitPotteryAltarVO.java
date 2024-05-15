package com.rexel.laocz.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WaitPotteryAltarVO {
    /**
     * 申请重量
     */
    private Double potteryAltarApplyWeight;
    /**
     * 酒液重量
     */
    private Double actualWeight;
    /**
     * 取样重量
     */
    private Double samplingWeight;
    /**
     * 称重罐编号
     */
    private String weighingTankNumber;
    /**
     * 剩余重量
     */
    private Double remainingWeight;
    /**
     * 陶坛管理编号
     */
    private String potteryAltarNumber;
    /**
     * 满坛重量
     */
    private Double potteryAltarFullAltarWeight;
    /**
     * 场区名称
     */
    private String areaName;
    /**
     * 防火区名称
     */
    private String fireZoneName;
    /**
     * 执行人
     */
    private String createBy;
    /**
     * 执行时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date operationTime;
    /**
     * 存储时长
     */
    private Long retentionDays;
    /**
     * 陶坛状态
     */
    private String potteryAltarState;
    /**
     * 酒品批次ID
     */
    private String liquorBatchId;
    /**
     * 开始之前重量
     */
    private Double beforeWeight;
    /**
     * 结束之后重量
     */
    private Double afterWeight;
}
