package com.rexel.earlywarning.vo;

import lombok.Data;
import lombok.ToString;

/**
 * 预警记录聚合结果
 *
 * @author chunhui.qu
 * @date 2022-02-09
 */
@Data
@ToString
public class GrassEarlyWarningAlarmHisGroupResult {
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
