package com.rexel.laocz.enums;

import lombok.Getter;

/**
 * @ClassName OperatingStatusEnum
 * @Description
 * @Author 孟开通
 * @Date 2024/3/19 11:40
 **/
@Getter
public enum OperatingStatusEnum {
    /**
     * 运行状态（0：占用，1：存储，2：入酒中，3：出酒中，4：倒坛中）
     */
    OCCUPY(0L, "占用"),
    STORAGE(1L, "存储"),
    WINE_IN(2L, "入酒中"),
    WINE_OUT(3L, "出酒中"),
    POUR(4L, "倒坛中");

    private final Long code;

    private final String desc;

    OperatingStatusEnum(Long code, String desc) {
        this.code = code;
        this.desc = desc;
    }


}
