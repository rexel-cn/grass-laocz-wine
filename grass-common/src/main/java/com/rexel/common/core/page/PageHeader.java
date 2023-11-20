package com.rexel.common.core.page;

import lombok.Data;

/**
 * @author 董海
 * @description 表头组合
 * @date 2020/1/17
 */
@Data
public class PageHeader {

    /**
     * 字段  id
     */
    private String prop;
    /**
     * 字段显示
     */
    private String propName;
    /**
     * 描述
     */
    private String propDescribtion;
    /**
     * 是否 排序
     */
    private boolean hasSort;
}
