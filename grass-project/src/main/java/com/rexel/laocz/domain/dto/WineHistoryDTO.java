package com.rexel.laocz.domain.dto;

import lombok.Data;

import java.util.Date;

/**
 * @ClassName WineHistoryDTO
 * @Description 用来查询 酒品相关信息，来保存到laocz_wine_history表
 * @Author 孟开通
 * @Date 2024/3/25 16:18
 **/
@Data
public class WineHistoryDTO {
    /**
     * 业务标识
     */
    private String busyId;
    /**
     * 申请的工单id
     */
    private String workOrderId;
    /**
     * 酒液批次号，时间戳生成
     */
    private String liquorBatchId;
    /**
     * 陶坛ID，外键关联laocz_pottery_altar_management
     */
    private Long potteryAltarId;
    /**
     * 操作类型：1：入酒，2出酒，3倒坛，4取样
     */
    private Long operationType;
    /**
     * 申请重量
     */
    private Double potteryAltarApplyWeight;
    /**
     * 剩余重量
     */
    private Double remainingWeight;
    /**
     * 亏损重量
     */
    private Double lossWeight;
    /**
     * 称重罐ID
     */
    private Long weighingTank;
    /**
     * 泵id
     */
    private Long pumpId;
    /**
     * 称重罐重量
     */
    private Double weighingTankWeight;
    /**
     * 业务时间（出酒、入酒、取样时间）
     */
    private Date operationTime;
    /**
     * 取样用途
     */
    private String samplingPurpose;
    /**
     * 取样重量
     */
    private Double samplingWeight;
    /**
     * 场区信息ID
     */
    private Long areaId;
    /**
     * 场区名称
     */
    private String areaName;
    /**
     * 防火区主键ID
     */
    private Long fireZoneId;
    /**
     * 防火区名称
     */
    private String fireZoneName;
    /**
     * 陶坛管理编号
     */
    private String potteryAltarNumber;
    /**
     * 陶坛二维码地址
     */
    private String potteryAltarQrCodeAddress;
    /**
     * 满坛重量
     */
    private Double potteryAltarFullAltarWeight;
    /**
     * 酒品名称
     */
    private String liquorName;
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
    private Date liquorBrewingTime;
    /**
     * 酒精度数
     */
    private String liquorContent;

}
