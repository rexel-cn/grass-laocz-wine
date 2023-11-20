package com.rexel.system.domain.vo;

import lombok.Data;

/**
 * 预聚合信息对象
 *
 * @author grass-service
 * @date 2023-04-27
 */
@Data
public class GrassReduceDeleteVO {
    private static final long serialVersionUID = 1L;

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
}
