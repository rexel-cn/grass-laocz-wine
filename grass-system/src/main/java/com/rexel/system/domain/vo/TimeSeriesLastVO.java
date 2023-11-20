package com.rexel.system.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName TimeSeriesLastVO
 * @Description
 * @Author 孟开通
 * @Date 2022/8/16 15:44
 */
@Data
public class TimeSeriesLastVO implements Serializable {
    /** 时间 */
    private Date time;
    /** 物联设备ID */
    private String deviceId;
    /** 测点ID */
    private String pointId;
    /** 测点值 */
    private String value;
    /** 数据质量 */
    private String qty;
}
