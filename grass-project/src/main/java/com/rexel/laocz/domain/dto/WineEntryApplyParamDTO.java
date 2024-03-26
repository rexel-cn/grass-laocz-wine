package com.rexel.laocz.domain.dto;

import lombok.Data;

/**
 * @ClassName WineEntryApplyParamDTO
 * @Description 入酒申请参数
 * @Author 孟开通
 * @Date 2024/3/13 11:39
 **/
@Data
public class WineEntryApplyParamDTO {
    /**
     * 酒操作业务详情id
     */
    private Long wineDetailsId;
    /**
     * 防火区id
     */
    private Long fireZoneId;
    /**
     * 称重罐id
     */
    private Long weighingTank;
}
