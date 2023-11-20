package com.rexel.common.utils;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.rexel.common.core.page.PageDomain;
import com.rexel.common.core.page.TableSupport;
import com.rexel.common.utils.sql.SqlUtil;

import java.util.ArrayList;
import java.util.List;

public class PageUtils extends PageHelper {
    /**
     * 设置请求分页数据
     */
    public static void startPage() {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
        PageHelper.startPage(pageNum, pageSize, orderBy);
    }

    /**
     * 清理分页的线程变量
     */
    public static void clearPage() {
        PageHelper.clearPage();
    }


    /**
     * 分页函数
     * @author pochettino
     * @param currentPage   当前页数
     * @param pageSize  每一页的数据条数
     * @param list  要进行分页的数据列表
     * @return  当前页要展示的数据
     */
    public static Page getPages(Integer currentPage, Integer pageSize, List list) {
        Page page = new Page();
        if(list==null){
            return  null;
        }
        int size = list.size();

        if(pageSize > size) {
            pageSize = size;
        }
        if (pageSize!=0){
            // 求出最大页数，防止currentPage越界
            int maxPage = size % pageSize == 0 ? size / pageSize : size / pageSize + 1;

            if(currentPage > maxPage) {
                currentPage = maxPage;
            }
        }
        // 当前页第一条数据的下标
        int curIdx = currentPage > 1 ? (currentPage - 1) * pageSize : 0;

        List pageList = new ArrayList();

        // 将当前页的数据放进pageList
        for(int i = 0; i < pageSize && curIdx + i < size; i++) {
            pageList.add(list.get(curIdx + i));
        }

        page.setCurrent(currentPage).setSize(pageSize).setTotal(list.size()).setRecords(pageList);
        return page;
    }

}