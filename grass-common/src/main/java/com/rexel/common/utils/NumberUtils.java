package com.rexel.common.utils;

import cn.hutool.core.util.StrUtil;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName: NumberUtils
 * @Description: 数据工具类
 * @author: hai.dong@rexel.com.cn
 * @date: 2020/3/5
 */
public class NumberUtils implements Serializable {

    public static final DecimalFormat DF_ONE = new DecimalFormat("#.0");
    public static final DecimalFormat DF_TWO = new DecimalFormat("###.00");

    public static Long parseLong(String str) {
        return StrUtil.isNotEmpty(str) ? Long.valueOf(str) : null;
    }
    /**
     * 是否是 数字
     */
    public static final Pattern IS_DIGITAL = Pattern.compile("[0-9]+[.]?[0-9]*[dD]?");


    public static String isNumeric(String str) {
        String result = "";
        StringBuilder max = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            // string判断是否是数字的方法（ASCII）
            int chr = str.charAt(i);
            if (chr > 47 && chr < 58) {
                max.append(str.charAt(i));
                //  结尾是数字不走else的情况
                if (i == str.length() - 1) {
                    result = max.toString();
                }
            } else {
                if (max.length() < result.length()) {
                    max = new StringBuilder();
                } else {
                    result = max.toString();
                    max = new StringBuilder();
                }
            }
        }
        return result;
    }

    /**
     * 转换为百分比
     *
     * @param obj                   数字
     * @param maximumFractionDigits 小数点位数
     * @return
     */
    public static String numToPercentage(Object obj, int maximumFractionDigits) {
        NumberFormat percent = NumberFormat.getPercentInstance();
        percent.setMaximumFractionDigits(maximumFractionDigits);
        return percent.format(obj);
    }

    /**
     * str 小数点保留两位小数
     *
     * @param str
     * @return
     */
    public static String strDecimalTwo(String str) {
        if (StringUtils.isEmpty(str)) {
            return "";
        }
        DecimalFormat format = new DecimalFormat("0.00");
        return format.format(new BigDecimal(str));
    }

    /**
     * 判断字符串是否 为 boolean 类型
     * 601-8073:
     *
     * @param str
     * @return
     */
    public static boolean isNumber(String str) {
        Matcher isNum = IS_DIGITAL.matcher(str);
        return isNum.matches();
    }

    public static String stripTrailingZeros(String str) {
        BigDecimal value = new BigDecimal(str);
        BigDecimal noZeros = value.stripTrailingZeros();
        String result = noZeros.toPlainString();
        return result;
    }


    public static Integer parseInt(String str) {
        return StrUtil.isNotEmpty(str) ? Integer.valueOf(str) : null;
    }
}
