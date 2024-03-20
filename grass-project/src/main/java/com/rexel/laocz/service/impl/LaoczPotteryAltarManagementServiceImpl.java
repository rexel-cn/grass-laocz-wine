package com.rexel.laocz.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.common.exception.ServiceException;
import com.rexel.laocz.domain.LaoczAreaInfo;
import com.rexel.laocz.domain.LaoczFireZoneInfo;
import com.rexel.laocz.domain.LaoczPotteryAltarManagement;
import com.rexel.laocz.domain.vo.PotteryAltarVo;
import com.rexel.laocz.mapper.LaoczPotteryAltarManagementMapper;
import com.rexel.laocz.service.ILaoczAreaInfoService;
import com.rexel.laocz.service.ILaoczFireZoneInfoService;
import com.rexel.laocz.service.ILaoczPotteryAltarManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 陶坛管理Service业务层处理
 *
 * @author grass-service
 * @date 2024-03-06
 */
@Service
public class LaoczPotteryAltarManagementServiceImpl extends ServiceImpl<LaoczPotteryAltarManagementMapper, LaoczPotteryAltarManagement> implements ILaoczPotteryAltarManagementService {

    @Autowired
    private ILaoczFireZoneInfoService iLaoczFireZoneInfoService;

    @Autowired
    private ILaoczAreaInfoService iLaoczAreaInfoService;

    /**
     * 查询陶坛管理列表
     *
     * @param laoczPotteryAltarManagement 陶坛管理
     * @return 陶坛管理
     */
    @Override
    public List<LaoczPotteryAltarManagement> selectLaoczPotteryAltarManagementList(LaoczPotteryAltarManagement laoczPotteryAltarManagement) {
        return baseMapper.selectLaoczPotteryAltarManagementList(laoczPotteryAltarManagement);
    }

    @Override
    public List<PotteryAltarVo> selectLaoczPotteryAltarManagementListDetail(LaoczPotteryAltarManagement laoczPotteryAltarManagement) {
        List<LaoczPotteryAltarManagement> laoczPotteryAltarManagements = baseMapper.selectLaoczPotteryAltarManagementList(laoczPotteryAltarManagement);


        List<PotteryAltarVo> list = laoczPotteryAltarManagements.stream().map((item) -> {

            PotteryAltarVo potteryAltarVo = new PotteryAltarVo();

            Long fireZoneId = item.getFireZoneId();

            LaoczFireZoneInfo laoczFireZoneInfo = iLaoczFireZoneInfoService.getById(fireZoneId);

            LaoczAreaInfo laoczAreaInfo = iLaoczAreaInfoService.getById(laoczFireZoneInfo.getAreaId());

            BeanUtil.copyProperties(item, potteryAltarVo);

            potteryAltarVo.setAreaName(laoczAreaInfo.getAreaName());
            potteryAltarVo.setFireZoneName(laoczFireZoneInfo.getFireZoneName());

            return potteryAltarVo;
        }).collect(Collectors.toList());

        return list;
    }

    @Override
    public PotteryAltarVo selectLaoczPotteryAltarManagement(Long potteryAltarId) {

        PotteryAltarVo potteryAltarVo = new PotteryAltarVo();

        LaoczPotteryAltarManagement laoczPotteryAltarManagement = this.getById(potteryAltarId);

        Long fireZoneId = laoczPotteryAltarManagement.getFireZoneId();

        LaoczFireZoneInfo laoczFireZoneInfo = iLaoczFireZoneInfoService.getById(fireZoneId);

        LaoczAreaInfo laoczAreaInfo = iLaoczAreaInfoService.getById(laoczFireZoneInfo.getAreaId());

        BeanUtil.copyProperties(laoczPotteryAltarManagement, potteryAltarVo);

        potteryAltarVo.setAreaName(laoczAreaInfo.getAreaName());
        potteryAltarVo.setFireZoneName(laoczFireZoneInfo.getFireZoneName());

        return potteryAltarVo;
    }

    @Override
    public boolean addPotteryAltar(LaoczPotteryAltarManagement laoczPotteryAltarManagement) {

        QueryWrapper<LaoczPotteryAltarManagement> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("pottery_altar_number", laoczPotteryAltarManagement.getPotteryAltarNumber());

        int count = this.count(queryWrapper);

        if (count > 0) {
            throw new ServiceException("陶坛编号已存在");
        } else {
            return this.save(laoczPotteryAltarManagement);
        }
    }

    @Override
    public boolean updateByIdWithPotteryAltar(LaoczPotteryAltarManagement laoczPotteryAltarManagement) {

        QueryWrapper<LaoczPotteryAltarManagement> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("pottery_altar_number", laoczPotteryAltarManagement.getPotteryAltarNumber());

        int count = this.count(queryWrapper);

        if (count > 0) {
            throw new ServiceException("陶坛编号已存在");
        } else {
            return updateById(laoczPotteryAltarManagement);
        }
    }
}
