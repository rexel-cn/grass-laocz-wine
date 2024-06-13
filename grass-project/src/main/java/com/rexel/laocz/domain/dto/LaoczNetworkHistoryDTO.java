package com.rexel.laocz.domain.dto;

import lombok.Data;

/**
 * @ClassName LaoczNetworkHistoryDTO
 * @Description 老村长网络设备报警历史记录DTO
 * @Author 孟开通
 * @Date 2024/6/13 14:35
 **/
@Data
public class LaoczNetworkHistoryDTO {
    /**
     * 开始时间
     */
    private String startTime;
    /**
     * 结束时间
     */
    private String endTime;
    /**
     * 网络设备名
     */
    private String networkName;
    /**
     * ip
     */
    private String ipAddr;
}
