package com.rexel.laocz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.laocz.domain.LaoczWineHistory;
import com.rexel.laocz.mapper.LaoczWineHistoryMapper;
import com.rexel.laocz.service.ILaoczWineHistoryService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 酒历史Service业务层处理
 *
 * @author grass-service
 * @date 2024-03-12
 */
@Service
public class LaoczWineHistoryServiceImpl extends ServiceImpl<LaoczWineHistoryMapper, LaoczWineHistory> implements ILaoczWineHistoryService {


    /**
     * 查询酒历史列表
     *
     * @param laoczWineHistory 酒历史
     * @return 酒历史
     */
    @Override
    public List<LaoczWineHistory> selectLaoczWineHistoryList(LaoczWineHistory laoczWineHistory) {
        return baseMapper.selectLaoczWineHistoryList(laoczWineHistory);
    }

}
