package com.rexel.laocz.domain.vo;

import com.rexel.laocz.domain.LaoczLiquorManagement;
import lombok.Data;

import java.util.Date;

@Data
public class PotteryAltarVo {
    /**
     * 陶坛管理主键ID
     */
    private Long potteryAltarId;
    /**
     * 陶坛管理编号
     */
    private String potteryAltarNumber;
    /**
     * 陶坛状态，1：使用，2封存
     */
    private String potteryAltarState;
    /**
     * 区域id
     */
    private Long areaId;
    /**
     * 区域名称
     */
    private String areaName;
    /**
     * 防火区主键ID，外键关联laocz_fire_zone_info
     */
    private Long fireZoneId;
    /**
     * 防火区名称
     */
    private String fireZoneName;
    /**
     * 满坛重量
     */
    private Long potteryAltarFullAltarWeight;
    /**
     * 陶坛二维码地址
     */
    private String potteryAltarQrCodeAddress;
    /**
     * 陶坛实际重量
     */
    private Double actualWeight;
    /**
     * 酒品id
     */
    private Long liquorManagementId;
    /**
     * 酒批次id
     */
    private Long liquorBatchId;
    /**
     * 入酒时间
     */
    private Date storingTime;
    /**
     * 存储时间 以天为单位 当前时间-入酒时间
     */
    private Integer storageTime;

    /**
     * 酒品
     */
    private LaoczLiquorManagement laoczLiquorManagement;

}
