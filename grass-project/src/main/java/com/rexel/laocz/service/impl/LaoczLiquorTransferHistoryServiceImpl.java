package com.rexel.laocz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.laocz.domain.LaoczLiquorTransferHistory;
import com.rexel.laocz.mapper.LaoczLiquorTransferHistoryMapper;
import com.rexel.laocz.service.ILaoczLiquorTransferHistoryService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 倒坛记录Service业务层处理
 *
 * @author grass-service
 * @date 2024-03-06
 */
@Service
public class LaoczLiquorTransferHistoryServiceImpl extends ServiceImpl<LaoczLiquorTransferHistoryMapper, LaoczLiquorTransferHistory> implements ILaoczLiquorTransferHistoryService {


    /**
     * 查询倒坛记录列表
     *
     * @param laoczLiquorTransferHistory 倒坛记录
     * @return 倒坛记录
     */
    @Override
    public List<LaoczLiquorTransferHistory> selectLaoczLiquorTransferHistoryList(LaoczLiquorTransferHistory laoczLiquorTransferHistory) {
        return baseMapper.selectLaoczLiquorTransferHistoryList(laoczLiquorTransferHistory);
    }

}
