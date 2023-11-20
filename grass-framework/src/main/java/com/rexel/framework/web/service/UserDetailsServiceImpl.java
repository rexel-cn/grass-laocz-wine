package com.rexel.framework.web.service;


import com.rexel.common.core.domain.entity.SysTenant;
import com.rexel.common.core.domain.entity.SysUser;
import com.rexel.common.core.domain.model.LoginUser;
import com.rexel.common.enums.CommonStatusEnum;
import com.rexel.common.enums.UserStatus;
import com.rexel.common.exception.ServiceException;
import com.rexel.common.exception.user.TenantDisableException;
import com.rexel.common.exception.user.TenantNotMatchException;
import com.rexel.common.utils.StringUtils;
import com.rexel.tenant.context.TenantContextHolder;
import com.rexel.user.ISysTenantServiceFrameworkApi;
import com.rexel.user.ISysUserServiceFrameworkApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 用户验证处理
 *
 * @author ids-admin
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);


    @Autowired
    private ISysUserServiceFrameworkApi userService;
    @Autowired
    private SysPermissionService permissionService;
    @Autowired
    private ISysTenantServiceFrameworkApi tenantService;


    @Override
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        SysUser user = userService.selectUserByPhoneNumber(phoneNumber);
        if (StringUtils.isNull(user)) {
            log.info("登录用户：{} 不存在.", phoneNumber);
            throw new ServiceException("登录用户：" + phoneNumber + " 不存在");
        } else if (UserStatus.DELETED.getCode().equals(user.getDelFlag())) {
            log.info("登录用户：{} 已被删除.", phoneNumber);
            throw new ServiceException("对不起，您的账号：" + phoneNumber + " 已被删除");
        } else if (UserStatus.DISABLE.getCode().equals(user.getStatus())) {
            log.info("登录用户：{} 已被停用.", phoneNumber);
            throw new ServiceException("对不起，您的账号：" + phoneNumber + " 已停用");
        }

        //根据电话查询企业id
        String tenantId = userService.selectTenantIdByPhoneNumber(phoneNumber);
        //根据企业id查询企业信息
        SysTenant tenant = tenantService.lambdaQuery().eq(SysTenant::getTenantId, tenantId).one();
        //验证企业
        checkTenant(tenant);

        //设置租户id上下文
        TenantContextHolder.setTenantId(user.getTenantId());
        return createLoginUser(user);
    }

    public UserDetails createLoginUser(SysUser user) {
        return new LoginUser(user, permissionService.getMenuPermission(user));
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

}
