package com.rexel.laocz.enums;

import lombok.Getter;

/**
 * @ClassName PotteryAltarStateEnum
 * @Description
 * @Author 孟开通
 * @Date 2024/3/13 15:53
 **/
@Getter
public enum PotteryAltarStateEnum {
    /**
     * 1：使用，2封存
     */
    USE(1L, "使用"),

    SEAL(2L, "封存");

    private final Long code;

    private final String desc;

    PotteryAltarStateEnum(Long code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
