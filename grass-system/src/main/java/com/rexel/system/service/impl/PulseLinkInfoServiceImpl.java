package com.rexel.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.system.domain.PulseLinkInfo;
import com.rexel.system.domain.dto.PulseLinkInfoDTO;
import com.rexel.system.domain.vo.EquipmentVO;
import com.rexel.system.mapper.PulseLinkInfoMapper;
import com.rexel.system.service.IPulseLinkInfoService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 连接信息Service业务层处理
 *
 * @author grass-service
 * @date 2022-08-16
 */
@Service
public class PulseLinkInfoServiceImpl extends ServiceImpl<PulseLinkInfoMapper, PulseLinkInfo> implements IPulseLinkInfoService {


    /**
     * 查询连接信息列表
     *
     * @param pulseLinkInfo 连接信息
     * @return 连接信息
     */
    @Override
    public List<PulseLinkInfo> selectPulseLinkInfoList(PulseLinkInfo pulseLinkInfo) {
        return baseMapper.selectPulseLinkInfoList(pulseLinkInfo);
    }

    /**
     * 查询物联设备
     *
     * @return
     */
    @Override
    public List<EquipmentVO> selectEquipmentVO(PulseLinkInfoDTO pulseLinkInfoDTO) {
        return baseMapper.selectEquipmentVO(pulseLinkInfoDTO);
    }

    @Override
    public Long selectEquipmentVOCount(PulseLinkInfoDTO pulseLinkInfoDTO) {
        return baseMapper.selectEquipmentVOCount(pulseLinkInfoDTO);
    }

    @Override
    public PulseLinkInfo selectPulseLinkInfoByDeviceId(String deviceId) {
        return baseMapper.selectPulseLinkInfoByDeviceId(deviceId);
    }

    @Override
    public List<String> selectLinkDeviceType() {
        return baseMapper.selectLinkDeviceType();
    }


}
