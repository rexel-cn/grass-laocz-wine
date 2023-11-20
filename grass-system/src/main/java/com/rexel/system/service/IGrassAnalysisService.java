package com.rexel.system.service;

import com.alibaba.fastjson2.JSONObject;
import com.rexel.system.domain.vo.TrendAnalysisVo;

import java.sql.SQLException;

/**
 * 趋势分析Service接口
 *
 * @author ids
 * @date 2022-11-01
 */
public interface IGrassAnalysisService {
    /**
     * 查询趋势分析数据
     *
     * @param trendAnalysisVo 查询参数
     * @return 结果
     * @throws SQLException e
     */
    JSONObject queryTimeSeries(TrendAnalysisVo trendAnalysisVo) throws SQLException;
}
