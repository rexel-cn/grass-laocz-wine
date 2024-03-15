package com.rexel.laocz.domain.vo;

import lombok.Data;

/**
 * @ClassName WineRealDataVO
 * @Description 入酒、出酒 实时数据
 * @Author 孟开通
 * @Date 2024/3/13 17:21
 **/
@Data
public class WineRealDataVO {
    /**
     * 申请重量
     */
    private Double potteryAltarApplyWeight;
    /**
     * 称重罐当前重量
     */
    private Double currentWeight;
    /**
     * 当前运行状态 0：未开始，1：开始，2：急停，3已完成"
     */
    private Long busyStatus;
}
