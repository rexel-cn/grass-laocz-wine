package com.rexel.laocz.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.laocz.domain.LaoczAreaInfo;
import com.rexel.laocz.domain.LaoczFireZoneInfo;
import com.rexel.laocz.domain.vo.FireZoneInfoVo;
import com.rexel.laocz.domain.vo.TreePullDownChildrenVO;
import com.rexel.laocz.domain.vo.TreePullDownVO;
import com.rexel.laocz.mapper.LaoczFireZoneInfoMapper;
import com.rexel.laocz.service.ILaoczAreaInfoService;
import com.rexel.laocz.service.ILaoczFireZoneInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
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
    private ILaoczAreaInfoService iLaoczAreaInfoService;
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

        List<FireZoneInfoVo> fireZoneInfoVos = baseMapper.selectLaoczFireZoneInfoListWithArea(laoczFireZoneInfo);

        return fireZoneInfoVos;
    }

    /**
     * 树状下拉框
     *
     * @return
     */
    @Override
    public List<TreePullDownVO> selectTreePullDown() {
        List<LaoczAreaInfo> laoczAreaInfoList = iLaoczAreaInfoService.lambdaQuery().list();
        List<LaoczFireZoneInfo> laoczFireZoneInfoList = this.lambdaQuery().list();
        Map<Long, List<LaoczFireZoneInfo>> fireZonesByAreaId = laoczFireZoneInfoList.stream().collect(Collectors.groupingBy(LaoczFireZoneInfo::getAreaId));
        return laoczAreaInfoList.stream().map(area -> {
            List<TreePullDownChildrenVO> children = fireZonesByAreaId.getOrDefault(area.getAreaId(), Collections.emptyList()).stream().map(fireZone -> new TreePullDownChildrenVO(fireZone.getFireZoneId(), fireZone.getFireZoneName())).collect(Collectors.toList());

            return new TreePullDownVO(area.getAreaId(), area.getAreaName(), children);
        }).collect(Collectors.toList());
    }

}
