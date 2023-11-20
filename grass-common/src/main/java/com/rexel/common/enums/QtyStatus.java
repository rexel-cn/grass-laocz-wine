package com.rexel.common.enums;

/**
 * @ClassName QtyStatus
 * @Description
 * @Author 孟开通
 * @Date 2022/8/19 17:08
 **/
public enum QtyStatus {
    /**
     * 0 正常
     * 1 离线
     */
    OK("0", "正常"), DISABLE("1", "离线");

    private final String code;
    private final String info;

    QtyStatus(String code, String info) {
        this.code = code;
        this.info = info;
    }

    public static String getInfo(String code) {
        for (QtyStatus value : QtyStatus.values()) {
            if (value.code.equals(code)) {
                return value.info;
            }
        }
        throw new IllegalArgumentException("No matching constant for [" + code + "]");
    }

    public String getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }
}
