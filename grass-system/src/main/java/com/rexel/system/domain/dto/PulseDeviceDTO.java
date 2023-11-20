package com.rexel.system.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * ClassName PulseDeviceDTO
 * Description
 * Author 孟开通
 * Date 2022/7/19 16:39
 **/
@Data
public class PulseDeviceDTO implements Serializable {
    /**
     * 连接ID
     */
    private Long linkId;
    /**
     * 新增方式 1创建设备，2修改设备
     */
    private String createType;
    /**
     * 设备ID
     */
    private String deviceId;
    /**
     * 设备名称
     */
    private String deviceName;


}
