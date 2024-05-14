package com.rexel.bpm.enums;

import com.rexel.common.core.IntArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * BPM 模型的表单类型的枚举
 *
 * @author 芋道源码
 */
@Getter
@AllArgsConstructor
public enum BpmModelFormTypeEnum implements IntArrayValuable {

    CUSTOM(20, "业务表单") // 业务自己定义的表单，自己进行数据的存储
    ;

    public static final int[] ARRAYS = Arrays.stream(values()).mapToInt(BpmModelFormTypeEnum::getType).toArray();

    private final Integer type;
    private final String name;

    @Override
    public int[] array() {
        return ARRAYS;
    }

}
