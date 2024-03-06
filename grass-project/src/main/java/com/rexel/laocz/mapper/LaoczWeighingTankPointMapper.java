package com.rexel.laocz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rexel.laocz.domain.LaoczWeighingTankPoint;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 称重罐相关测点维护Mapper接口
 *
 * @author grass-service
 * @date 2024-03-06
 */
@Repository
public interface LaoczWeighingTankPointMapper extends BaseMapper<LaoczWeighingTankPoint> {
    /**
     * 查询称重罐相关测点维护列表
     *
     * @param laoczWeighingTankPoint 称重罐相关测点维护
     * @return 称重罐相关测点维护集合
     */
    List<LaoczWeighingTankPoint> selectLaoczWeighingTankPointList(LaoczWeighingTankPoint laoczWeighingTankPoint);

    /**
     * 批量新增称重罐相关测点维护
     *
     * @param laoczWeighingTankPointList 称重罐相关测点维护列表
     * @return 结果
     */
    int batchLaoczWeighingTankPoint(List<LaoczWeighingTankPoint> laoczWeighingTankPointList);

}
