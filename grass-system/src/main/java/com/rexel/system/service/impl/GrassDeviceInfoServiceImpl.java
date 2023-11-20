package com.rexel.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.common.utils.SecurityUtils;
import com.rexel.system.domain.GrassDeviceInfo;
import com.rexel.system.domain.vo.GrassDeviceInfoVO;
import com.rexel.system.domain.vo.common.NumberCountVO;
import com.rexel.system.mapper.GrassDeviceInfoMapper;
import com.rexel.system.service.IGrassDeviceInfoService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 设备信息Service业务层处理
 *
 * @author grass-service
 * @date 2022-08-16
 */
@Service
public class GrassDeviceInfoServiceImpl extends ServiceImpl<GrassDeviceInfoMapper, GrassDeviceInfo> implements IGrassDeviceInfoService {


    /**
     * 查询设备信息列表
     *
     * @param grassDeviceInfo 设备信息
     * @return 设备信息
     */
    @Override
    public List<GrassDeviceInfo> selectGrassDeviceInfoList(GrassDeviceInfo grassDeviceInfo) {
        return baseMapper.selectGrassDeviceInfoList(grassDeviceInfo);
    }

    /**
     * 根据deviceId 查询数量
     *
     * @param deviceId
     * @return
     */
    @Override
    public Integer selectCountByDeviceName(String deviceName) {
        GrassDeviceInfo grassDeviceInfo = new GrassDeviceInfo();
        grassDeviceInfo.setDeviceName(deviceName);
        grassDeviceInfo.setTenantId(SecurityUtils.getTenantId());
        return baseMapper.selectCountByDeviceName(grassDeviceInfo);
    }

    /**
     * 根据deviceId 查询数量
     *
     * @param linkId
     * @return
     */
    @Override
    public Integer selectCountByLinkId(String linkId) {
        return baseMapper.selectCountByLinkId(linkId);
    }

    /**
     * 设备下拉框
     *
     * @return
     */
    @Override
    public List<GrassDeviceInfoVO> dropDown() {
        List<GrassDeviceInfo> deviceInfoList = baseMapper.selectGrassDeviceInfoList(null);
        return BeanUtil.copyToList(deviceInfoList, GrassDeviceInfoVO.class);
    }


    /**
     * 根据机器码 连接信息查询设备
     *
     * @param machineCode
     * @return
     */
    @Override
    public GrassDeviceInfo selectGrassDeviceInfoByMachineCode(String machineCode) {
        return baseMapper.selectGrassDeviceInfoByMachineCode(machineCode);
    }

    @Override
    public Boolean deleteByDeviceId(String deviceId) {
        return baseMapper.deleteByDeviceId(deviceId);
    }

    @Override
    public Boolean insertDeviceInfo(GrassDeviceInfo grassDeviceInfo) {
        return baseMapper.insertDeviceInfo(grassDeviceInfo);
    }

    @Override
    public Boolean updateDeviceInfo(GrassDeviceInfo grassDeviceInfo) {
        return baseMapper.updateDeviceInfo(grassDeviceInfo);
    }

    @Override
    public List<GrassDeviceInfo> selectGrassDeviceInfoListByTenantId(String tenantId) {
        return baseMapper.selectGrassDeviceInfoListByTenantId(tenantId);
    }

    @Override
    public Boolean deleteByTenantId(String tenantId) {
        return baseMapper.deleteByTenantId(tenantId);
    }

    @Override
    public NumberCountVO deviceStatisticalByTenantId() {
        NumberCountVO numberCountVO = baseMapper.deviceStatistical();
        return numberCountVO;
    }

}
