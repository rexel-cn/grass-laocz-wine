package com.rexel.laocz.domain.vo;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.rexel.common.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 陶坛操作记录陶坛信息
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
