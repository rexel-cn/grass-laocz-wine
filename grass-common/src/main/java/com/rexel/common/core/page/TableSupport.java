package com.rexel.common.core.page;

import com.rexel.common.utils.ServletUtils;

/**
 * 表格数据处理
 *
 * @author ids-admin
 */
public class TableSupport {
    /**
     * 当前记录起始索引
     */
    public static final String PAGE_NUM = "pageNum";

    /**
     * 每页显示记录数
     */
    public static final String PAGE_SIZE = "pageSize";

    /**
     * 排序列
     */
    public static final String ORDER_BY_COLUMN = "orderByColumn";

    /**
     * 排序的方向 "desc" 或者 "asc".
     */
    public static final String IS_ASC = "isAsc";

    /**
     * 分页参数合理化
     */
    public static final String REASONABLE = "reasonable";

    /**
     * 封装分页对象
     */
    public static PageDomain getPageDomain() {
        PageDomain pageDomain = new PageDomain();
        Integer pageNum = ServletUtils.getParameterToInt(PAGE_NUM);
        if (pageNum == null) {
            pageNum = ServletUtils.getPostParameterToInt(PAGE_NUM);
        }
        Integer pageSize = ServletUtils.getParameterToInt(PAGE_SIZE);
        if (pageSize == null) {
            pageSize = ServletUtils.getPostParameterToInt(PAGE_SIZE);
        }

        pageDomain.setPageNum(pageNum == null ? 1 : pageNum);
        pageDomain.setPageSize(pageSize == null ? 10 : pageSize);
        pageDomain.setOrderByColumn(ServletUtils.getParameter(ORDER_BY_COLUMN));
        pageDomain.setIsAsc(ServletUtils.getParameter(IS_ASC));
        return pageDomain;
    }

    public static PageDomain buildPageRequest() {
        return getPageDomain();
    }
}
