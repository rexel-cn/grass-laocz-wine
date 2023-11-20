package com.rexel.system.domain.dto.tenant;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName TenantPageReqDTO
 * @Description
 * @Author 孟开通
 * @Date 2022/9/28 16:18
 **/
@Data
public class TenantPageReqDTO implements Serializable {

    /**
     * 租户名
     */
    private String tenantName;

    /**
     * 租户id
     */
    private String tenantId;
}
