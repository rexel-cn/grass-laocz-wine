package com.rexel.laocz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.laocz.domain.LaoczOutLiquorHistory;
import com.rexel.laocz.mapper.LaoczOutLiquorHistoryMapper;
import com.rexel.laocz.service.ILaoczOutLiquorHistoryService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 出酒记录Service业务层处理
 *
 * @author grass-service
 * @date 2024-03-06
 */
@Service
public class LaoczOutLiquorHistoryServiceImpl extends ServiceImpl<LaoczOutLiquorHistoryMapper, LaoczOutLiquorHistory> implements ILaoczOutLiquorHistoryService {


    /**
     * 查询出酒记录列表
     *
     * @param laoczOutLiquorHistory 出酒记录
     * @return 出酒记录
     */
    @Override
    public List<LaoczOutLiquorHistory> selectLaoczOutLiquorHistoryList(LaoczOutLiquorHistory laoczOutLiquorHistory) {
        return baseMapper.selectLaoczOutLiquorHistoryList(laoczOutLiquorHistory);
    }

}
