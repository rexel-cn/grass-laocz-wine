package com.rexel.laocz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rexel.laocz.domain.LaoczPumpPoint;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 泵相关测点维护Mapper接口
 *
 * @author grass-service
 * @date 2024-03-06
 */
@Repository
public interface LaoczPumpPointMapper extends BaseMapper<LaoczPumpPoint> {
    /**
     * 查询泵相关测点维护列表
     *
     * @param laoczPumpPoint 泵相关测点维护
     * @return 泵相关测点维护集合
     */
    List<LaoczPumpPoint> selectLaoczPumpPointList(LaoczPumpPoint laoczPumpPoint);

    /**
     * 批量新增泵相关测点维护
     *
     * @param laoczPumpPointList 泵相关测点维护列表
     * @return 结果
     */
    int batchLaoczPumpPoint(List<LaoczPumpPoint> laoczPumpPointList);

}
