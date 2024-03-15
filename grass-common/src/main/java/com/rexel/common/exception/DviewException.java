package com.rexel.common.exception;

import org.slf4j.helpers.MessageFormatter;

/**
 * 自定义异常 dview 异常
 *
 * @author ids
 */
public class DviewException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private final String message;
    private Integer code;

    public DviewException(String message, Object... args) {
        this.message = MessageFormatter.arrayFormat(message, args).getMessage();
    }

    public DviewException(String message, Integer code) {
        this.message = message;
        this.code = code;
    }

    public DviewException(String message, Throwable e) {
        super(message, e);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public Integer getCode() {
        return code;
    }
}
