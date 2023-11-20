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
public class GrassEarlyWarningAlarmHisGroupByRulesName {
    private static final long serialVersionUID = 1L;

    /**
     * 规则名称
     */
    private String rulesName;
    /**
     * 合计数量
     */
    private Long alarmCount;
}
