package com.rexel.common.exception.user;

import com.rexel.common.exception.base.BaseException;

/**
 * @ClassName TenantException
 * @Description
 * @Author 孟开通
 * @Date 2023/1/13 14:02
 **/

public class TenantException extends BaseException {
    private static final long serialVersionUID = 1L;

    public TenantException(String code, Object[] args) {
        super("tenant", code, args, null);
    }
}
