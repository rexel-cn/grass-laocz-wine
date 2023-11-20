package com.rexel.framework.security.handle;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.rexel.common.constant.Constants;
import com.rexel.common.constant.HttpStatus;
import com.rexel.common.core.domain.AjaxResult;
import com.rexel.common.core.domain.model.LoginUser;
import com.rexel.common.utils.ServletUtils;
import com.rexel.common.utils.StringUtils;
import com.rexel.framework.manager.AsyncTtlManager;
import com.rexel.framework.manager.factory.AsyncFactory;
import com.rexel.framework.web.service.TokenService;
import com.rexel.tenant.context.TenantContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义退出处理类 返回成功
 *
 * @author ids-admin
 */
@Configuration
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {
    @Autowired
    private TokenService tokenService;

    /**
     * 退出处理
     *
     * @return
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        LoginUser loginUser = tokenService.getLoginUser(request);
        if (StringUtils.isNotNull(loginUser)) {
            String userName = loginUser.getUsername();
            String PhoneNumber = loginUser.getUser().getPhoneNumber();
            // 删除用户缓存记录
            tokenService.delLoginUser(loginUser.getToken());
            String tenantId = StrUtil.isNotEmpty(loginUser.getTenantId()) ? loginUser.getTenantId() : null;
            if (tenantId != null) {
                TenantContextHolder.setTenantId(tenantId);
            }
            // 记录用户退出日志
            AsyncTtlManager.me().execute(AsyncFactory.recordLogininfor(PhoneNumber, userName, loginUser.getUserId(), Constants.LOGOUT, "退出成功"));
        }
        ServletUtils.renderString(response, JSON.toJSONString(AjaxResult.error(HttpStatus.SUCCESS, "退出成功")));
    }
}
