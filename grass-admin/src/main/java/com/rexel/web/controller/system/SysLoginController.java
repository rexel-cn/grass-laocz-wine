package com.rexel.web.controller.system;

import com.rexel.common.constant.Constants;
import com.rexel.common.core.domain.AjaxResult;
import com.rexel.common.core.domain.entity.SysMenu;
import com.rexel.common.core.domain.entity.SysTenant;
import com.rexel.common.core.domain.entity.SysUser;
import com.rexel.common.core.domain.model.LoginBody;
import com.rexel.common.utils.SecurityUtils;
import com.rexel.framework.web.service.SysLoginService;
import com.rexel.framework.web.service.SysPermissionService;
import com.rexel.system.service.ISysMenuService;
import com.rexel.system.service.ISysTenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

/**
 * 登录验证
 *
 * @author ids-admin
 */
@RestController
public class SysLoginController {
    @Autowired
    private SysLoginService loginService;

    @Autowired
    private ISysMenuService menuService;

    @Autowired
    private SysPermissionService permissionService;


    @Autowired
    private ISysTenantService sysTenantService;


    /**
     * 登录方法
     *
     * @param loginBody 登录信息
     * @return 结果
     */
    @PostMapping("/login")
    public AjaxResult login(@RequestBody @Validated LoginBody loginBody) {
        AjaxResult ajax = AjaxResult.success();
        // 生成令牌
        String token = loginService.login( loginBody.getPhoneNumber(), loginBody.getPassword(), loginBody.getCode(),
                loginBody.getUuid());
        ajax.put(Constants.TOKEN, token);
        return ajax;
    }

    /**
     * 获取token
     * @param loginBody
     * @return
     */
    @PostMapping("/getToken")
    public AjaxResult getToken(@RequestBody @Validated LoginBody loginBody){
        AjaxResult ajax = AjaxResult.success();
        // 生成令牌
        String token = loginService.login( loginBody.getPhoneNumber(), loginBody.getPassword());
        ajax.put(Constants.TOKEN, token);
        return ajax;
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping("getInfo")
    public AjaxResult getInfo() {
        SysUser user = SecurityUtils.getLoginUser().getUser();

        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(user);
        SysTenant one = sysTenantService.lambdaQuery().eq(SysTenant::getTenantId, SecurityUtils.getTenantId()).one();
        AjaxResult ajax = AjaxResult.success();
        ajax.put("user", user);

        ajax.put("permissions", permissions);
        ajax.put("tenantLogo", one.getLogo());
        return ajax;
    }

    /**
     * 获取路由信息
     *
     * @return 路由信息
     */
    @GetMapping("getRouters")
    public AjaxResult getRouters() {
        Long userId = SecurityUtils.getUserId();
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(userId);
        return AjaxResult.success(menuService.buildMenus(menus));
    }
}
