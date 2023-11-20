package com.rexel.earlywarning.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rexel.earlywarning.domain.GrassEarlyWarningSuggestHis;

/**
 * 处置建议历史Service接口
 *
 * @author grass-service
 * @date 2023-10-17
 */
public interface IGrassEarlyWarningSuggestHisService extends IService<GrassEarlyWarningSuggestHis> {

    /**
     * 查询处置建议历史列表
     *
     * @param grassEarlyWarningSuggestHis 处置建议历史
     * @return 处置建议历史集合
     */
    List<GrassEarlyWarningSuggestHis> selectGrassEarlyWarningSuggestHisList(GrassEarlyWarningSuggestHis grassEarlyWarningSuggestHis);
}
