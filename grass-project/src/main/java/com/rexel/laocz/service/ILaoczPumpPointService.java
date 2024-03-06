package com.rexel.laocz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rexel.laocz.domain.LaoczPumpPoint;

import java.util.List;

/**
 * 泵相关测点维护Service接口
 *
 * @author grass-service
 * @date 2024-03-06
 */
public interface ILaoczPumpPointService extends IService<LaoczPumpPoint> {

    /**
     * 查询泵相关测点维护列表
     *
     * @param laoczPumpPoint 泵相关测点维护
     * @return 泵相关测点维护集合
     */
    List<LaoczPumpPoint> selectLaoczPumpPointList(LaoczPumpPoint laoczPumpPoint);

}
