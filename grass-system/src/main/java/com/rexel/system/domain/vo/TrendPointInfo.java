package com.rexel.system.domain.vo;

import lombok.Data;
import lombok.ToString;

/**
 * TrendPointInfo
 *
 * @author ids
 * @date 2022-11-01
 */
@Data
@ToString
public class TrendPointInfo {
    /*
     * 请求参数
     */
    /** 租户ID */
    private String tenantId;

    /** 资产ID */
    private String assetId;

    /** 资产名称 */
    private String assetName;

    /** 物联设备ID */
    private String deviceId;

    /** 测点ID */
    private String pointId;

    /** 测点名称 */
    private String pointName;

    /*
     * 后台设置
     */
    /** 租户名称 */
    private String tenantName;

    /** 租户英文名称 */
    private String tenantEngName;

    /** 测点名称重复 */
    private boolean duplicatePointName;

    /** 租户内测点重复 */
    private boolean duplicateInTenant;

    /** 租户间测点重复 */
    private boolean duplicateOverTenant;
}
