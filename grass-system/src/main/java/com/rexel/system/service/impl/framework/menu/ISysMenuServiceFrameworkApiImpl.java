package com.rexel.system.service.impl.framework.menu;

import com.rexel.menu.ISysMenuServiceFrameworkApi;
import com.rexel.system.service.ISysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @ClassName ISysMenuServiceFrameworkApiImpl
 * @Description
 * @Author 孟开通
 * @Date 2023/1/31 11:28
 **/
@Service
public class ISysMenuServiceFrameworkApiImpl implements ISysMenuServiceFrameworkApi {

    @Autowired
    private ISysMenuService iSysMenuService;

    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    @Override
    public Set<String> selectMenuPermsByUserId(Long userId) {
        return iSysMenuService.selectMenuPermsByUserId(userId);
    }
}
