package com.rexel.common.utils;

import cn.hutool.core.util.StrUtil;
import com.rexel.common.constant.JudgeConstant;

import java.math.BigDecimal;

/**
 * @ClassName JudgeUtils
 * @Description 判断工具类
 * @Author 孟开通
 * @Date 2022/11/8 17:26
 **/
public class JudgeUtils {
    /**
     * @param judge       判断符号
     * @param thisValue   当前值
     * @param targetValue 目标值
     * @return
     */
    public static boolean judge(String judge, BigDecimal thisValue, BigDecimal targetValue) {
        if (StrUtil.isEmpty(judge) || thisValue == null || targetValue == null) {
            throw new RuntimeException("judge is null or thisValue or targetValue is null");
        }
        //判断
        switch (judge) {
            case JudgeConstant.GREATER_THAN:
                return thisValue.compareTo(targetValue) > 0;
            case JudgeConstant.LESS_THAN:
                return thisValue.compareTo(targetValue) < 0;
            case JudgeConstant.EQUAL:
                return thisValue.compareTo(targetValue) == 0;
            case JudgeConstant.NOT_EQUAL:
                return thisValue.compareTo(targetValue) != 0;
            case JudgeConstant.GREATER_THAN_OR_EQUAL:
                return thisValue.compareTo(targetValue) >= 0;
            case JudgeConstant.LESS_THAN_OR_EQUAL:
                return thisValue.compareTo(targetValue) <= 0;
            default:
                throw new RuntimeException("judge is not support");
        }
    }
}
