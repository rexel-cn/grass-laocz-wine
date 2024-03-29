package com.rexel.laocz.domain.dto;

/**
 * @ClassName WinePourPotteryAltarDTO
 * @Description 倒坛入酒DTO
 * @Author 孟开通
 * @Date 2024/3/29 10:30
 **/
public class WinePourPotteryAltarDTO extends WinePotteryAltarBaseDTO {


    /**
     * true 是倒坛入酒，false是倒坛出酒
     */
    private boolean pour;
    /**
     * 酒液重量  如果是倒坛入酒时，需要带入倒坛出酒时的重量
     */
    private Double wineWeight;


}

