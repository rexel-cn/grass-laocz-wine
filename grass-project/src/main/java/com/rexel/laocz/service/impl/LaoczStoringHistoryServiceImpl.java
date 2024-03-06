package com.rexel.laocz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.laocz.domain.LaoczStoringHistory;
import com.rexel.laocz.mapper.LaoczStoringHistoryMapper;
import com.rexel.laocz.service.ILaoczStoringHistoryService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 入酒记录Service业务层处理
 *
 * @author grass-service
 * @date 2024-03-06
 */
@Service
public class LaoczStoringHistoryServiceImpl extends ServiceImpl<LaoczStoringHistoryMapper, LaoczStoringHistory> implements ILaoczStoringHistoryService {


    /**
     * 查询入酒记录列表
     *
     * @param laoczStoringHistory 入酒记录
     * @return 入酒记录
     */
    @Override
    public List<LaoczStoringHistory> selectLaoczStoringHistoryList(LaoczStoringHistory laoczStoringHistory) {
        return baseMapper.selectLaoczStoringHistoryList(laoczStoringHistory);
    }

}
