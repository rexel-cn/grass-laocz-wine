package com.rexel.system.domain.vo;


import com.rexel.common.annotation.Excel;
import lombok.Data;

@Data
public class PointTagExportVO {
    @Excel(name = "测点标识")
    private String pointId;
    @Excel(name = "测点名字")
    private String pointName;
    @Excel(name = "测点类型")
    private String pointType;
    @Excel(name = "物联设备id")
    private String deviceId;
    @Excel(name = "物联设备名字")
    private String deviceName;
    @Excel(name = "标签键")
    private String tagKey;
    @Excel(name = "标签值")
    private String tagValue;
    @Excel(name = "标签类型", readConverterExp = "0=用户标签,1=系统标签,2=业务标签")
    private String tagType;
    @Excel(name = "备注")
    private String remark;
}
