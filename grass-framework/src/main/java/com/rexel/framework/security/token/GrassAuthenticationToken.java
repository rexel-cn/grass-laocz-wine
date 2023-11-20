package com.rexel.framework.security.token;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

import java.util.Collection;

/**
 * @ClassName GrassAuthenticationToken
 * @Description
 * @Author 孟开通
 * @Date 2022/9/30 13:45
 **/
public class GrassAuthenticationToken extends AbstractAuthenticationToken {


    /**
     * 未认证前存储的是 UserPrincipal value 是 账号 邮箱 手机号， 认证后用户信息
     */
    private final Object principal;

    /**
     * 如果是 用户密码认证 这里是密码，手机号就是验证码，邮箱的话是空
     */
    private Object credentials;

    public GrassAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        super.setAuthenticated(true);
    }


    public GrassAuthenticationToken(Object principal, Object credentials) {
        super(null);
        this.principal = principal;
        this.credentials = credentials;
        super.setAuthenticated(false);
    }

    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        Assert.isTrue(!isAuthenticated, "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        super.setAuthenticated(false);
    }

    public void eraseCredentials() {
        super.eraseCredentials();
        this.credentials = null;
    }
}
