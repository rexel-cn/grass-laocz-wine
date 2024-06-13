package com.rexel.laocz.domain.vo;

import lombok.Data;

/**
 * @ClassName NetWorkVO
 * @Description 网络设备VO
 * @Author 孟开通
 * @Date 2024/6/11 15:27
 **/
@Data
public class NetWorkVO {
    /**
     * 主键id
     */
    private Long id;
    /**
     * 网络设备名
     */
    private String networkName;
    /**
     * ip地址
     */
    private String ipAddr;
    /**
     * 连接状态
     */
    private String connectStatus;
}
