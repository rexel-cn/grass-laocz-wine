package com.rexel.laocz.enums;

import lombok.Getter;

/**
 * @ClassName WineRealRunStatusEnum
 * @Description 酒实时运行状态
 * @Author 孟开通
 * @Date 2024/3/12 17:04
 **/
@Getter
public enum WineRealRunStatusEnum {

    /**
     * 0：未开始，1：开始，2：急停，3已完成
     */
    NOT_STARTED(0L, "未开始"),

    STARTED(1L, "开始"),

    EMERGENCY_STOP(2L, "急停"),

    COMPLETED(3L, "已完成");

    private final Long value;

    private final String name;


    WineRealRunStatusEnum(Long value, String name) {
        this.value = value;
        this.name = name;
    }

}
