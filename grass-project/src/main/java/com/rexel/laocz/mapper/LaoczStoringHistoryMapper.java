package com.rexel.laocz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rexel.laocz.domain.LaoczStoringHistory;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 入酒记录Mapper接口
 *
 * @author grass-service
 * @date 2024-03-06
 */
@Repository
public interface LaoczStoringHistoryMapper extends BaseMapper<LaoczStoringHistory> {
    /**
     * 查询入酒记录列表
     *
     * @param laoczStoringHistory 入酒记录
     * @return 入酒记录集合
     */
    List<LaoczStoringHistory> selectLaoczStoringHistoryList(LaoczStoringHistory laoczStoringHistory);

    /**
     * 批量新增入酒记录
     *
     * @param laoczStoringHistoryList 入酒记录列表
     * @return 结果
     */
    int batchLaoczStoringHistory(List<LaoczStoringHistory> laoczStoringHistoryList);

}
