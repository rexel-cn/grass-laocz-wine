package com.rexel.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.rexel.common.config.PulseUrlConfig;
import com.rexel.common.enums.PulseLinkStatusEnum;
import com.rexel.common.exception.ServiceException;
import com.rexel.common.utils.DateUtils;
import com.rexel.common.utils.SecurityUtils;
import com.rexel.common.utils.pulse.PulseHttpRequestUtil;
import com.rexel.common.utils.pulse.PulseHttpResponseUtil;
import com.rexel.system.domain.GrassDeviceInfo;
import com.rexel.system.domain.GrassPointInfo;
import com.rexel.system.domain.GrassPointTagPoint;
import com.rexel.system.domain.PulseLinkInfo;
import com.rexel.system.domain.dto.PulseDeviceDTO;
import com.rexel.system.domain.dto.PulseLinkInfoDTO;
import com.rexel.system.domain.vo.EquipmentOverviewVO;
import com.rexel.system.domain.vo.EquipmentVO;
import com.rexel.system.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * ClassName IEquipmentImpl
 * Description
 * Author 孟开通
 * Date 2022/7/21 14:52
 **/
@Service
public class IEquipmentImpl implements IEquipmentService {
    /**
     * 不要超过7天
     */
    private static final int MAX_TIME = 7;
    @Autowired
    private IGrassDeviceInfoService iGrassDeviceInfoService;
    @Autowired
    private IPulseLinkInfoService iPulseLinkInfoService;
    @Autowired
    private IPulseLinkStatusService iPulseLinkStatusService;
    @Autowired
    private IGrassRulesInfoService iGrassRuleService;
    @Autowired
    private PulseUrlConfig pulseUrlConfig;

    @Autowired
    private IGrassPointService iGrassPointService;

    @Autowired
    private IGrassPointTagToPointService iGrassPointTagToPointService;

    @Autowired
    private IGrassAssetPointService iGrassAssetPointService;


    @Override
    public List<EquipmentVO> getList(PulseLinkInfoDTO pulseLinkInfoDTO) {
        return iPulseLinkInfoService.selectEquipmentVO(pulseLinkInfoDTO);
    }

