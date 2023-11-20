package com.rexel.system.domain.vo;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 告警历史表 iot_alarm_history
 *
 * @author chunhui.qu
 * @date 2020-06-11
 */
@Builder
@Data
@ToString
public class GrassAlarmGroupVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 合计数量
     */
    private Long alarmCount;

    /**
     * 时间区间
     */
    private String alarmTimeRange;
}
