package com.rexel.earlywarning.service;

import java.util.List;
import com.rexel.earlywarning.domain.GrassEarlyWarningSuggest;

/**
 * 处置建议Service接口
 *
 * @author admin
 * @date 2022-01-14
 */
public interface IGrassEarlyWarningSuggestService {
    /**
     * 查询处置建议
     *
     * @param suggestId 处置建议ID
     * @return 处置建议
     */
    GrassEarlyWarningSuggest selectGrassEarlyWarningSuggestById(Long suggestId);

    /**
     * 查询处置建议列表
     *
     * @param suggest 处置建议
     * @return 处置建议集合
     */
    List<GrassEarlyWarningSuggest> selectGrassEarlyWarningSuggestList(GrassEarlyWarningSuggest suggest);

    /**
     * 新增处置建议
     *
     * @param suggest 处置建议
     * @return 结果
     */
    int insertGrassEarlyWarningSuggest(GrassEarlyWarningSuggest suggest);

    /**
     * 修改处置建议
     *
     * @param suggest 处置建议
     * @return 结果
     */
    int updateGrassEarlyWarningSuggest(GrassEarlyWarningSuggest suggest);

    /**
     * 删除处置建议信息
     *
     * @param suggestId 处置建议ID
     * @return 结果
     */
    int deleteGrassEarlyWarningSuggestById(Long suggestId);
}
