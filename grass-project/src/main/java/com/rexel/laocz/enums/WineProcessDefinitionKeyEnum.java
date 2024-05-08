package com.rexel.laocz.enums;

import lombok.Getter;

/**
 * @ClassName WineProcessDefinitionKey
 * @Description 酒类流程定义的 Key 枚举
 * @Author 孟开通
 * @Date 2024/4/28 13:22
 **/
@Getter
public enum WineProcessDefinitionKeyEnum {
    /**
     * 出酒
     * 入酒
     * 倒坛
     * 取样
     */
    OUT_WINE("out_wine","出酒"),
    IN_WINE("in_wine","入酒"),
    POUR_TANK("pour_tank","倒坛"),
    SAMPLING("sampling","取样");

    private final String key;

    private final String desc;

    WineProcessDefinitionKeyEnum(String key, String desc) {
        this.key = key;
        this.desc = desc;
    }

}
