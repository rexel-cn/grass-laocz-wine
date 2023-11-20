package com.rexel.common.utils;

import com.baomidou.mybatisplus.core.toolkit.Sequence;

/**
 * @ClassName SequenceUtil
 * @Description 雪花算法生成id
 * @Author 孟开通
 * @Date 2022/10/25 14:59
 **/
public class SequenceUtils {
    private static final Sequence sequence = new Sequence();

    public static Long nextId() {
        return sequence.nextId();
    }
}
