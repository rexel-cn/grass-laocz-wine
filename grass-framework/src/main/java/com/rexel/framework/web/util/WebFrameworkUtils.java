package com.rexel.framework.web.util;

import cn.hutool.core.util.StrUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * 专属于 web 包的工具类
 *
 * @author
 */
public class WebFrameworkUtils {
    public static final String HEADER_TENANT_ID = "tenant-id";

    /**
     * 获得租户编号，从 header 中
     *
     * @param request 请求
     * @return 租户编号
     */
    public static Long getTenantId(HttpServletRequest request) {
        String tenantId = request.getHeader(HEADER_TENANT_ID);
        return StrUtil.isNotEmpty(tenantId) ? Long.valueOf(tenantId) : null;
    }


}
