package com.rexel.laocz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.laocz.domain.LaoczWeighingTank;
import com.rexel.laocz.mapper.LaoczWeighingTankMapper;
import com.rexel.laocz.service.ILaoczWeighingTankService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 称重罐管理Service业务层处理
 *
 * @author grass-service
 * @date 2024-03-06
 */
@Service
public class LaoczWeighingTankServiceImpl extends ServiceImpl<LaoczWeighingTankMapper, LaoczWeighingTank> implements ILaoczWeighingTankService {


    /**
     * 查询称重罐管理列表
     *
     * @param laoczWeighingTank 称重罐管理
     * @return 称重罐管理
     */
    @Override
    public List<LaoczWeighingTank> selectLaoczWeighingTankList(LaoczWeighingTank laoczWeighingTank) {
        return baseMapper.selectLaoczWeighingTankList(laoczWeighingTank);
    }

}
