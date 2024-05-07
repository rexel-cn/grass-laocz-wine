package com.rexel.laocz.enums;

import lombok.Getter;

/**
 * @ClassName WineOutTypeEnum
 * @Description
 * @Author 孟开通
 * @Date 2024/5/7 14:57
 **/
@Getter
public enum WineOutTypeEnum {
    /**
     *  1出酒前，2出酒后
     */

    WINE_OUT_BEFORE(1, "出酒前"),

    WINE_OUT_AFTER(2, "出酒后");

    private final Integer code;

    private final String info;

    WineOutTypeEnum(Integer code, String info) {
        this.code = code;
        this.info = info;
    }

}
