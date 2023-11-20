package com.rexel.system.domain.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 用于所有告警页面表单检索条件
 *
 * @author chunhui.qu
 * @date 2020-06-11
 */
@Data
@ToString
public class GrassAlarmHistoryDTO implements Serializable {
    private static final long serialVersionUID = 1L;
     /**
     * 设备名称
     */
    private String deviceId;
    /**
     * 点位名称
     */
    private String pointId;

    // 开始时间
    private String timeFrom;

    // 结束时间
    private String timeTo;

    // 资产设备名
    private String assetName;

    // 归属分类
    private String assetTypeName;

    // 点位名称
    private String pointName;

    // 规则名称
    private String rulesName;

    // 时间格式
    private String timeFormat;

    // 时间单位转换
    private String timeUnit;

    // 时间格式转换
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
