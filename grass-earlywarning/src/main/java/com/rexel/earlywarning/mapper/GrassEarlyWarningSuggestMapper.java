package com.rexel.earlywarning.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.rexel.earlywarning.domain.GrassEarlyWarningSuggest;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 处置建议Mapper接口
 *
 * @author admin
 * @date 2022-01-14
 */
@Repository
public interface GrassEarlyWarningSuggestMapper {
    /**
     * 查询处置建议
     *
     * @param suggestId 处置建议ID
     * @return 处置建议
     */
    @InterceptorIgnore(tenantLine = "on")
    GrassEarlyWarningSuggest selectGrassEarlyWarningSuggestById(Long suggestId);

    /**
     * 查询处置建议列表
     *
     * @param info 处置建议
     * @return 处置建议集合
     */
    List<GrassEarlyWarningSuggest> selectGrassEarlyWarningSuggestList(GrassEarlyWarningSuggest info);

    /**
     * 新增处置建议
     *
     * @param info 处置建议
     * @return 结果
     */
    int insertGrassEarlyWarningSuggest(GrassEarlyWarningSuggest info);

    /**
     * 修改处置建议
     *
     * @param info 处置建议
     * @return 结果
     */
    int updateGrassEarlyWarningSuggest(GrassEarlyWarningSuggest info);

    /**
     * 删除处置建议
     *
     * @param suggestId 处置建议ID
     * @return 结果
     */
    int deleteGrassEarlyWarningSuggestById(Long suggestId);

    /**
     * 批量删除处置建议
     *
     * @param suggestIds 需要删除的数据ID
     * @return 结果
     */
    int deleteGrassEarlyWarningSuggestByIds(Long[] suggestIds);
}
