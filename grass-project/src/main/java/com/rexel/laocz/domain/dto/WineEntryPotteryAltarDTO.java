package com.rexel.laocz.domain.dto;

import lombok.Data;

import java.util.List;

/**
 * @ClassName WineEntryPotteryAltarDTO
 * @Description 入酒，陶坛筛选DTO
 * @Author 孟开通
 * @Date 2024/3/21 10:07
 **/
@Data
public class WineEntryPotteryAltarDTO {
    /**
     * 防火区主键ID
     */
    private Long fireZoneId;
    /**
     * 陶坛管理编号
     */
    private String potteryAltarNumber;
    /**
     * 陶坛管理主键ID
     */
    private List<Long> potteryAltarIds;

}
