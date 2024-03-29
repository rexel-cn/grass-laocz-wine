package com.rexel.laocz.domain.dto;

import com.rexel.laocz.enums.RealStatusEnum;

/**
 * @ClassName WineSamplePotteryAltarDTO
 * @Description 取样，陶坛筛选DTO
 * @Author 孟开通
 * @Date 2024/3/29 10:15
 **/
public class WineSamplePotteryAltarDTO extends WinePotteryAltarBaseDTO {
    /**
     * 存储中
     */
    private Long realStatus = RealStatusEnum.STORAGE.getCode();
}
