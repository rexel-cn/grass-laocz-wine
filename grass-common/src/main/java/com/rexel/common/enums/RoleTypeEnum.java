package com.rexel.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleTypeEnum {

    /**
     * 管理员
     */
    SYSTEM(0),
    /**
     * 普通角色
     */
    CUSTOM(1);

    private final Integer type;

}
