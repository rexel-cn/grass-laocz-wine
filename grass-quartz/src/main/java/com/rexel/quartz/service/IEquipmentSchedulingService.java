package com.rexel.quartz.service;

/**
 * @ClassName IEquipmentSchedulingService
 * @Description
 * @Author 孟开通
 * @Date 2022/12/20 15:38
 **/
public interface IEquipmentSchedulingService {
    /**
     * 物联设备定时清理
     */
    void deleteObsoleteDevices();
}
