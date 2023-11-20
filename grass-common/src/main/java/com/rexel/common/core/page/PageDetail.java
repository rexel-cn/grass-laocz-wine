package com.rexel.common.core.page;

import lombok.Data;

import java.util.List;

/**
 * @author 董海
 * @description 表头加列表数据
 * @date 2020/1/17
 */
@Data
public class PageDetail {

    /**
     * 总条数
     */
    private Long total;
    /**
     * 列表数据
     */
    private List<?> dataList;
    /**
     * 表头数据
     */
    private List<PageHeader> tableColumnList;
}
