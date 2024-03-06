package com.rexel.laocz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rexel.laocz.domain.LaoczWeighingTankPoint;

import java.util.List;

/**
 * 称重罐相关测点维护Service接口
 *
 * @author grass-service
 * @date 2024-03-06
 */
public interface ILaoczWeighingTankPointService extends IService<LaoczWeighingTankPoint> {

    /**
     * 查询称重罐相关测点维护列表
     *
     * @param laoczWeighingTankPoint 称重罐相关测点维护
     * @return 称重罐相关测点维护集合
     */
    List<LaoczWeighingTankPoint> selectLaoczWeighingTankPointList(LaoczWeighingTankPoint laoczWeighingTankPoint);

}
