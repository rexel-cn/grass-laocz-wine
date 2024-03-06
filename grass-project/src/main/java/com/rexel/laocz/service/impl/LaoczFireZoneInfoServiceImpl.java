package com.rexel.laocz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.laocz.domain.LaoczFireZoneInfo;
import com.rexel.laocz.mapper.LaoczFireZoneInfoMapper;
import com.rexel.laocz.service.ILaoczFireZoneInfoService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 防火区信息Service业务层处理
 *
 * @author grass-service
 * @date 2024-03-06
 */
@Service
public class LaoczFireZoneInfoServiceImpl extends ServiceImpl<LaoczFireZoneInfoMapper, LaoczFireZoneInfo> implements ILaoczFireZoneInfoService {


    /**
     * 查询防火区信息列表
     *
     * @param laoczFireZoneInfo 防火区信息
     * @return 防火区信息
     */
    @Override
    public List<LaoczFireZoneInfo> selectLaoczFireZoneInfoList(LaoczFireZoneInfo laoczFireZoneInfo) {
        return baseMapper.selectLaoczFireZoneInfoList(laoczFireZoneInfo);
    }

}
