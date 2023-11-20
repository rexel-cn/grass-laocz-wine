package com.rexel.system.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rexel.common.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * ClassName PointQueryVo
 * Description 查询测点
 * Author 孟开通
 * Date 2022/7/7 14:44
 **/
@Data
public class PointQueryVO implements Serializable {
    /**
     * grass_id
     */
    private Long id;

    private String tenantId;
    /**
     * 设备id
     */
    @Excel(name = "设备ID")
    private String deviceId;
    /**
     * 设备名称
     */
    @Excel(name = "设备名称")
    private String deviceName;
    /**
     * 物联设备类型
     */
    @Excel(name = "物联设备类型")
    private String linkDeviceType;
    /**
     * 测点id
     */
    @Excel(name = "测点ID")
    private String pointId;
    /**
     * 测点名
     */
    @Excel(name = "测点名称")
    private String pointName;
    /**
     * 测点类型
     */
    private String pointType;
    /**
     * 测点类型
     */
    @Excel(name = "测点类型")
    private String pointTypeDesc;
    /**
     * 测点单位
     */
    @Excel(name = "测点单位")
    private String pointUnitDesc;
    /**
     * 测点单位
     */
    private String pointUnit;
    /**
     * 测点值
     */
    private String pointValue;
    /**
     * 	最后一次上报时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastTime;
    /**
     * 输入最小值
     */
    @Excel(name = "输入最小值")
    private Double inMin;
    /**
     * 输入最大值
     */
    @Excel(name = "输入最大值")
    private Double inMax;
    /**
     * 输出最小值
     */
    @Excel(name = "输出最小值")
    private Double outMin;
    /**
     * 输出最大值
     */
    @Excel(name = "输出最大值")
    private Double outMax;
    /**
     * 数据质量: 中文说明
     *  0：正常
     *  1或“”：离线
     */
    private String qty;

    /**
     * 关联设备数量
     */
    private Integer associateDeviceCount;
    /**
     * 测点标签数量
     */
    private Integer pointTagCount;
    private String remark;


    private String displayName;
}
