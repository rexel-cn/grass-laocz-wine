package com.rexel.laocz.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 陶坛操作记录陶坛信息
 *
 * @author Yiyang.Hu
 * @version 1.0
 * @date 2024-03-20 2:01 PM
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PotteryAltarInformationInfoVO {
    /**
     * 申请重量
     */
    private Double potteryAltarApplyWeight;
    /**
     * 酒业重量
     */
    private Double liquorWeight;
    /**
     * 称重罐编号
     */
    private Long weighingTankNumber;
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
}
