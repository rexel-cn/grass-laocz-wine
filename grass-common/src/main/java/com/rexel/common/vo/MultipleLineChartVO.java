package com.rexel.common.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName MultipleLineChartVO
 * @Description 折线图 - 柱状图 多条
 * @Author 孟开通
 * @Date 2022-11-29 10:33:43
 **/
@Data
public class MultipleLineChartVO {
    /**
     * 图表标题
     */
    private String chartTitle;
    /**
     * 图表点位
     */
    private List<String> legend;
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
    private List<yAxis> yAxis = new ArrayList<>();

    @Data
    public class yAxis {
        private String name;
        private List<String> data;
    }
}
