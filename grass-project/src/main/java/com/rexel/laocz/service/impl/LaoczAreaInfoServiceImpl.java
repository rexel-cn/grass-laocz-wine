package com.rexel.laocz.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.common.exception.ServiceException;
import com.rexel.laocz.domain.LaoczAreaInfo;
import com.rexel.laocz.domain.LaoczFireZoneInfo;
import com.rexel.laocz.domain.vo.AreaVo;
import com.rexel.laocz.domain.vo.FireZoneInfoVo;
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
        List<LaoczAreaInfo> laoczAreaInfos = this.selectLaoczAreaInfoList(null);
        if (laoczAreaInfos.isEmpty()) {
            return new ArrayList<>();
        }
        return BeanUtil.copyToList(laoczAreaInfos, AreaVo.class);
    }

    @Override
    public List<LaoczFireZoneInfo> getByIdWithfireZoneName(Long id) {
        LambdaQueryWrapper<LaoczFireZoneInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LaoczFireZoneInfo::getAreaId,id);
        queryWrapper.orderByAsc(LaoczFireZoneInfo::getDispalyOrder);
        return iLaoczFireZoneInfoService.list(queryWrapper);
    }

    /**
     * 新增场区
     * @param laoczAreaInfo 场区信息
     * @return
     */
    @Override
    public boolean addLaoczAreaInfo(LaoczAreaInfo laoczAreaInfo) {
        //判断场区编号是否已存在
        QueryWrapper<LaoczAreaInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("area_name",laoczAreaInfo.getAreaName());

        int count = this.count(wrapper);
        if (count > 0){
            throw new ServiceException("场区编号已存在");
        }
        return this.save(laoczAreaInfo);
    }

    /**
     * 修改场区
     *
     * @param laoczAreaInfo 场区信息
     * @return 返回
     */
    @Override
    public Boolean updateLaoczAreaInfo(LaoczAreaInfo laoczAreaInfo) {
        //判断场区是否存在
        LaoczAreaInfo byId = this.getById(laoczAreaInfo.getAreaId());
        if (byId == null) {
            throw new ServiceException("场区不存在");
        }
        //判断场区编号是否已存在,排除自己
        QueryWrapper<LaoczAreaInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("area_name", laoczAreaInfo.getAreaName());
        wrapper.ne("area_id", laoczAreaInfo.getAreaId());

        int count = this.count(wrapper);
        if (count > 0) {
            throw new ServiceException("场区编号已存在");
        }
        return this.updateById(laoczAreaInfo);
    }

    /**
     * 联查全部防火区+场区
     */
    @Override
    public List<FireZoneInfoVo> getAreaFire() {
        return baseMapper.selectAreaFire();
    }

    /**
     * 根据id删除
     *
     * @param areaId 场区id
     * @return 返回
     */
    @Override
    public Boolean deleteLaoczAreaInfoById(Long areaId) {
        //判断场区是否存在
        LaoczAreaInfo byId = this.getById(areaId);
        if (byId == null) {
            throw new ServiceException("场区不存在");
        }
        //判断是否有防火区
        Integer count = iLaoczFireZoneInfoService.lambdaQuery().eq(LaoczFireZoneInfo::getAreaId, areaId).count();
        if (count > 0) {
            throw new ServiceException("该场区下有防火区，无法删除");
        }
        return this.removeById(areaId);
    }
}
