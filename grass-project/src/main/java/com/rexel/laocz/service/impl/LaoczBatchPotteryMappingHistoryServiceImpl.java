package com.rexel.laocz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.laocz.domain.LaoczBatchPotteryMappingHistory;
import com.rexel.laocz.mapper.LaoczBatchPotteryMappingHistoryMapper;
import com.rexel.laocz.service.ILaoczBatchPotteryMappingHistoryService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 陶坛与批次历史关系
 * Service业务层处理
 *
 * @author grass-service
 * @date 2024-03-06
 */
@Service
public class LaoczBatchPotteryMappingHistoryServiceImpl extends ServiceImpl<LaoczBatchPotteryMappingHistoryMapper, LaoczBatchPotteryMappingHistory> implements ILaoczBatchPotteryMappingHistoryService {


    /**
     * 查询陶坛与批次历史关系
     * 列表
     *
     * @param laoczBatchPotteryMappingHistory 陶坛与批次历史关系
     * @return 陶坛与批次历史关系
     */
    @Override
    public List<LaoczBatchPotteryMappingHistory> selectLaoczBatchPotteryMappingHistoryList(LaoczBatchPotteryMappingHistory laoczBatchPotteryMappingHistory) {
        return baseMapper.selectLaoczBatchPotteryMappingHistoryList(laoczBatchPotteryMappingHistory);
    }

}
