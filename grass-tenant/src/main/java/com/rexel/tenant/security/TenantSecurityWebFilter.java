package com.rexel.tenant.security;

import com.alibaba.fastjson2.JSON;
import com.rexel.common.constant.HttpStatus;
import com.rexel.common.core.domain.AjaxResult;
import com.rexel.common.core.domain.model.LoginUser;
import com.rexel.common.utils.SecurityUtils;
import com.rexel.common.utils.ServletUtils;
import com.rexel.tenant.context.TenantContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * 多租户 Security Web 过滤器
 * 1. 如果是登陆的用户，校验是否有权限访问该租户，避免越权问题。
 * 2. 如果请求未带租户的编号，检查是否是忽略的 URL，否则也不允许访问。
 * 3. 校验租户是合法，例如说被禁用、到期
 *
 * @author
 */
@Slf4j
public class TenantSecurityWebFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String tenantId = TenantContextHolder.getTenantId();
        // 1. 登陆的用户，校验是否有权限访问该租户，避免越权问题。
        LoginUser user = SecurityUtils.getLoginUser();
        if (user != null) {
            // 如果获取不到租户编号，则尝试使用登陆用户的租户编号
            if (tenantId == null) {
                tenantId = user.getTenantId();
                TenantContextHolder.setTenantId(tenantId);
                // 如果传递了租户编号，则进行比对租户编号，避免越权问题
            } else if (!Objects.equals(user.getTenantId(), TenantContextHolder.getTenantId())) {
                log.error("[doFilterInternal][租户({}) User({}/{}) 越权访问租户({}) URL({}/{})]",
                        user.getTenantId(), user.getUserId(), user.getUser().getUserType(),
                        TenantContextHolder.getTenantId(), request.getRequestURI(), request.getMethod());
                ServletUtils.renderString(response, JSON.toJSONString(AjaxResult.error(HttpStatus.UNAUTHORIZED, "您无权访问该租户的数据")));
                return;
            }
        }
        // 继续过滤
        chain.doFilter(request, response);
    }

}
