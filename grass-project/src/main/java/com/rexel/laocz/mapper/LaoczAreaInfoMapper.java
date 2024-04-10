package com.rexel.laocz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rexel.laocz.domain.LaoczAreaInfo;
import com.rexel.laocz.domain.vo.FireZoneInfoVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 场区信息Mapper接口
 *
 * @author grass-service
 * @date 2024-03-06
 */
@Repository
public interface LaoczAreaInfoMapper extends BaseMapper<LaoczAreaInfo> {
    /**
     * 查询场区信息列表
     *
     * @param laoczAreaInfo 场区信息
     * @return 场区信息集合
     */
    List<LaoczAreaInfo> selectLaoczAreaInfoList(LaoczAreaInfo laoczAreaInfo);

    /**
     * 批量新增场区信息
     *
     * @param laoczAreaInfoList 场区信息列表
     * @return 结果
     */
    int batchLaoczAreaInfo(List<LaoczAreaInfo> laoczAreaInfoList);
    /**
     * 联查全部防火区+场区
     */
    List<FireZoneInfoVo> selectAreaFire();
}
