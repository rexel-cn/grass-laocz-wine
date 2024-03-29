package com.rexel.laocz.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.laocz.domain.LaoczAreaInfo;
import com.rexel.laocz.domain.LaoczFireZoneInfo;
import com.rexel.laocz.domain.vo.AreaVo;
import com.rexel.laocz.mapper.LaoczAreaInfoMapper;
import com.rexel.laocz.service.ILaoczAreaInfoService;
import com.rexel.laocz.service.ILaoczFireZoneInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 场区信息Service业务层处理
 *
 * @author grass-service
 * @date 2024-03-06
 */
@Service
public class LaoczAreaInfoServiceImpl extends ServiceImpl<LaoczAreaInfoMapper, LaoczAreaInfo> implements ILaoczAreaInfoService {

    @Autowired
    private ILaoczFireZoneInfoService iLaoczFireZoneInfoService;

    /**
     * 查询场区信息列表
     *
     * @param laoczAreaInfo 场区信息
     * @return 场区信息
     */
    @Override
    public List<LaoczAreaInfo> selectLaoczAreaInfoList(LaoczAreaInfo laoczAreaInfo) {
        return baseMapper.selectLaoczAreaInfoList(laoczAreaInfo);
    }

    @Override
    public List<AreaVo> dropDown() {

        ArrayList<AreaVo> list = new ArrayList<>();

        List<LaoczAreaInfo> laoczAreaInfos = this.selectLaoczAreaInfoList(null);

        if (laoczAreaInfos.isEmpty()) {
            return list;
        }

        for (LaoczAreaInfo laoczAreaInfo : laoczAreaInfos) {
            AreaVo areaVo = new AreaVo();
            BeanUtil.copyProperties(laoczAreaInfo,areaVo);
            list.add(areaVo);
        }
        return list;
    }

    @Override
    public List<LaoczFireZoneInfo> getByIdWithfireZoneName(Long id) {
        QueryWrapper<LaoczFireZoneInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("area_id",id);
        List<LaoczFireZoneInfo> list = iLaoczFireZoneInfoService.list(queryWrapper);

        return list;
    }

}
