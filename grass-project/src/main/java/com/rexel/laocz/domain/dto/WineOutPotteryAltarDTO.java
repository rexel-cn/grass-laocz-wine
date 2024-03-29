package com.rexel.laocz.domain.dto;

import com.rexel.laocz.enums.RealStatusEnum;
import lombok.Data;


/**
 * @ClassName WineOutPotteryAltarDTO
 * @Description 出酒，陶坛筛选DTO
 * @Author 孟开通
 * @Date 2024/3/29 09:54
 **/
@Data
public class WineOutPotteryAltarDTO extends WinePotteryAltarBaseDTO {
    /**
     * 酒批次id
     */
    private String liquorBatchId;
    /**
     * 存储中
     */
    private Long realStatus = RealStatusEnum.STORAGE.getCode();
}
