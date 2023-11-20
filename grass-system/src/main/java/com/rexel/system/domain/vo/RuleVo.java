package com.rexel.system.domain.vo;

import cn.hutool.core.collection.CollectionUtil;
import com.rexel.common.annotation.Excel;
import com.rexel.system.domain.GrassRuleNotice;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ClassName RuleVo
 * Description
 * Author 孟开通
 * Date 2022/8/5 11:18
 **/
@Data
public class RuleVo implements Serializable {
    /**
     * 通知模板
     */
    List<GrassRuleNotice> ruleNoticeList;
    /**
     * 主键id
     */
    private String id;
    /**
     * 规则名称
     */
    @Excel(name = "规则名称")
    private String rulesName;
    /**
     * 设备id
     */
    @Excel(name = "设备id")
    private String deviceId;
    /**
     * 测点id
     */
    @Excel(name = "测点id")
    private String pointId;
    /**
     * 测点名称
     */
    private String pointName;
    /**
     * 判断条件
     */
    @Excel(name = "判断条件")
    private String pointJudge;
    /**
     * 报警值
     */
    @Excel(name = "报警值")
    private Double pointValue;
    /**
     * 报警级别
     */
    @Excel(name = "报警级别")
    private String rulesLevel;
    /**
     * 沉默周期
     */
    @Excel(name = "沉默周期")
    private String silentCycle;
    /**
     * 生效开始时间
     */
    @Excel(name = "生效开始时间")
    private String startTime;
    /**
     * 生效结束时间
     */
    @Excel(name = "生效结束时间")
    private String endTime;
    /**
     * 报警分类
     */
    @Excel(name = "报警分类")
    private String rulesType;
    /**
     * 是否生效
     */
    @Excel(name = "是否生效")
    private Boolean isEnable;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 修改时间
     */
    private String updateTime;
    /**
     * 沉默周期名称
     */
    private String silentCycleName;
    /**
     * 报警分类名称
     */
    private String rulesLevelName;
    /**
     * 报警分类名称
     */
    private String rulesTypeName;
    /**
     * 设备名称
     */
    private String deviceName;
    /**
     * 拼接通知模板名称
     */
    @Excel(name = "通知模板")
    private String noticeTemplateName;

    private List<Long> noticeTemplateIds;

    public List<Long> getNoticeTemplateIds() {
        if (CollectionUtil.isNotEmpty(ruleNoticeList)) {
            return ruleNoticeList.stream().map(GrassRuleNotice::getNoticeTemplateId).collect(Collectors.toList());
        }
        return noticeTemplateIds;
    }

    public String getNoticeTemplateName() {
        if (CollectionUtil.isNotEmpty(ruleNoticeList)) {
            List<String> collect = ruleNoticeList.stream().map(GrassRuleNotice::getNoticeTemplateName).collect(Collectors.toList());
            return String.join(",", collect);
        }
        return noticeTemplateName;
    }
}
