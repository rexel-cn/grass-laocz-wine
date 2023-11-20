package com.rexel.system.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName GrassDeviceInfoVO
 * @Description
 * @Author 孟开通
 * @Date 2022/8/17 17:48
 **/
@Data
public class GrassDeviceInfoVO implements Serializable {
    /**
     * 设备ID
     */
    private String deviceId;
    /**
     * 设备名
     */
    private String deviceName;
}
