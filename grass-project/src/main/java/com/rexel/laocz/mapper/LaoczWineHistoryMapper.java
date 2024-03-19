package com.rexel.laocz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rexel.laocz.domain.LaoczWineHistory;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 酒历史Mapper接口
 *
 * @author grass-service
 * @date 2024-03-12
 */
@Repository
public interface LaoczWineHistoryMapper extends BaseMapper<LaoczWineHistory> {
    /**
     * 查询酒历史列表
     *
     * @param laoczWineHistory 酒历史
     * @return 酒历史集合
     */
    List<LaoczWineHistory> selectLaoczWineHistoryList(LaoczWineHistory laoczWineHistory);

    /**
     * 批量新增酒历史
     *
     * @param laoczWineHistoryList 酒历史列表
     * @return 结果
     */
    int batchLaoczWineHistory(List<LaoczWineHistory> laoczWineHistoryList);

    void saveHistory(Long wineDetailsId);
}
