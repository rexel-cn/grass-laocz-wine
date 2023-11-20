package com.rexel.system.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName GrassAlarmHistoryVO
 * @Description 告警查询Vo
 * @Author 孟开通
 * @Date 2023/4/27 14:53
 **/
@Data
public class GrassAlarmHistoryVO implements Serializable {
    /**
     * 报警时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date alarmTime;
    /**
     * 规则名称
     */
    private String rulesName;
    /**
     * 归属分类
     */
    private String assetTypeName;
    /**
     * 设备名称
     */
    private String assetName;
    /**
     * 测点名称
     */
    private String pointName;
    /**
     * 测点名称
     */
    private String pointId;
    /**
     * 判断条件
     */
    private String pointJudge;
    /**
     * 报警值
     */
    private String alarmValue;
    /**
     * 报警阈值
     */
    private String pointValue;


}
