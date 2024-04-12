package com.rexel.laocz.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @ClassName QrInCodeScanDTO
 * @Description
 * @Author 孟开通
 * @Date 2024/4/11 16:22
 **/
@Data
public class QrInCodeScanDTO {
    /**
     * 申请入酒的陶坛编号
     */
    @NotEmpty(message = "扫描编号失败")
    private String potteryAltarNumber;
    /**
     * 申请倒坛出酒的陶坛id
     */
    @NotNull(message = "入酒陶坛参数有误，请重新申请")
    private Long outPotteryAltarId;
    /**
     * 申请的重量
     */
    @NotNull(message = "重量参数有误，请重新申请")
    private Double outWeight;
}