    @Override
    public Long getListCount(PulseLinkInfoDTO pulseLinkInfoDTO) {
        return iPulseLinkInfoService.selectEquipmentVOCount(pulseLinkInfoDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean creat(PulseDeviceDTO pulseDeviceDTO) {
        check(pulseDeviceDTO.getDeviceName());
        switch (pulseDeviceDTO.getCreateType()) {
            case "1":
                //创建物联设备
                create(pulseDeviceDTO);
                break;
            case "2":
                //更换设备//修改设备link_id
                replace(pulseDeviceDTO);
                break;
            default:
                throw new ServiceException("未知异常,请刷新重试");
        }
        return true;
    }

    /**
     * 更换设备
     */
    private void replace(PulseDeviceDTO pulseDeviceDTO) {
        GrassDeviceInfo grassDeviceInfo = new GrassDeviceInfo();
        grassDeviceInfo.setDeviceId(pulseDeviceDTO.getDeviceId());
        grassDeviceInfo.setLinkId(pulseDeviceDTO.getLinkId());
        iGrassDeviceInfoService.updateDeviceInfo(grassDeviceInfo);
    }

    /**
     * 创建设备
     *
     * @param pulseDeviceDTO
     */
    private void create(PulseDeviceDTO pulseDeviceDTO) {
        checkCreate(pulseDeviceDTO);
        String deviceId = getDeviceId(pulseDeviceDTO.getLinkId());
        GrassDeviceInfo grassDeviceInfo = BeanUtil.copyProperties(pulseDeviceDTO, GrassDeviceInfo.class);
        grassDeviceInfo.setTenantId(SecurityUtils.getTenantId());
        grassDeviceInfo.setDeviceId(deviceId);
        grassDeviceInfo.setBucketId(Objects.requireNonNull(SecurityUtils.getLoginUser()).getBucketId());
        iGrassDeviceInfoService.insertDeviceInfo(grassDeviceInfo);
        PulseLinkInfo pulseLinkInfo = iPulseLinkInfoService.lambdaQuery().eq(PulseLinkInfo::getLinkId, pulseDeviceDTO.getLinkId()).one();
        JSONObject dto = new JSONObject();
        dto.put("deviceId", deviceId);
        dto.put("machineCode", pulseLinkInfo.getMachineCode());
        Boolean aBoolean = PulseHttpResponseUtil
                .responseToBoolean(PulseHttpRequestUtil
                        .sendPostJson(pulseUrlConfig.getDownSetDevice(), JSON.toJSONString(dto)));
        if (!aBoolean) {
            throw new ServiceException("设置物联设备ID错误");
        }
    }

    private void checkCreate(PulseDeviceDTO pulseDeviceDTO) {
        Integer linkIdCount = iGrassDeviceInfoService.selectCountByLinkId(String.valueOf(pulseDeviceDTO.getLinkId()));
        if (ObjectUtil.isNotNull(linkIdCount) && linkIdCount > 0) {
            throw new ServiceException("创建失败，该设备已被创建，请刷新后重试");
        }
    }


    @Override
    @Transactional
    public Boolean update(PulseDeviceDTO pulseDeviceDTO) {
        GrassDeviceInfo grassDeviceInfo = BeanUtil.copyProperties(pulseDeviceDTO, GrassDeviceInfo.class);
        //检查当前租户下设备名不重复
        check(grassDeviceInfo.getDeviceName());

        GrassDeviceInfo byId = iGrassDeviceInfoService.getById(grassDeviceInfo.getDeviceId());


        //如果修改了设备名，需要修改 测点表的设备名
        if (!byId.getDeviceName().equals(grassDeviceInfo.getDeviceName())) {
            iGrassPointService.lambdaUpdate().eq(GrassPointInfo::getDeviceId, grassDeviceInfo.getDeviceId())
                    .set(GrassPointInfo::getDeviceName, grassDeviceInfo.getDeviceName()).update();
        }

        return iGrassDeviceInfoService.updateDeviceInfo(grassDeviceInfo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean delete(PulseDeviceDTO pulseDeviceDTO) {
        String deviceId = pulseDeviceDTO.getDeviceId();
        checkDelete(deviceId);
        // PulseLinkInfo pulseLinkInfo = iPulseLinkInfoService.selectPulseLinkInfoByDeviceId(deviceId);
        //删除设备表
        iGrassDeviceInfoService.deleteByDeviceId(deviceId);
        //删除测点表
        iGrassPointService.lambdaUpdate().eq(GrassPointInfo::getDeviceId, deviceId).remove();
        //删除测点标签表
        iGrassPointTagToPointService.lambdaUpdate().eq(GrassPointTagPoint::getDeviceId, deviceId).remove();
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean linkDelete(String linkId) {
        checkLinkDelete(linkId);
        iPulseLinkStatusService.deleteByLinkId(linkId);
        iPulseLinkInfoService.lambdaUpdate()
                .eq(PulseLinkInfo::getLinkId, linkId)
                .remove();
        return true;
    }

    @Override
    public EquipmentOverviewVO equipmentOverview() {
        EquipmentOverviewVO equipmentOverviewVO = new EquipmentOverviewVO();
        equipmentOverviewVO.setBindCount(getListCount(new PulseLinkInfoDTO(true, SecurityUtils.getTenantId())).intValue());
        equipmentOverviewVO.setNoBindCount(getListCount(new PulseLinkInfoDTO(false, SecurityUtils.getTenantId())).intValue());
        equipmentOverviewVO.setBindPointCount(iGrassPointService.selectCount());
        return equipmentOverviewVO;
    }

    /**
     *
     */
    @Override
    public void deleteObsoleteDevices() {
        PulseLinkInfoDTO pulseLinkInfoDTO = new PulseLinkInfoDTO();
        pulseLinkInfoDTO.setMaster(false);
        List<EquipmentVO> list = getList(pulseLinkInfoDTO);
        //筛选出linkStatusCode 为0并且 lastTime距离现在超过 7天的设备
        list.stream().filter(equipmentVO -> PulseLinkStatusEnum.OFFLINE.getCode().equals(equipmentVO.getLinkStatusCode())
                && DateUtil.between(DateUtil.date(), equipmentVO.getLastTime(), DateUnit.DAY) >= MAX_TIME).forEach(equipmentVO -> {
            //删除link
            linkDelete(equipmentVO.getLinkId());
        });
    }

    /**
     * 校验断开连接
     *
     * @param linkId 连接id
     */
    private void checkLinkDelete(String linkId) {
        Integer linkIdCount = iGrassDeviceInfoService.selectCountByLinkId(linkId);
        if (ObjectUtil.isNotNull(linkIdCount) && linkIdCount > 0) {
            throw new ServiceException("断开失败，已创建设备");
        }
    }

    /**
     * 校验删除设备
     *
     * @param deviceId 设备ID
     */
    private void checkDelete(String deviceId) {
        //检查报警规则
        Integer count = iGrassRuleService.selectCountByDeviceId(deviceId);
        if (ObjectUtil.isNotNull(count) && count > 0) {
            throw new ServiceException("删除失败，请先删除报警规则");
        }
        //检查资产测点关联表
        Integer count1 = iGrassAssetPointService.selectAssetPointCountByDeviceId(deviceId);
        if (ObjectUtil.isNotNull(count1) && count1 > 0) {
            throw new ServiceException("删除失败，请先删除设备");
        }

    }

    /**
     * 校验创建设备
     *
     * @param deviceName 设备名称
     */
    private void check(String deviceName) {
        Integer deviceIdCount = iGrassDeviceInfoService.selectCountByDeviceName(deviceName);
        if (ObjectUtil.isNotNull(deviceIdCount) && deviceIdCount > 0) {
            throw new ServiceException("设备名称存在");
        }
    }

    private String getDeviceId(Long linkId) {
        StringBuilder sb = new StringBuilder();
        sb.append("d_");
        sb.append(DateUtils.dateTimeNow(DateUtils.YYYYMMDDHHMMSSSSS));
        sb.append("_");
        sb.append(linkId);
        sb.append("_");
        sb.append(UUID.randomUUID().toString().substring(0, 6));
        return sb.toString().toLowerCase();
    }
}
