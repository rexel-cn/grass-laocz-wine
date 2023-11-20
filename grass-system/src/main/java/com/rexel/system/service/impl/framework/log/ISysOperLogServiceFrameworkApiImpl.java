package com.rexel.system.service.impl.framework.log;

import cn.hutool.core.bean.BeanUtil;
import com.rexel.log.domain.SysOperLogFramework;
import com.rexel.log.service.ISysOperLogServiceFrameworkApi;
import com.rexel.system.domain.SysOperLog;
import com.rexel.system.service.ISysOperLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName ISysOperLogServiceFrameworkApiImpl
 * @Description
 * @Author 孟开通
 * @Date 2023/1/31 11:26
 **/
@Service
public class ISysOperLogServiceFrameworkApiImpl implements ISysOperLogServiceFrameworkApi {

    @Autowired
    private ISysOperLogService sysOperLogService;

    /**
     * @param operLog
     */
    @Override
    public void insertOperlog(SysOperLogFramework operLog) {
        sysOperLogService.insertOperlog(BeanUtil.copyProperties(operLog, SysOperLog.class));
    }
}
