package com.rexel.laocz.domain.dto;

import lombok.Data;

/**
 * @ClassName WinePourPotApplyDTO
 * @Description 倒坛申请
 * @Author 孟开通
 * @Date 2024/4/1 15:06
 **/
@Data
public class WinePourPotApplyDTO {
    /**
     * 出酒 陶坛罐主键id
     */
    private Long outPotteryAltarId;
    /**
     * 申请重量
     */
    private Double applyWeight;
    /**
     * 入酒 陶坛罐主键id
     */
    private Long inPotteryAltarId;


}
