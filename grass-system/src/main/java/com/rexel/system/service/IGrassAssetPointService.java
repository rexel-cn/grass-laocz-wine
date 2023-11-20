package com.rexel.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rexel.common.core.domain.entity.GrassAssetPoint;
import com.rexel.system.domain.vo.GrassAssetPointInfoVO;

import java.util.List;

/**
 * 资产测点关联Service接口
 *
 * @author grass-service
 * @date 2022-07-19
 */
public interface IGrassAssetPointService extends IService<GrassAssetPoint> {

    /**
     * 查询资产测点关联列表
     *
     * @param grassAssetPoint 资产测点关联
     * @return 资产测点关联集合
     */
    public List<GrassAssetPoint> selectGrassAssetPointList(GrassAssetPoint grassAssetPoint);

    /**
     *
     */
    Boolean deleteToTenant(String tenantId);


    Integer selectAssetPointCountByDeviceId(String deviceId);

    /**
     * 根据设备id查询测点
     * @param id 设备ID
     * @return
     */
    List<GrassAssetPointInfoVO> selectGrassAssetPointsByAssetId(String id);
}
