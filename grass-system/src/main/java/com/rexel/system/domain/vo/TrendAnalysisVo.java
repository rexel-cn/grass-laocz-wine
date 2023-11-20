package com.rexel.system.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * TrendAnalysisVo
 *
 * @author ids
 * @date 2022-11-01
 */
@Data
public class TrendAnalysisVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 开始时间
     */
    private String from;

    /**
     * 结束时间
     */
    private String to;

    /**
     * 时间间隔
     */
    private String interval;

    /**
     * 聚合功能
     */
    private String func;

    /**
     * 是否隐藏表单数据
     */
    private boolean hideTableData = true;

    /**
     * 是否连接空数据
     */
    private boolean connectNulls = false;

    /**
     * 测点集合
     */
    private List<TrendPointInfo> pointList;
}
