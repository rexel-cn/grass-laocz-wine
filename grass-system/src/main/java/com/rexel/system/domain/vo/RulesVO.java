package com.rexel.system.domain.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rexel.common.annotation.Excel;
import com.rexel.system.domain.GrassRulesInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName RulesVO
 * @Description
 * @Author 孟开通
 * @Date 2022/8/17 09:57
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RulesVO extends GrassRulesInfo implements Serializable {
    /**
     * 通知模板名称集合
     */
    @JsonIgnore
    private List<String> noticeTemplateNameList;
    /**
     * 通知模板ID集合
     */
    private List<Long> noticeTemplateIds;
    /**
     * 通知模板名称
     */
    @Excel(name = "通知模板")
    private String noticeTemplateName;

    /**
     * 资产设备名
     */
    private String assetName;
    /**
     * 归属分类
     */
    private String assetTypeName;
    /**
     * 设备名
     */
    private String deviceName;
    /**
     * 测点名
     */
    private String pointName;
    /**
     * 报警级别
     */
    private String rulesLevelName;
    /**
     * 沉默周期
     */
    private String silentCycleName;

}