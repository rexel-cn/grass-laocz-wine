package com.rexel.system.domain.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @ClassName TimeSeriesWindowVO
 * @Description
 * @Author ids
 * @Date 2022/11/1
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TimeSeriesWindowVO extends TimeSeriesDataVO {
    /**
     * 聚合函数
     */
    private String func;
}
