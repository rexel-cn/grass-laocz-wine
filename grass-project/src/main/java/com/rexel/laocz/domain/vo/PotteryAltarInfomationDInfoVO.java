package com.rexel.laocz.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author Yiyang.Hu
 * @version 1.0
 * @date 2024-03-20 3:53 PM
 */
@Data
public class PotteryAltarInfomationDInfoVO {
    /**
     * 申请的工单id
     */
    private String workOrderId;
    /**
     * 申请重量
     */
    private Double ApplyWeight;
    /**
     * 酒品名称
     */
    private String liquorName;
    /**
     * 酒批次ID，外键关联laocz_liquor_batch
     */
    private String liquorBatchId;
    /**
     * 酒类等级——酒品字典维护
     */
    private String liquorLevel;
    /**
     * 酒业轮次
     */
    private String liquorRound;
    /**
     * 酒香名称——酒品字典维护
     */
    private String liquorFlavorName;
    /**
     * 酒液来源
     */
    private String liquorSource;
    /**
     * 酒液年份
     */
    private String liquorYear;
    /**
     * 酒液酿造时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date liquorBrewingTime;
    /**
     * 酒精度数
     */
    private String liquorContent;
    /**
     * 申请重量
     */
    private Long potteryAltarApplyWeight;
    /**
     * 称重罐编号
     */
    private Long weighingTankNumber;
    /**
     * 剩余重量
     */
    private Long remainingWeight;
    /**
     * 陶坛管理编号
     */
    private String potteryAltarNumber;
    /**
     * 陶坛状态，1：使用，2封存
     */
    private String potteryAltarState;
    /**
     * 满坛重量
     */
    private Long potteryAltarFullAltarWeight;
    /**
     * 场区名称
     */
    private String areaName;
    /**
     * 防火区名称
     */
    private String fireZoneName;
}