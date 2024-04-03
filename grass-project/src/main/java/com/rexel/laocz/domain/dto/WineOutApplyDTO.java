package com.rexel.laocz.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName WineOutApplyDTO
 * @Description 出酒申请参数
 * @Author 孟开通
 * @Date 2024/3/11 14:06
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WineOutApplyDTO {
    /**
     * 陶坛罐主键id
     */
    private Long potteryAltarId;
    /**
     * 申请重量
     */
    private Double applyWeight;
}
