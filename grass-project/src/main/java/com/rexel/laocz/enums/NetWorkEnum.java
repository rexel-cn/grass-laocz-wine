package com.rexel.laocz.enums;

import lombok.Getter;

/**
 * @ClassName NetWorkEnum
 * @Description
 * @Author 孟开通
 * @Date 2024/6/11 15:19
 **/
@Getter
public enum NetWorkEnum {
    /**
     * true 连接， false断开 , null 未知
     */
    CONNECT(true, "连接"),
    DISCONNECT(false, "断开"),
    UNKNOWN(null, "未知");

    private Boolean code;

    private String message;

    NetWorkEnum(Boolean code, String message) {
        this.code = code;
        this.message = message;
    }

    public static String getMessage(Boolean code) {
        for (NetWorkEnum value : NetWorkEnum.values()) {
            if (value.code.equals(code)) {
                return value.message;
            }
        }
        return null;
    }
}
