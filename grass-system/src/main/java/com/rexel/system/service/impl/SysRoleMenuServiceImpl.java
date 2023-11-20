package com.rexel.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.system.domain.SysRoleMenu;
import com.rexel.system.mapper.SysRoleMenuMapper;
import com.rexel.system.service.ISysRoleMenuService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * 角色和菜单关联Service业务层处理
 *
 * @author grass-service
 * @date 2022-12-05
 */
@Service
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements ISysRoleMenuService {

    /**
     * @param roleId
     * @return
     */
    @Override
    public List<SysRoleMenu> selectListByRoleId(Long roleId) {
        return lambdaQuery().eq(roleId != null, SysRoleMenu::getRoleId, roleId).list();
    }

    /**
     * @param roleId
     * @param deleteMenuIds
     */
    @Override
    public void deleteListByRoleIdAndMenuIds(Long roleId, Collection<Long> deleteMenuIds) {
        lambdaUpdate()
                .eq(roleId != null, SysRoleMenu::getRoleId, roleId)
                .in(deleteMenuIds != null && !deleteMenuIds.isEmpty(), SysRoleMenu::getMenuId, deleteMenuIds)
                .remove();
    }

    /**
     * @param roleId
     */
    @Override
    public void deleteListByRoleId(Long roleId) {
        lambdaUpdate()
                .eq(roleId != null, SysRoleMenu::getRoleId, roleId)
                .remove();
    }

    /**
     * @param menuId
     */
    @Override
    public void deleteListByMenuId(Long menuId) {
        lambdaUpdate()
                .eq(menuId != null, SysRoleMenu::getMenuId, menuId)
                .remove();
    }
}
