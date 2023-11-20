package com.rexel.system.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rexel.common.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName EquipmentVO
 * @Description 物联设备VO
 * @Author 孟开通
 * @Date 2022/7/21 15:01
 **/
@Data
public class EquipmentVO implements Serializable {
    private String linkId;
    /**
     * 机器码
     */
    @Excel(name = "网关机器码")
    private String machineCode;
    /**
     * 设备型号
     */
    @Excel(name = "设备类型")
    private String linkDeviceType;
    @Excel(name = "设备名称")
    private String linkDeviceName;
    /**
     * 首次连接时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "首次连接时间", dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date linkTime;
    /**
     * 连接状态（1：在线、0：离线）
     */
    @Excel(name = "设备状态")
    private String linkStatus;

    private String linkStatusCode;
    /**
     * 最后一次上线时间
     */
    @Excel(name = "最后一次连接时间", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastTime;
    /**
     * 设备Id (物联设备编号)
     */
    @Excel(name = "物联设备编号")
    private String deviceId;
    /**
     * 设备名称
     */
    @Excel(name = "物联设备名称")
    private String deviceName;
}
