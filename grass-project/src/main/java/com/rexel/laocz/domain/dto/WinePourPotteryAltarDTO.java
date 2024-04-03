package com.rexel.laocz.domain.dto;

import lombok.Data;

/**
 * @ClassName WinePourPotteryAltarDTO
 * @Description 倒坛入酒DTO
 * @Author 孟开通
 * @Date 2024/3/29 10:30
 **/
@Data
public class WinePourPotteryAltarDTO extends WinePotteryAltarBaseDTO {
    /**
     * 如果是出酒不需要传递，如果是入酒需要传递出酒的id
     */
    private Long potteryAltarId;
    /**
     * 酒液重量  如果是倒坛入酒时，需要带入倒坛出酒时的重量
     */
    private Double wineWeight;


}

