package com.rexel.system.domain.dto;

import com.rexel.common.annotation.Excel;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName RulesExcelDTO
 * @Description
 * @Author 孟开通
 * @Date 2022/8/17 15:14
 **/
@Data
public class RulesExcelDTO implements Serializable {

    @Excel(name = "规则名称")
    private String rulesName;

    // 资产设备名
    @Excel(name = "资产设备名")
    private String assetName;

    // 归属分类
    @Excel(name = "归属分类")
    private String assetTypeName;

    /**
     * 设备id
     */
    @Excel(name = "设备ID")
    private String deviceId;
    /**
     * 测点id
     */
    @Excel(name = "测点ID")
    private String pointId;
    /**
     * 判断条件
     */
    @Excel(name = "判断条件")
    private String pointJudge;
    /**
     * 报警值
     */
    @Excel(name = "测点值")
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
    private String isEnable;

    @Excel(name = "通知模板")
    private String noticeTemplateName;
}
