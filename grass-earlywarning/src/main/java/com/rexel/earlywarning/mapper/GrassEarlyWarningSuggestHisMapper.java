package com.rexel.earlywarning.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rexel.earlywarning.domain.GrassEarlyWarningSuggestHis;
import org.springframework.stereotype.Repository;

/**
 * 处置建议历史Mapper接口
 *
 * @author grass-service
 * @date 2023-10-17
 */
@Repository
public interface GrassEarlyWarningSuggestHisMapper extends BaseMapper<GrassEarlyWarningSuggestHis> {
    /**
     * 查询处置建议历史列表
     *
     * @param grassEarlyWarningSuggestHis 处置建议历史
     * @return 处置建议历史集合
     */
    List<GrassEarlyWarningSuggestHis> selectGrassEarlyWarningSuggestHisList(GrassEarlyWarningSuggestHis grassEarlyWarningSuggestHis);

    /**
     * 批量新增处置建议历史
     *
     * @param grassEarlyWarningSuggestHisList 处置建议历史列表
     * @return 结果
     */
    int batchGrassEarlyWarningSuggestHis(List<GrassEarlyWarningSuggestHis> grassEarlyWarningSuggestHisList);
}
