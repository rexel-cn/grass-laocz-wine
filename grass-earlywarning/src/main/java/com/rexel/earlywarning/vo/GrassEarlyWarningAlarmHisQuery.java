package com.rexel.earlywarning.vo;

import lombok.Data;
import lombok.ToString;

/**
 * 预警记录查询条件对象
 *
 * @author chunhui.qu
 * @date 2020-06-11
 */
@Data
@ToString
public class GrassEarlyWarningAlarmHisQuery {
    private static final long serialVersionUID = 1L;

    /**
     * 租户ID
     */
    private String tenantId;
    /**
     * 开始时间
     */
    private String timeFrom;
    /**
     * 开始时间
     */
    private String timeTo;
    /**
     * 预警规则名称
     */
    private String rulesName;
    /**
     * 报警等级
     */
    private String rulesLevel;
    /**
     * 时间单位
     */
    private String timeUnit;
    /**
     * 时间格式
     */
    private String timeFormat;
    /**
     * 时间格式转换
     *
     * @return 结果
     */
    public String getTimeFormat() {
        // 分钟
        String minute = "m";
        // 小时
        String hour = "h";
        // 天
        String day = "d";
        if (minute.equals(timeUnit)) {
            return "%Y-%m-%d %H:%i";
        } else if (hour.equals(timeUnit)) {
            return "%Y-%m-%d %H";
        } else if (day.equals(timeUnit)) {
            return "%Y-%m-%d";
        }
        return "%Y-%m-%d";
    }
}
