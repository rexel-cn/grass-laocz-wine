package com.rexel.web.controller.analysis;

import com.alibaba.fastjson2.JSONObject;
import com.rexel.common.annotation.RepeatSubmit;
import com.rexel.common.core.domain.AjaxResult;
import com.rexel.system.domain.vo.TrendAnalysisVo;
import com.rexel.system.service.IGrassAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

/**
 * 趋势分析Controller
 *
 * @author ids
 * @date 2022-11-01
 */
@RestController
@RequestMapping("/rexel-api/analysis")
public class GrassAnalysisController {
    @Autowired
    private IGrassAnalysisService grassAnalysisService;

    /**
     * 趋势分析查询
     *
     * @param trendAnalysisVo TrendAnalysisVo
     * @return 结果
     */
    @PostMapping("/query")
    @RepeatSubmit(message = "30秒内请勿重复查询", interval = 30000)
    public AjaxResult queryTimeSeries(@RequestBody TrendAnalysisVo trendAnalysisVo) {
        JSONObject jsonObject;
        try {
            jsonObject = grassAnalysisService.queryTimeSeries(trendAnalysisVo);
        } catch (SQLException e) {
            e.printStackTrace();
            return AjaxResult.error("查询趋势分析数据异常");
        }
        return AjaxResult.success(jsonObject);
    }
}
