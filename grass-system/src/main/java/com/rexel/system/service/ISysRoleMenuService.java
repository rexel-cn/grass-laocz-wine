package com.rexel.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rexel.system.domain.SysRoleMenu;

import java.util.Collection;
import java.util.List;

/**
 * 角色和菜单关联Service接口
 *
 * @author grass-service
 * @date 2022-12-05
 */
public interface ISysRoleMenuService extends IService<SysRoleMenu> {

    List<SysRoleMenu> selectListByRoleId(Long roleId);

    void deleteListByRoleIdAndMenuIds(Long roleId, Collection<Long> deleteMenuIds);

    void deleteListByRoleId(Long roleId);

    void deleteListByMenuId(Long menuId);
}
