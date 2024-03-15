package com.rexel.laocz.domain.dto;

import lombok.Data;

/**
 * @ClassName samplingApplyDTO
 * @Description
 * @Author 孟开通
 * @Date 2024/3/6 15:56
 **/
@Data
public class SamplingApplyDTO {
    /**
     * 陶坛管理主键ID
     */
    private Long potteryAltarId;
    /**
     * 取样用途 1-品尝 2-化验
     */
    private String samplingPurpose;
    /**
     * 取样重量 默认都是0.45KG
     */
    private Double samplingWeight = 0.45;
}
