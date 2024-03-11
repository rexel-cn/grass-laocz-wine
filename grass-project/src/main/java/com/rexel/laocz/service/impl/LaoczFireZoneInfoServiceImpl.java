package com.rexel.laocz.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.laocz.domain.LaoczAreaInfo;
import com.rexel.laocz.domain.LaoczFireZoneInfo;
import com.rexel.laocz.mapper.LaoczFireZoneInfoMapper;
import com.rexel.laocz.service.ILaoczFireZoneInfoService;
import com.rexel.laocz.vo.FireZoneInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 防火区信息Service业务层处理
 *
 * @author grass-service
 * @date 2024-03-06
 */
@Service
public class LaoczFireZoneInfoServiceImpl extends ServiceImpl<LaoczFireZoneInfoMapper, LaoczFireZoneInfo> implements ILaoczFireZoneInfoService {

    @Autowired
    LaoczAreaInfoServiceImpl laoczAreaInfoService;

    /**
     * 查询防火区信息列表
     *
     * @param laoczFireZoneInfo 防火区信息
     * @return 防火区信息
     */
    @Override
    public List<FireZoneInfoVo> selectLaoczFireZoneInfoList(LaoczFireZoneInfo laoczFireZoneInfo) {

        List<LaoczFireZoneInfo> laoczFireZoneInfos = baseMapper.selectLaoczFireZoneInfoList(laoczFireZoneInfo);

        List<FireZoneInfoVo> list = laoczFireZoneInfos.stream().map((item) -> {
            FireZoneInfoVo fireZoneInfoVo = new FireZoneInfoVo();

            BeanUtil.copyProperties(item, fireZoneInfoVo);

            LaoczAreaInfo laoczAreaInfo = laoczAreaInfoService.getById(item.getAreaId());

            if (laoczAreaInfo != null) {
                String areaName = laoczAreaInfo.getAreaName();
                fireZoneInfoVo.setAreaName(areaName);
            }
            return fireZoneInfoVo;
        }).collect(Collectors.toList());

        return list;
    }

}
