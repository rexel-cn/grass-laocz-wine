package com.rexel.laocz.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 陶坛信息
 *
 * @author Yiyang.Hu
 * @version 1.0
 * @date 2024-03-19 3:56 PM
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PotteryAltarInformationVO {
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
