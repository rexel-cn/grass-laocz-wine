package com.rexel.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rexel.system.domain.GrassDeviceInfo;
import com.rexel.system.domain.vo.GrassDeviceInfoVO;
import com.rexel.system.domain.vo.common.NumberCountVO;

import java.util.List;

/**
 * 设备信息Service接口
 *
 * @author grass-service
 * @date 2022-08-16
 */
public interface IGrassDeviceInfoService extends IService<GrassDeviceInfo> {

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
    Integer selectCountByDeviceName(String deviceName);

    /**
     * 根据deviceId 查询数量
     *
     * @param linkId
     * @return
     */
    Integer selectCountByLinkId(String linkId);


    /**
     * 设备下拉框
     *
     * @return
     */
    List<GrassDeviceInfoVO> dropDown();

    /**
     * 根据机器码 连接信息查询设备
     *
     * @param machineCode
     * @return
     */
    GrassDeviceInfo selectGrassDeviceInfoByMachineCode(String machineCode);

    /**
     * 根据设备id删除设备
     *
     * @param deviceId
     * @return
     */
    Boolean deleteByDeviceId(String deviceId);

    Boolean insertDeviceInfo(GrassDeviceInfo grassDeviceInfo);

    Boolean updateDeviceInfo(GrassDeviceInfo grassDeviceInfo);

    /**
     * 根据租户查询设备
     *
     * @param tenantId 设备信息
     * @return 设备信息集合
     */
    List<GrassDeviceInfo> selectGrassDeviceInfoListByTenantId(String tenantId);

    Boolean deleteByTenantId(String tenantId);

    /**
     * 物联设备统计
     * @param tenantId
     * @return
     */
    NumberCountVO deviceStatisticalByTenantId( );
}
