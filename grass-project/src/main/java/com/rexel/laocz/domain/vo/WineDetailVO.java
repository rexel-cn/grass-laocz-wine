package com.rexel.laocz.domain.vo;

import lombok.Data;

/**
 * @ClassName WineDetailVO
 * @Description 酒操作业务详情
 * @Author 孟开通
 * @Date 2024/3/13 10:40
 **/
@Data
public class WineDetailVO {
    /**
     * 酒操作业务详情id
     */
    private Long WineDetailsId;
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
     * 防火区id
     */
    private Long fireZoneId;
    /**
     * 防火区名称
     */
    private String fireZoneName;
    /**
     * 申请重量
     */
    private Double potteryAltarApplyWeight;
    /**
     * 酒品批次号
     */
    private String liquorBatchId;
    /**
     * 酒品管理Id
     */
    private String liquorManagementId;
    /**
     * 酒品名称
     */
    private String liquorName;
    /**
     * 运行状态，0：未开始，1：开始，2：急停，3已完成
     */
    private String busyStatus;
    /**
     * 称重罐id
     */
    private Long weighingTank;
    /**
     * 称重罐名称
     */
    private String weighingTankNumber;
    /**
     * 取样用途
     */
    private String samplingPurpose;
    /**
     * 实际重量
     */
    private String actualWeight;
    /**
     * 称重罐重量
     */
    private String weighingTankWeight;

}
