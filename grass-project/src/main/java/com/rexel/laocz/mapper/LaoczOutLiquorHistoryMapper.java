package com.rexel.laocz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rexel.laocz.domain.LaoczOutLiquorHistory;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 出酒记录Mapper接口
 *
 * @author grass-service
 * @date 2024-03-06
 */
@Repository
public interface LaoczOutLiquorHistoryMapper extends BaseMapper<LaoczOutLiquorHistory> {
    /**
     * 查询出酒记录列表
     *
     * @param laoczOutLiquorHistory 出酒记录
     * @return 出酒记录集合
     */
    List<LaoczOutLiquorHistory> selectLaoczOutLiquorHistoryList(LaoczOutLiquorHistory laoczOutLiquorHistory);

    /**
     * 批量新增出酒记录
     *
     * @param laoczOutLiquorHistoryList 出酒记录列表
     * @return 结果
     */
    int batchLaoczOutLiquorHistory(List<LaoczOutLiquorHistory> laoczOutLiquorHistoryList);

}
