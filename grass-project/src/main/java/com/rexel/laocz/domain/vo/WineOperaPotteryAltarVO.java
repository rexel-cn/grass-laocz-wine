package com.rexel.laocz.domain.vo;

import com.rexel.laocz.domain.LaoczLiquorManagement;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName WineOperaPotteryAltarVO
 * @Description 酒操作（出酒、入酒、取样、倒坛）查询陶坛列表
 * @Author 孟开通
 * @Date 2024/3/28 17:45
 **/
@Data
public class WineOperaPotteryAltarVO {
    /**
     * 陶坛管理主键ID
     */
    private Long potteryAltarId;
    /**
     * 陶坛管理编号
     */
    private String potteryAltarNumber;
    /**
     * 区域名称
     */
    private String areaName;
    /**
     * 防火区名称
     */
    private String fireZoneName;
    /**
     * 满坛重量
     */
    private Long potteryAltarFullAltarWeight;
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
