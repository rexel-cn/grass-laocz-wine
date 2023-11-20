package com.rexel.earlywarning.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * 预警规则报警历史对象
 * 
 * @author admin
 * @date 2022-01-14
 */
@Data
@ToString
public class GrassEarlyWarningAlarmData {
    private static final long serialVersionUID = 1L;

    /**
     * JobID
     */
    private String jobId;
    /**
     * 预警时间
     */
    private Date alarmTime;
}
