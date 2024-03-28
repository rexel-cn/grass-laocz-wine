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
    /**
     * 条件查询，1有酒（取样，出酒，倒坛（出酒）），2没酒（入酒）   3倒坛（入酒）：空罐子，或者倒坛入酒同一批次的有酒的坛子
     */
    private String conditionQuery;
    /**
     * 酒批次id，如果是倒坛入酒，需要传入酒批次id 和conditionQuery一起使用
     */
    private String liquorBatchId;
}
