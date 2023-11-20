package com.rexel.config;


/**
 * 参数配置 服务层
 *
 * @author ids-admin
 */
public interface ISysConfigServiceFrameworkApi {

    /**
     * 获取验证码开关
     *
     * @return true开启，false关闭
     */
    public boolean selectCaptchaOnOff();

}
