package com.rexel.common.utils;

import org.apache.commons.codec.binary.Base64;

import java.nio.charset.StandardCharsets;

/**
 * @author :qichunhui
 * @date :2020/7/21
 */
public class Base64Utils {
    /**
     * BASE64解密
     */
    public static String decode(String str) {
        return new String(Base64.decodeBase64(str), StandardCharsets.UTF_8);
    }

    /**
     * BASE64加密
     */
    public static String encode(String str) {
        return Base64.encodeBase64String(str.getBytes(StandardCharsets.UTF_8));
    }
}
