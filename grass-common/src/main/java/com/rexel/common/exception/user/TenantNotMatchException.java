package com.rexel.common.exception.user;

/**
 * @ClassName TenantNotMatchException
 * @Description
 * @Author 孟开通
 * @Date 2023/1/13 14:04
 **/
public class TenantNotMatchException extends TenantException {
    private static final long serialVersionUID = 1L;

    public TenantNotMatchException() {
        super("tenant.not.match", null);
    }
}
