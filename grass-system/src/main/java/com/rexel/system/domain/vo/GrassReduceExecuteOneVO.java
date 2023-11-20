package com.rexel.system.domain.vo;

import lombok.Data;

/**
 * 预聚合信息对象
 *
 * @author grass-service
 * @date 2023-04-27
 */
@Data
public class GrassReduceExecuteOneVO {
    private static final long serialVersionUID = 1L;

    /**
     * 设备ID
     */
    private String deviceId;

    /**
     * 执行时间
     */
    private String dataTime;

    /**
     * 聚合颗粒度
     */
    private String interval;
}
