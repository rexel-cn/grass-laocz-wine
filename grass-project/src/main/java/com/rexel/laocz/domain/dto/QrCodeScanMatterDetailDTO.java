package com.rexel.laocz.domain.dto;

import lombok.Data;

/**
 * @ClassName QrCodeScanMatterDetailDTO
 * @Description
 * @Author 孟开通
 * @Date 2024/5/23 15:48
 **/
@Data
public class QrCodeScanMatterDetailDTO {
    /**
     * 事项id
     */
    Long wineOperationsId;
    /**
     * 陶坛编号
     */
    String potteryAltarNumber;
}
