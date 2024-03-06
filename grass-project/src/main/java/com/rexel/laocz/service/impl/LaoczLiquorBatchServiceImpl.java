package com.rexel.laocz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.laocz.domain.LaoczLiquorBatch;
import com.rexel.laocz.mapper.LaoczLiquorBatchMapper;
import com.rexel.laocz.service.ILaoczLiquorBatchService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 酒液批次相关信息Service业务层处理
 *
 * @author grass-service
 * @date 2024-03-06
 */
@Service
public class LaoczLiquorBatchServiceImpl extends ServiceImpl<LaoczLiquorBatchMapper, LaoczLiquorBatch> implements ILaoczLiquorBatchService {


    /**
     * 查询酒液批次相关信息列表
     *
     * @param laoczLiquorBatch 酒液批次相关信息
     * @return 酒液批次相关信息
     */
    @Override
    public List<LaoczLiquorBatch> selectLaoczLiquorBatchList(LaoczLiquorBatch laoczLiquorBatch) {
        return baseMapper.selectLaoczLiquorBatchList(laoczLiquorBatch);
    }

}
