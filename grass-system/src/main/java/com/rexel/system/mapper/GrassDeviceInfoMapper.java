package com.rexel.system.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rexel.system.domain.GrassDeviceInfo;
import com.rexel.system.domain.vo.common.NumberCountVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 设备信息Mapper接口
 *
 * @author grass-service
 * @date 2022-08-16
 */
@Repository
public interface GrassDeviceInfoMapper extends BaseMapper<GrassDeviceInfo> {
    /**
     * 查询设备信息列表
     *
     * @param grassDeviceInfo 设备信息
     * @return 设备信息集合
     */
    List<GrassDeviceInfo> selectGrassDeviceInfoList(GrassDeviceInfo grassDeviceInfo);

    /**
     * 根据deviceId 查询数量
     *
     * @param deviceId
     * @return
     */
    @InterceptorIgnore(tenantLine = "on")
    Integer selectCountByDeviceName(GrassDeviceInfo grassDeviceInfo);

    /**
     * 根据deviceId 查询数量
     *
     * @param linkId
     * @return
     */
    @InterceptorIgnore(tenantLine = "on")
    Integer selectCountByLinkId(String linkId);

    /**
     * 根据机器码 连接信息查询设备
     *
     * @param machineCode
     * @return
     */
    @InterceptorIgnore(tenantLine = "on")
    GrassDeviceInfo selectGrassDeviceInfoByMachineCode(String machineCode);

    /**
     * 根据设备id删除设备
     *
     * @param deviceId
     * @return
     */
    @InterceptorIgnore(tenantLine = "on")
    Boolean deleteByDeviceId(String deviceId);

    @InterceptorIgnore(tenantLine = "on")
    Boolean insertDeviceInfo(GrassDeviceInfo grassDeviceInfo);

    @InterceptorIgnore(tenantLine = "on")
    Boolean updateDeviceInfo(GrassDeviceInfo grassDeviceInfo);

    @InterceptorIgnore(tenantLine = "on")
    List<GrassDeviceInfo> selectGrassDeviceInfoListByTenantId(@Param("tenantId") String tenantId);

    @InterceptorIgnore(tenantLine = "on")
    Boolean deleteByTenantId(String tenantId);

    NumberCountVO deviceStatistical();
}
