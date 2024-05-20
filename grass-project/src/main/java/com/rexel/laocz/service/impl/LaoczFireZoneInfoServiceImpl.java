package com.rexel.laocz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.common.exception.ServiceException;
import com.rexel.laocz.domain.LaoczAreaInfo;
import com.rexel.laocz.domain.LaoczFireZoneInfo;
import com.rexel.laocz.domain.LaoczPotteryAltarManagement;
import com.rexel.laocz.domain.vo.FireZoneInfoVo;
import com.rexel.laocz.domain.vo.TreePullDownChildrenVO;
import com.rexel.laocz.domain.vo.TreePullDownVO;
import com.rexel.laocz.mapper.LaoczFireZoneInfoMapper;
import com.rexel.laocz.service.ILaoczAreaInfoService;
import com.rexel.laocz.service.ILaoczFireZoneInfoService;
import com.rexel.laocz.service.ILaoczPotteryAltarManagementService;
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
    private ILaoczPotteryAltarManagementService iLaoczPotteryAltarManagementService;


    /**
     * 查询防火区信息列表
     *
     * @param laoczFireZoneInfo 防火区信息
     * @return 防火区信息
     */
    @Override
    public List<FireZoneInfoVo> selectLaoczFireZoneInfoList(LaoczFireZoneInfo laoczFireZoneInfo) {
        return baseMapper.selectLaoczFireZoneInfoListWithArea(laoczFireZoneInfo);
    }

    /**
     * 保存防火区信息
     *
     * @param laoczFireZoneInfo 防火区信息
     * @return 返回
     */
    @Override
    public Boolean saveFireZoneInfo(LaoczFireZoneInfo laoczFireZoneInfo) {
        lambdaQuery().eq(LaoczFireZoneInfo::getFireZoneName, laoczFireZoneInfo.getFireZoneName())
                .eq(LaoczFireZoneInfo::getAreaId, laoczFireZoneInfo.getAreaId())
                .oneOpt().ifPresent(fireZone -> {
                    throw new ServiceException("防火区名称已存在");
                });
        return this.save(laoczFireZoneInfo);
    }

    /**
     * 修改防火区信息
     *
     * @param laoczFireZoneInfo 防火区信息
     * @return 返回
     */
    @Override
    public Boolean updateFireZoneInfo(LaoczFireZoneInfo laoczFireZoneInfo) {
        LaoczFireZoneInfo fireZoneInfo = this.getById(laoczFireZoneInfo.getFireZoneId());
        if (fireZoneInfo == null) {
            throw new ServiceException("防火区不存在");
        }
        if (!fireZoneInfo.getFireZoneName().equals(laoczFireZoneInfo.getFireZoneName())) {
            lambdaQuery().eq(LaoczFireZoneInfo::getFireZoneName, laoczFireZoneInfo.getFireZoneName())
                    .eq(LaoczFireZoneInfo::getAreaId, laoczFireZoneInfo.getAreaId())
                    .ne(LaoczFireZoneInfo::getFireZoneId, laoczFireZoneInfo.getFireZoneId())
                    .oneOpt().ifPresent(fireZone -> {
                        throw new ServiceException("防火区名称已存在");
                    });
        }
        return this.updateById(laoczFireZoneInfo);
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

    @Override
    public Long findFireZoneId(String areaName, String fireZoneName) {
        // 获取归属区域Id
        QueryWrapper<LaoczAreaInfo> areaWrapper = new QueryWrapper<>();
        areaWrapper.eq("area_name", areaName);
        LaoczAreaInfo laoczAreaInfo = iLaoczAreaInfoService.getOne(areaWrapper);
        // 获取防火区Id
        if (laoczAreaInfo != null) {
            QueryWrapper<LaoczFireZoneInfo> zoneInfoQueryWrapper = new QueryWrapper<>();
            zoneInfoQueryWrapper.eq("area_id", laoczAreaInfo.getAreaId())
                    .eq("fire_zone_name", fireZoneName);
            LaoczFireZoneInfo fireZoneInfo = this.getOne(zoneInfoQueryWrapper);
             return fireZoneInfo.getFireZoneId();
        }
        throw new ServiceException("防火区Id不存在");
    }

    /**
     * 删除防火区信息
     *
     * @param fireZoneId 防火区id
     * @return 返回
     */
    @Override
    public Boolean deleteLaoczFireZoneInfoById(Long fireZoneId) {
        LaoczFireZoneInfo byId = this.getById(fireZoneId);
        if (byId == null) {
            throw new ServiceException("防火区不存在");
        }
        //查询是否有陶坛关联使用
        Integer count = iLaoczPotteryAltarManagementService.lambdaQuery().eq(LaoczPotteryAltarManagement::getFireZoneId, fireZoneId).count();
        if (count > 0) {
            throw new ServiceException("该防火区下有陶坛，无法删除");
        }
        return this.removeById(fireZoneId);
    }

}
