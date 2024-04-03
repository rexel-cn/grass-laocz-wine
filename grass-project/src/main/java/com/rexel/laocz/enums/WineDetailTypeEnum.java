package com.rexel.laocz.enums;

import lombok.Getter;

/**
 * @ClassName WineDetailTypeEnum
 * @Description
 * @Author 孟开通
 * @Date 2024/3/29 14:53
 **/
@Getter
public enum WineDetailTypeEnum {
    /**
     * 操作详细类型：1:入酒，2出酒，3倒坛入，4倒坛出，5取样
     */
    IN_WINE(1L, "入酒"),
    OUT_WINE(2L, "出酒"),
    POUR_IN(3L, "倒坛入"),
    POUR_OUT(4L, "倒坛出"),
    SAMPLING(5L, "取样"),
    NULL_ENUM(0L, "未知");

    private final Long code;

    private final String desc;

    WineDetailTypeEnum(Long code, String desc) {
        this.code = code;
        this.desc = desc;
    }


    public static WineDetailTypeEnum getEnumByCode(Long detailType) {
        for (WineDetailTypeEnum wineDetailTypeEnum : WineDetailTypeEnum.values()) {
            if (wineDetailTypeEnum.getCode().equals(detailType)) {
                return wineDetailTypeEnum;
            }
        }
        return NULL_ENUM;
    }
}
