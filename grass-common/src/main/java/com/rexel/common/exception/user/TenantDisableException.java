package com.rexel.common.exception.user;

/**
 * @ClassName TenantDisableException
 * @Description
 * @Author 孟开通
 * @Date 2023/1/13 14:07
 **/
public class TenantDisableException extends TenantException {
    private static final long serialVersionUID = 1L;

    public TenantDisableException() {
        super("tenant.disable", null);
    }
}
