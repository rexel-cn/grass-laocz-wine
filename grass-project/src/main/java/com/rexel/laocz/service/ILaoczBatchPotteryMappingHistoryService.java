package com.rexel.laocz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rexel.laocz.domain.LaoczBatchPotteryMappingHistory;

import java.util.List;

/**
 * 陶坛与批次历史关系
 * Service接口
 *
 * @author grass-service
 * @date 2024-03-06
 */
public interface ILaoczBatchPotteryMappingHistoryService extends IService<LaoczBatchPotteryMappingHistory> {

    /**
     * 查询陶坛与批次历史关系
     * 列表
     *
     * @param laoczBatchPotteryMappingHistory 陶坛与批次历史关系
     * @return 陶坛与批次历史关系
     * 集合
     */
    List<LaoczBatchPotteryMappingHistory> selectLaoczBatchPotteryMappingHistoryList(LaoczBatchPotteryMappingHistory laoczBatchPotteryMappingHistory);

}
