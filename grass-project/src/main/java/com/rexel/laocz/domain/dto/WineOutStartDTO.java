package com.rexel.laocz.domain.dto;

import lombok.Data;

/**
 * @ClassName WineOutStartDTO
 * @Description
 * @Author 孟开通
 * @Date 2024/5/7 14:56
 **/
@Data
public class WineOutStartDTO {
    /**
     * 酒操作业务详情id
     */
    private Long wineDetailsId;
    /**
     * 出酒类型 1出酒前，2出酒后
     */
    private Integer type;
}
