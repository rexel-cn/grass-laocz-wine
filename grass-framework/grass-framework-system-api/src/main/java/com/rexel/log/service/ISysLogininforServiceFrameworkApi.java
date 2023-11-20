package com.rexel.log.service;


import com.rexel.log.domain.SysLogininforFramework;

/**
 * @ClassName ISysLogininforFrameworkService
 * @Description
 * @Author 孟开通
 * @Date 2023/1/17 17:25
 **/
public interface ISysLogininforServiceFrameworkApi {
    /**
     * 新增系统登录日志
     *
     * @param logininfor 访问日志对象
     */
    void insertLogininfor(SysLogininforFramework logininfor);
}
