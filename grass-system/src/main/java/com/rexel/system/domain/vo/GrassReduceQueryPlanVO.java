package com.rexel.system.domain.vo;

import lombok.Data;

/**
 * 预聚合信息对象
 *
 * @author grass-service
 * @date 2023-04-27
 */
@Data
public class GrassReduceQueryPlanVO {
    private static final long serialVersionUID = 1L;

    /**
     * 设备ID
     */
    private String deviceId;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 聚合颗粒度
     */
    private String interval;

    /**
     * 结果状态
     */
    private String status;

    /**
     * 限制条数
     */
    private Integer limit;
}
