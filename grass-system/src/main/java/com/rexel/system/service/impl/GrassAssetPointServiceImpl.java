package com.rexel.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.common.core.domain.entity.GrassAssetPoint;
import com.rexel.common.exception.CustomException;
import com.rexel.system.domain.GrassPointInfo;
import com.rexel.system.domain.vo.GrassAssetPointInfoVO;
import com.rexel.system.mapper.GrassAssetPointMapper;
import com.rexel.system.service.IGrassAssetPointService;
import com.rexel.system.service.IGrassPointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 资产测点关联Service业务层处理
 *
 * @author grass-service
 * @date 2022-07-19
 */
@Service
public class GrassAssetPointServiceImpl extends ServiceImpl<GrassAssetPointMapper, GrassAssetPoint> implements IGrassAssetPointService {
    @Autowired
    private IGrassPointService grassPointService;

    /**
     * 查询资产测点关联列表
     *
     * @param grassAssetPoint 资产测点关联
     * @return 资产测点关联
     */
    @Override
    public List<GrassAssetPoint> selectGrassAssetPointList(GrassAssetPoint grassAssetPoint) {
        return baseMapper.selectGrassAssetPointList(grassAssetPoint);
    }

    @Override
    public Boolean deleteToTenant(String tenantId) {
        return baseMapper.deleteToTenant(tenantId);
    }

    @Override
    public Integer selectAssetPointCountByDeviceId(String deviceId) {
        return baseMapper.selectAssetPointCountByDeviceId(deviceId);
    }

    /**
     * 根据设备id查询测点
     *
     * @param id 设备ID
     * @return
     */
    @Override
    public List<GrassAssetPointInfoVO> selectGrassAssetPointsByAssetId(String id) {
        try {
            List<GrassAssetPoint> grassAssetPointList = this.lambdaQuery()
                    .eq(GrassAssetPoint::getAssetId, id)
                    .list();
            List<Long> pointIdList = grassAssetPointList.stream()
                    .map(GrassAssetPoint::getPointPrimaryKey)
                    .collect(Collectors.toList());
            List<GrassPointInfo> grassPointInfoList = grassPointService.lambdaQuery()
                    .in(GrassPointInfo::getId, pointIdList)
                    .list();
            List<GrassAssetPointInfoVO> grassAssetPointInfoVOList = new ArrayList<>();
            for (GrassPointInfo grassPointInfo : grassPointInfoList) {
                GrassAssetPointInfoVO grassAssetPointInfoVO = new GrassAssetPointInfoVO();
                grassAssetPointInfoVO.setPointId(grassPointInfo.getPointId());
                grassAssetPointInfoVO.setDeviceId(grassPointInfo.getDeviceId());
                grassAssetPointInfoVO.setPointName(grassPointInfo.getPointName());
                grassAssetPointInfoVOList.add(grassAssetPointInfoVO);
            }
            return grassAssetPointInfoVOList;
        } catch (Exception e) {
            throw new CustomException("查询失败");
        }
    }

}
