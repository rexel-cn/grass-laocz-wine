package com.rexel.system.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rexel.common.core.domain.entity.GrassAssetPoint;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 资产测点关联Mapper接口
 *
 * @author grass-service
 * @date 2022-07-19
 */
@Repository
public interface GrassAssetPointMapper extends BaseMapper<GrassAssetPoint> {
    /**
     * 查询资产测点关联列表
     *
     * @param grassAssetPoint 资产测点关联
     * @return 资产测点关联集合
     */
    List<GrassAssetPoint> selectGrassAssetPointList(GrassAssetPoint grassAssetPoint);

    /**
     * 删除租户下的数据
     */
    @InterceptorIgnore(tenantLine = "on")
    Boolean deleteToTenant(String tenantId);


    /**
     * 根据物联设备id查询其测点关联资产的数量
     *
     * @param deviceId
     * @return
     */
    Integer selectAssetPointCountByDeviceId(String deviceId);
}
