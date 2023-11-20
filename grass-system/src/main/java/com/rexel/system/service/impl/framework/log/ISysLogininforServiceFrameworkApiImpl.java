package com.rexel.system.service.impl.framework.log;

import cn.hutool.core.bean.BeanUtil;
import com.rexel.log.domain.SysLogininforFramework;
import com.rexel.log.service.ISysLogininforServiceFrameworkApi;
import com.rexel.system.domain.SysLogininfor;
import com.rexel.system.service.ISysLogininforService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName ISysLogininforServiceFrameworkApiImpl
 * @Description
 * @Author 孟开通
 * @Date 2023/1/31 11:24
 **/
@Service
public class ISysLogininforServiceFrameworkApiImpl implements ISysLogininforServiceFrameworkApi {

    @Autowired
    private ISysLogininforService iSysLogininforService;

    /**
     * 新增系统登录日志
     *
     * @param logininfor 访问日志对象
     */
    @Override
    public void insertLogininfor(SysLogininforFramework logininfor) {
        iSysLogininforService.insertLogininfor(BeanUtil.copyProperties(logininfor, SysLogininfor.class));
    }
}
