package com.rexel.system.service.impl.framework.config;

import com.rexel.config.ISysConfigServiceFrameworkApi;
import com.rexel.system.service.ISysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName ISysConfigServiceFrameworkApiImpl
 * @Description
 * @Author 孟开通
 * @Date 2023/1/31 11:23
 **/
@Service
public class ISysConfigServiceFrameworkApiImpl implements ISysConfigServiceFrameworkApi {
    @Autowired
    private ISysConfigService iSysConfigService;

    /**
     * 获取验证码开关
     *
     * @return true开启，false关闭
     */
    @Override
    public boolean selectCaptchaOnOff() {
        return iSysConfigService.selectCaptchaOnOff();
    }
}
