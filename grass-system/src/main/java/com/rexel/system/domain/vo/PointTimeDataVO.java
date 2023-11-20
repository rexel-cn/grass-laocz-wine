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
public class PointTimeDataVO implements Serializable {
    /**
     * grass_id
     */
    private String assetId;
    /**
     * 测点唯一标识
     */
    private String pointPrimaryKey;
    /**
     * 测点id
     */
    @Excel(name = "测点ID")
    private String pointId;

    private String deviceId;
    /**
     * 测点名
     */
    @Excel(name = "测点名称")
    private String pointName;
    /**
     * 测点类型
     */
    @Excel(name = "测点类型")
    private String pointType;
    /**
     * 测点单位
     */
    @Excel(name = "测点单位")
    private String pointUnit;
    /**
     * 测点值
     */
    private String pointValue;
    /**
     * 最后一次上报时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastTime;
    /**
     * 数据质量
     */
    private String qty;
    /**
     * 是否置顶，mysql 1=true，0=false
     * 1=不置顶，0=置顶
     * true=不置顶，false=置顶
     */
    private Boolean starShow;
}
