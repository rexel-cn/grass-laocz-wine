package com.rexel.framework.security.auth;

import com.rexel.common.core.domain.entity.SysTenant;
import com.rexel.common.core.domain.entity.SysUser;
import com.rexel.common.core.domain.model.LoginUser;
import com.rexel.common.core.domain.model.UserPrincipal;
import com.rexel.common.enums.CommonStatusEnum;
import com.rexel.common.enums.UserStatus;
import com.rexel.common.exception.user.TenantDisableException;
import com.rexel.common.exception.user.TenantNotMatchException;
import com.rexel.common.exception.user.UserException;
import com.rexel.framework.security.token.GrassAuthenticationToken;
import com.rexel.framework.web.service.SysPermissionService;
import com.rexel.tenant.context.TenantContextHolder;
import com.rexel.user.ISysTenantServiceFrameworkApi;
import com.rexel.user.ISysUserServiceFrameworkApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @ClassName GrassAuthenticationProvider
 * @Description
 * @Author 孟开通
 * @Date 2022/9/30 11:42
 **/
@Service
public class GrassAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private ISysUserServiceFrameworkApi userService;
    @Autowired
    private SysPermissionService permissionService;

    @Autowired
    private ISysTenantServiceFrameworkApi tenantService;


    /**
     * 密码验证
     */
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public void setPasswordEncoder(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }


    /**
     * 具体验证密码逻辑
     *
     * @param authentication the authentication request object.
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Object principal = authentication.getPrincipal();
        if (!(principal instanceof UserPrincipal)) {
            throw new BadCredentialsException("用户认证失败!");
        }
        UserPrincipal userPrincipal = (UserPrincipal) principal;
        //企业英文名
//        String engName = userPrincipal.getEngName();
        //手机号或帐号
        String value = userPrincipal.getValue();
        //密码
        String passWord = (String) authentication.getCredentials();
        //根据电话查询企业id
        String tenantId = userService.selectTenantIdByPhoneNumber(value);
        //根据企业id查询企业信息
        SysTenant tenant = tenantService.lambdaQuery().eq(SysTenant::getTenantId, tenantId).one();

        //根据企业英文名查询企业信息
//        SysTenant tenant = tenantService.lambdaQuery().eq(SysTenant::getEngName, engName).one();
        //验证企业
        checkTenant(tenant);
        //设置租户id上下文
        TenantContextHolder.setTenantId(tenant.getTenantId());

        SysUser sysUser;
//        switch (userPrincipal.getType()) {
//            case USERNAME:
//                sysUser = userService.selectUserByUserNameAndEngName(value);
//                break;
//            case MOBILE:
//                sysUser = userService.selectUserByPhoneAndEngName(value);
//                break;
//            default:
//                break;
//        }
        sysUser = userService.selectUserByPhoneAndEngName(value);
        checkAuthUser(sysUser);
        checkAuthPassword(passWord, sysUser.getPassword());


        Set<String> menuPermission = permissionService.getMenuPermission(sysUser);
        return new GrassAuthenticationToken(createLoginUser(sysUser, menuPermission), sysUser.getUserId(), getAuthorities(menuPermission));
    }

    private void checkTenant(SysTenant tenant) {
        if (Objects.isNull(tenant)) {
            throw new TenantNotMatchException();
        }
        //是否禁用
        if (CommonStatusEnum.DISABLE.getStatus().equals(tenant.getStatus())) {
            throw new TenantDisableException();
        }

    }

    public UserDetails createLoginUser(SysUser user, Set<String> menuPermission) {
        user.setPassword(null);
        return new LoginUser(user, menuPermission);
    }


    public Collection<? extends GrantedAuthority> getAuthorities(Set<String> menuPermission) {
        Collection<GrantedAuthority> collection = new HashSet<>();
        if (!CollectionUtils.isEmpty(menuPermission)) {
            menuPermission.parallelStream().forEach(role -> collection.add(new SimpleGrantedAuthority(role)));
        }
        return collection;
    }

    private void checkAuthUser(SysUser sysUser) {
        if (Objects.isNull(sysUser)) {
            throw new UserException("user.not.exists", null);
        }
        //是否禁用
        if (UserStatus.DISABLE.getCode().equals(sysUser.getStatus())) {
            throw new UserException("user.blocked", null);
        }
        //是否删除
        if (UserStatus.DELETED.getCode().equals(sysUser.getStatus())) {
            throw new UserException("user.password.delete", null);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (GrassAuthenticationToken.class.isAssignableFrom(authentication));
    }

    private void checkAuthPassword(String targetValue, String matchValue) {
        if (Objects.isNull(targetValue)) {
            throw new UserException("user.password.no.exists", null);
        }
        if (!passwordEncoder.matches(targetValue, matchValue)) {
            throw new UserException("user.password.not.match", null);
        }
    }
}
