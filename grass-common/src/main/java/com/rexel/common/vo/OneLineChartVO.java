package com.rexel.common.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName OneLineChartVO
 * @Description 折线图 - 柱状图  一条
 * @Author 孟开通
 * @Date 2022-11-29 10:34:00
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OneLineChartVO {

    /**
     * 图表标题
     */
    private String chartTitle;
    /**
     * 图表点位
     */
    private String legend;
    /**
     * 单位
     */
    private String unit;
    /**
     * 横坐标
     */
    private List<String> xAxis = new ArrayList<>();
    /**
     * 纵坐标
     */
    private List<String> yAxis = new ArrayList<>();
}
