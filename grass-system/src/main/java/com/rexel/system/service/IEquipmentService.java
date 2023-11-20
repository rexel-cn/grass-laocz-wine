package com.rexel.system.service;

import com.rexel.system.domain.dto.PulseDeviceDTO;
import com.rexel.system.domain.dto.PulseLinkInfoDTO;
import com.rexel.system.domain.vo.EquipmentOverviewVO;
import com.rexel.system.domain.vo.EquipmentVO;

import java.util.List;

/**
 * ClassName IEquipment 物联设备
 * Description
 *
 * @Author 孟开通
 * Date 2022/7/21 14:50
 **/
public interface IEquipmentService {
    /**
     * 查询
     *
     * @param pulseLinkInfoDTO
     * @return
     */
    List<EquipmentVO> getList(PulseLinkInfoDTO pulseLinkInfoDTO);

    /**
     * 查询 数量
     *
     * @param pulseLinkInfoDTO
     * @return
     */
    Long getListCount(PulseLinkInfoDTO pulseLinkInfoDTO);

    /**
     * 创建
     *
     * @param pulseDeviceDTO
     * @return
     */
    Boolean creat(PulseDeviceDTO pulseDeviceDTO);

    /**
     * 修改
     *
     * @param pulseDeviceDTO
     * @return
     */
    Boolean update(PulseDeviceDTO pulseDeviceDTO);

    /**
     * 删除
     *
     * @param pulseDeviceDTO
     * @return
     */
    Boolean delete(PulseDeviceDTO pulseDeviceDTO);

    /**
     * 断开
     *
     * @param pulseDeviceDTO
     * @return
     */
    Boolean linkDelete(String linkId);

    /**
     * 物联设备统计概览
     *
     * @return
     */
    EquipmentOverviewVO equipmentOverview();

    /**
     * 物联设备清理
     */
    void deleteObsoleteDevices();
}
