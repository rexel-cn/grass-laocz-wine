package com.rexel.laocz.enums;

import lombok.Getter;

/**
 * @ClassName WineOperationTypeEnum
 * @Description
 * @Author 孟开通
 * @Date 2024/3/21 09:34
 **/
@Getter
public enum WineOperationTypeEnum {
    /**
     * 操作类型 1:开始 2:急停 3:继续
     */
    START("1", "开始"),

    EMERGENCY_STOP("2", "急停"),

    CONTINUE("3", "继续");

    private final String value;

    private final String name;

    WineOperationTypeEnum(String value, String name) {
        this.value = value;
        this.name = name;
    }

    public static String getNameByValue(String value) {
        for (WineOperationTypeEnum wineOperationTypeEnum : WineOperationTypeEnum.values()) {
            if (wineOperationTypeEnum.getValue().equals(value)) {
                return wineOperationTypeEnum.getValue();
            }
        }
        return null;
    }

}
