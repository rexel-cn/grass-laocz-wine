package com.rexel.nsq.rule.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * ClassName AlarmDTO
 * Description
 * Author 孟开通
 * Date 2022/5/27 10:52
 **/
@Data
public class AlarmDTO implements Serializable {

    /**
     * 报警历史id
     */
    private String id;
    /**
     * 租户id
     */
    private String tenantId;
    /**
     * 规则id
     */
    private String rulesId;
    /**
     * 规则名称
     */
    private String rulesName;
    /**
     * 规则唯一标识
     */
    private String rulesUnique;
    /**
     * 设备id
     */
    private String deviceId;
    /**
     * 资产设备id
     */
    private String assetId;
    /**
     * 测点id
     */
    private String pointId;
    /**
     * 测点判断符号
     */
    private String pointJudge;
    /**
     * 判断值
     */
    private String pointValue;
    /**
     * 报警时间
     */
    private Date alarmTime;
    /**
     * 报警值
     */
    private String alarmValue;
    /**
     * 报警级别
     */
    private String rulesLevel;
    /**
     * 报警类型
     */
    private String rulesType;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date updateTime;
    /**
     * 沉默周期
     */
    private String silentCycle;
}
