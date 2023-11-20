package com.rexel.system.domain.vo;

import lombok.Data;

/**
 * @ClassName TimeSeriesDataVo
 * @Description
 * @Author 孟开通
 * @Date 2022/10/24 11:29
 **/
@Data
public class TimeSeriesDataVO {
    /** 时间 */
    private String time;
    /** 物联设备ID */
    private String deviceId;
    /** 测点ID */
    private String pointId;
    /** 测点值 */
    private Double value;
}
