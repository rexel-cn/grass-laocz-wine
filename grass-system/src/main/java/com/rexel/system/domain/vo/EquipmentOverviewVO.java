package com.rexel.system.domain.vo;

import lombok.Data;

/**
 * ClassName EquipmentOverviewVO
 * Description 物联设备统计概览
 * Author 孟开通
 * Date 2022/10/10 15:05
 **/
@Data
public class EquipmentOverviewVO {
    /**
     * 已接入物联设备数
     */
    private Integer bindCount;
    /**
     * 未接入物联设备数
     */
    private Integer noBindCount;
    /**
     *已接入测点数
     */
    private Integer bindPointCount;
}
