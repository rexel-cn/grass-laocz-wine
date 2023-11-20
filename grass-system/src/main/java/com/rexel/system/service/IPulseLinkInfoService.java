package com.rexel.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rexel.system.domain.PulseLinkInfo;
import com.rexel.system.domain.dto.PulseLinkInfoDTO;
import com.rexel.system.domain.vo.EquipmentVO;

import java.util.List;

/**
 * 连接信息Service接口
 *
 * @author grass-service
 * @date 2022-08-16
 */
public interface IPulseLinkInfoService extends IService<PulseLinkInfo> {

    /**
     * 查询连接信息列表
     *
     * @param pulseLinkInfo 连接信息
     * @return 连接信息集合
     */
    List<PulseLinkInfo> selectPulseLinkInfoList(PulseLinkInfo pulseLinkInfo);

    /**
     * 查询物联设备
     *
     * @return
     */
    List<EquipmentVO> selectEquipmentVO(PulseLinkInfoDTO pulseLinkInfoDTO);

    /**
     * 查询物联设备数量
     *
     * @return
     */
    Long selectEquipmentVOCount(PulseLinkInfoDTO pulseLinkInfoDTO);

    /**
     * 根据设备id查询连接信息
     *
     * @param deviceId 设备id
     * @return
     */
    PulseLinkInfo selectPulseLinkInfoByDeviceId(String deviceId);

    /**
     * 查询物联设备型号
     *
     * @return
     */
    List<String> selectLinkDeviceType();

}
