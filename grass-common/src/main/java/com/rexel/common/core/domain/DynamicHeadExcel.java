package com.rexel.common.core.domain;

import lombok.Data;

import java.util.List;

/**
 * @ClassName DynamicHeadExcel
 * @Description 动态列头导出
 * @Author 孟开通
 * @Date 2022/10/25 09:34
 **/
@Data
public class DynamicHeadExcel {
    private List<List<String>> head;
    private List<List<String>> data;
}
