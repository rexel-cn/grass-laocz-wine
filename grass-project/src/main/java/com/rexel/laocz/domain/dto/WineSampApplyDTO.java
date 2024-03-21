package com.rexel.laocz.domain.dto;

import lombok.Data;

/**
 * @ClassName WineSampApply
 * @Description 取样申请
 * @Author 孟开通
 * @Date 2024/3/19 18:14
 **/
@Data
public class WineSampApplyDTO {
    /**
     * 陶坛罐主键id
     */
    private Long potteryAltarId;
    /**
     * 取样用途
     */
    private String sampPurpose;
    /**
     * 取样重量
     */
    private Double samplingWeight = 0.45;

}
