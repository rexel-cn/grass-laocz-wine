package com.rexel.menu;


import java.util.Set;

/**
 * 菜单 业务层
 *
 * @author ids-admin
 */
public interface ISysMenuServiceFrameworkApi {

    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    public Set<String> selectMenuPermsByUserId(Long userId);

}
