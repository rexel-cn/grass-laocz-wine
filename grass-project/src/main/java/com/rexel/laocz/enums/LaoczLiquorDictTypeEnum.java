package com.rexel.laocz.enums;

import lombok.Getter;

/**
 * @ClassName LaoczLiquorDictTypeEnum
 * @Description 酒品字典枚举
 * @Author 孟开通
 * @Date 2024/5/18 15:28
 **/
@Getter
public enum LaoczLiquorDictTypeEnum {
    /**
     * 1:酒品信息,2:酒业等级信息
     */
    WINE_INFO(1L, "酒品信息"),
    WINE_GRADE_INFO(2L, "酒业等级信息");

    private final Long code;

    private final String desc;

    LaoczLiquorDictTypeEnum(Long code, String desc) {
        this.code = code;
        this.desc = desc;
    }


}
