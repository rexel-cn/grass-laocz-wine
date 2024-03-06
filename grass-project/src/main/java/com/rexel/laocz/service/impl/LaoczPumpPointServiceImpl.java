package com.rexel.laocz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.laocz.domain.LaoczPumpPoint;
import com.rexel.laocz.mapper.LaoczPumpPointMapper;
import com.rexel.laocz.service.ILaoczPumpPointService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 泵相关测点维护Service业务层处理
 *
 * @author grass-service
 * @date 2024-03-06
 */
@Service
public class LaoczPumpPointServiceImpl extends ServiceImpl<LaoczPumpPointMapper, LaoczPumpPoint> implements ILaoczPumpPointService {


    /**
     * 查询泵相关测点维护列表
     *
     * @param laoczPumpPoint 泵相关测点维护
     * @return 泵相关测点维护
     */
    @Override
    public List<LaoczPumpPoint> selectLaoczPumpPointList(LaoczPumpPoint laoczPumpPoint) {
        return baseMapper.selectLaoczPumpPointList(laoczPumpPoint);
    }

}
