package com.rexel.common.core.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rexel.common.core.domain.AjaxResult;
import com.rexel.common.core.domain.SysHeaderMetadata;
import com.rexel.common.core.domain.model.LoginUser;
import com.rexel.common.core.page.PageDomain;
import com.rexel.common.core.page.PageHeader;
import com.rexel.common.core.page.TableDataInfo;
import com.rexel.common.core.page.TableSupport;
import com.rexel.common.core.service.ISysHeaderMetadataService;
import com.rexel.common.utils.DateUtils;
import com.rexel.common.utils.PageUtils;
import com.rexel.common.utils.SecurityUtils;
import com.rexel.common.utils.StringUtils;
import com.rexel.common.utils.sql.SqlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.beans.PropertyEditorSupport;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * web层通用数据处理
 *
 * @author ids-admin
 */
public class BaseController {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ISysHeaderMetadataService headerMetadataService;

    /**
     * 将前台传递过来的日期格式的字符串，自动转化为Date类型
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // Date 类型转换
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(DateUtils.parseDate(text));
            }
        });
    }

    /**
     * 设置请求分页数据
     */
    protected void startPage() {
        PageUtils.startPage();
    }

    protected void clearPage() {
        PageUtils.clearPage();
    }

    /**
     * 设置请求排序数据
     */
    protected void startOrderBy() {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        if (StringUtils.isNotEmpty(pageDomain.getOrderBy())) {
            String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
            PageHelper.orderBy(orderBy);
        }
    }

    /**
     * 响应请求分页数据
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    protected TableDataInfo getDataTable(List<?> list) {
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(org.springframework.http.HttpStatus.OK.value());
        rspData.setMsg("查询成功");
        rspData.setRows(list);
        rspData.setTotal(new PageInfo(list).getTotal());
        return rspData;
    }

    protected TableDataInfo getDataTable(List<?> list, long total) {
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(org.springframework.http.HttpStatus.OK.value());
        rspData.setMsg("查询成功");
        rspData.setRows(list);
        rspData.setTotal(total);
        return rspData;
    }

    /**
     * 响应请求分页数据
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    protected TableDataInfo getDataTable(List<?> list, String headerName) {
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(org.springframework.http.HttpStatus.OK.value());
        rspData.setMsg(org.springframework.http.HttpStatus.OK.getReasonPhrase());
        //列表信息组装

        PageInfo<?> pageInfo = new PageInfo<>(list == null ? new ArrayList<>() : list);
        //表头信息
        if (StringUtils.isNotBlank(headerName)) {
            SysHeaderMetadata headerMetadataPo = new SysHeaderMetadata();
            headerMetadataPo.setHeaderName(headerName);
            headerMetadataPo.setIsDelete(0L);
            List<PageHeader> pageHeaders = headerMetadataService.selectSysHeaderMetadataList(headerMetadataPo);
            if (null != pageHeaders) {
                rspData.setTableColumnList(pageHeaders);
            }
        }
        rspData.setTotal(pageInfo.getTotal());
        rspData.setRows(list == null ? new ArrayList<>() : list);
        return rspData;
    }

    /**
     * 响应请求分页数据
     *
     * @param list 数据列表
     * @param map  表头信息
     * @return
     */
    protected TableDataInfo getDataTable(List<?> list, Map<String, String> map, long total) {
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(org.springframework.http.HttpStatus.OK.value());
        rspData.setMsg(org.springframework.http.HttpStatus.OK.getReasonPhrase());
        if (CollectionUtil.isNotEmpty(map)) {
            //表头信息
            List<PageHeader> pageHeaders = new ArrayList<>();
            map.forEach((k, v) -> {
                PageHeader pageHeader = new PageHeader();
                pageHeader.setProp(k);
                pageHeader.setPropName(v);
                pageHeaders.add(pageHeader);
            });
            rspData.setTableColumnList(pageHeaders);
        }
        rspData.setTotal(total);
        rspData.setRows(list == null ? new ArrayList<>() : list);
        return rspData;
    }


    protected TableDataInfo getDataTable(Page<?> page) {
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(org.springframework.http.HttpStatus.OK.value());
        rspData.setMsg("查询成功");
        rspData.setRows(page.getRecords());
        rspData.setTotal(new PageInfo(page.getRecords()).getTotal());
        return rspData;
    }

    protected TableDataInfo getDataTable(PageInfo<?> pageInfo, String headerName) {
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(org.springframework.http.HttpStatus.OK.value());
        rspData.setMsg(org.springframework.http.HttpStatus.OK.getReasonPhrase());
        //表头信息
        if (StringUtils.isNotBlank(headerName)) {
            SysHeaderMetadata headerMetadataPo = new SysHeaderMetadata();
            headerMetadataPo.setHeaderName(headerName);
            headerMetadataPo.setIsDelete(0L);
            List<PageHeader> pageHeaders = headerMetadataService.selectSysHeaderMetadataList(headerMetadataPo);
            if (null != pageHeaders) {
                rspData.setTableColumnList(pageHeaders);
            }
        }
        rspData.setTotal(pageInfo.getTotal());
        rspData.setRows(pageInfo.getList() == null ? new ArrayList<>() : pageInfo.getList());
        return rspData;
    }

    /**
     * 返回成功
     */
    public AjaxResult success() {
        return AjaxResult.success();
    }

    /**
     * 返回失败消息
     */
    public AjaxResult error() {
        return AjaxResult.error();
    }

    /**
     * 返回成功消息
     */
    public AjaxResult success(String message) {
        return AjaxResult.success(message);
    }

    /**
     * 返回成功消息
     *
     * @param data
     * @return
     */
    public AjaxResult success(Object data) {
        return AjaxResult.success(data);
    }

    /**
     * 返回失败消息
     */
    public AjaxResult error(String message) {
        return AjaxResult.error(message);
    }

    /**
     * 响应返回结果
     *
     * @param rows 影响行数
     * @return 操作结果
     */
    protected AjaxResult toAjax(int rows) {
        return rows > 0 ? AjaxResult.success() : AjaxResult.error();
    }

    /**
     * 响应返回结果
     *
     * @param result 结果
     * @return 操作结果
     */
    protected AjaxResult toAjax(boolean result) {
        return result ? success() : error();
    }

    /**
     * 页面跳转
     */
    public String redirect(String url) {
        return StringUtils.format("redirect:{}", url);
    }

    /**
     * 获取用户缓存信息
     */
    public LoginUser getLoginUser() {
        return SecurityUtils.getLoginUser();
    }

    /**
     * 获取登录用户id
     */
    public Long getUserId() {
        return getLoginUser().getUserId();
    }
    /**
     * 获取登录用户名
     */
    public String getUsername() {
        return getLoginUser().getUsername();
    }

    /**
     * 获取登录用户名
     */
    public String getTenantId() {
        return getLoginUser().getTenantId();
    }


    /**
     * 获取登录用户名
     */
    public Page getPage() {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        return new Page(pageNum, pageSize);
    }

    protected <T> List<T> page(List<T> list) {
        if (CollectionUtil.isEmpty(list)) {
            return list;
        }
        PageDomain pageDomain = TableSupport.buildPageRequest();
        // 返回分页查询结果
        int pageNum = pageDomain.getPageNum();
        int pageSize = pageDomain.getPageSize();
        if (pageSize * pageNum > list.size()) {
            return list.subList(pageSize * (pageNum - 1), list.size());
        } else {
            return list.subList(pageSize * (pageNum - 1), pageSize * pageNum);
        }
    }
}
