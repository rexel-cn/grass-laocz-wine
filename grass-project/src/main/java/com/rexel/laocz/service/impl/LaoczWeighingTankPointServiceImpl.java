package com.rexel.laocz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.laocz.domain.LaoczWeighingTankPoint;
import com.rexel.laocz.mapper.LaoczWeighingTankPointMapper;
import com.rexel.laocz.service.ILaoczWeighingTankPointService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 称重罐相关测点维护Service业务层处理
 *
 * @author grass-service
 * @date 2024-03-06
 */
@Service
public class LaoczWeighingTankPointServiceImpl extends ServiceImpl<LaoczWeighingTankPointMapper, LaoczWeighingTankPoint> implements ILaoczWeighingTankPointService {


    /**
     * 查询称重罐相关测点维护列表
     *
     * @param laoczWeighingTankPoint 称重罐相关测点维护
     * @return 称重罐相关测点维护
     */
    @Override
    public List<LaoczWeighingTankPoint> selectLaoczWeighingTankPointList(LaoczWeighingTankPoint laoczWeighingTankPoint) {
        return baseMapper.selectLaoczWeighingTankPointList(laoczWeighingTankPoint);
    }

}
