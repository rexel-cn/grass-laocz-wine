package com.rexel.laocz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.laocz.domain.LaoczBatchPotteryMapping;
import com.rexel.laocz.mapper.LaoczBatchPotteryMappingMapper;
import com.rexel.laocz.service.ILaoczBatchPotteryMappingService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 陶坛与批次实时关系Service业务层处理
 *
 * @author grass-service
 * @date 2024-03-06
 */
@Service
public class LaoczBatchPotteryMappingServiceImpl extends ServiceImpl<LaoczBatchPotteryMappingMapper, LaoczBatchPotteryMapping> implements ILaoczBatchPotteryMappingService {


    /**
     * 查询陶坛与批次实时关系列表
     *
     * @param laoczBatchPotteryMapping 陶坛与批次实时关系
     * @return 陶坛与批次实时关系
     */
    @Override
    public List<LaoczBatchPotteryMapping> selectLaoczBatchPotteryMappingList(LaoczBatchPotteryMapping laoczBatchPotteryMapping) {
        return baseMapper.selectLaoczBatchPotteryMappingList(laoczBatchPotteryMapping);
    }

}
