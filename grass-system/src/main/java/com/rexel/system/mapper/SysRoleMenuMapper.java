package com.rexel.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rexel.system.domain.SysRoleMenu;
import org.springframework.stereotype.Repository;

/**
 * 角色与菜单关联表 数据层
 *
 * @author ids-admin
 */
@Repository
public interface SysRoleMenuMapper extends BaseMapper<SysRoleMenu> {
    /**
     * 查询菜单使用数量
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    int checkMenuExistRole(Long menuId);
}
