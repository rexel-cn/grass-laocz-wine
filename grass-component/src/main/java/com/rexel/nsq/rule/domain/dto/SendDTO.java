package com.rexel.nsq.rule.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * ClassName SendDTO
 * Description 构造发送内容
 * Author 孟开通
 * Date 2022/5/30 14:22
 **/
@Data
public class SendDTO extends Send implements Serializable {
    /**
     * 报警级别(参考字典police_level)
     */
    private String rulesLevel;
    /**
     * 测点ID
     */
    private String pointName;
    /**
     * 通知内容
     */
    private String noticeContent;
    /**
     * 设备名称
     */
    private String equipmentName;
    /**
     * 实时值
     */
    private String rtVal;
    /**
     * 报警时间
     */
    private Date currTime;
    /**
     * 沉默周期
     */
    private String silentCycle;
}
