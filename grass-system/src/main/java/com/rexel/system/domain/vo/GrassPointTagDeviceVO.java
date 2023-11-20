package com.rexel.system.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 关联测点显示的具体信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GrassPointTagDeviceVO {

    private String pointId;

    private String pointName;

    private String deviceId;

    private String deviceName;

    private String linkDeviceType;
}
