package com.rexel.laocz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rexel.laocz.domain.LaoczFireZoneInfo;
import com.rexel.laocz.domain.dto.AreaFireZoneNameDTO;
import com.rexel.laocz.domain.vo.AreaFireZoneInfo;
import com.rexel.laocz.domain.vo.FireZoneInfoVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 防火区信息Mapper接口
 *
 * @author grass-service
 * @date 2024-03-06
 */
@Repository
public interface LaoczFireZoneInfoMapper extends BaseMapper<LaoczFireZoneInfo> {
    /**
     * 查询防火区信息列表
     *
     * @param laoczFireZoneInfo 防火区信息
     * @return 防火区信息集合
     */
    List<LaoczFireZoneInfo> selectLaoczFireZoneInfoList(LaoczFireZoneInfo laoczFireZoneInfo);

    /**
     * 批量新增防火区信息
     *
     * @param laoczFireZoneInfoList 防火区信息列表
     * @return 结果
     */
    int batchLaoczFireZoneInfo(List<LaoczFireZoneInfo> laoczFireZoneInfoList);

    List<FireZoneInfoVo> selectLaoczFireZoneInfoListWithArea(LaoczFireZoneInfo laoczFireZoneInfo);

    /**
     * 根据防火区，场区名称查询防火区信息
     *
     * @param areaFireZoneNameDTO 防火区，场区名称
     * @return 防火区信息
     */
    List<AreaFireZoneInfo> selectAreaFireZoneInfoList(List<AreaFireZoneNameDTO> areaFireZoneNameDTO);

}
