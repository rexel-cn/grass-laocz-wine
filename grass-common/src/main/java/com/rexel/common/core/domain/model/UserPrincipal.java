package com.rexel.common.core.domain.model;

import lombok.Data;

/**
 * @ClassName UserPrincipal
 * @Description
 * @Author 孟开通
 * @Date 2022/9/30 14:14
 **/
@Data
public class UserPrincipal {
    /**
     * 类型
     */
//    private final Type type;
    /**
     * 值
     */
    private final String value;

    /**
     * 公司英文名
     */
//    private String engName;
    public UserPrincipal(String value) {
//        this.engName = engName;
//        this.type = type;
        this.value = value;
    }

//    public enum Type {
//        /**
//         * 用户名
//         */
//        USERNAME,
//        /**
//         * 手机号
//         */
//        MOBILE,
//
//    }
}
