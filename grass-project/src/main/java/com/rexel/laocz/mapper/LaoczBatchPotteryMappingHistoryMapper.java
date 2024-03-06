package com.rexel.laocz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rexel.laocz.domain.LaoczBatchPotteryMappingHistory;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 陶坛与批次历史关系
 * Mapper接口
 *
 * @author grass-service
 * @date 2024-03-06
 */
@Repository
public interface LaoczBatchPotteryMappingHistoryMapper extends BaseMapper<LaoczBatchPotteryMappingHistory> {
    /**
     * 查询陶坛与批次历史关系
     * 列表
     *
     * @param laoczBatchPotteryMappingHistory 陶坛与批次历史关系
     * @return 陶坛与批次历史关系
     * 集合
     */
    List<LaoczBatchPotteryMappingHistory> selectLaoczBatchPotteryMappingHistoryList(LaoczBatchPotteryMappingHistory laoczBatchPotteryMappingHistory);

    /**
     * 批量新增陶坛与批次历史关系
     *
     * @param laoczBatchPotteryMappingHistoryList 陶坛与批次历史关系
     *                                            列表
     * @return 结果
     */
    int batchLaoczBatchPotteryMappingHistory(List<LaoczBatchPotteryMappingHistory> laoczBatchPotteryMappingHistoryList);

}
