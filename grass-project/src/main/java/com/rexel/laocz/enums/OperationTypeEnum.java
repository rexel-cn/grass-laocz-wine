package com.rexel.laocz.enums;

import lombok.Getter;

@Getter
public enum OperationTypeEnum {
    /**
     * 1：入酒
     * 2：出酒
     * 3：倒坛
     * 4：取样
     */
    WINE_ENTRY(1L, "入酒"),
    WINE_OUT(2L, "出酒"),
    POUR_POT(3L, "倒坛"),
    SAMPLING(4L, "取样"),
    NULL_ENUM(0L, "未知");

    private final Long value;
    private final String name;

    OperationTypeEnum(Long value, String name) {
        this.value = value;
        this.name = name;
    }

    public static String getNameByValue(Long value) {
        for (OperationTypeEnum operationTypeEnum : OperationTypeEnum.values()) {
            if (operationTypeEnum.getValue().equals(value)) {
                return operationTypeEnum.getName();
            }
        }
        return null;
    }

    public static OperationTypeEnum getByValue(Long value) {
        for (OperationTypeEnum operationTypeEnum : OperationTypeEnum.values()) {
            if (operationTypeEnum.getValue().equals(value)) {
                return operationTypeEnum;
            }
        }
        return NULL_ENUM;
    }

}
