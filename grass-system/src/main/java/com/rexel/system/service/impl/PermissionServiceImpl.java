package com.rexel.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.rexel.common.core.domain.entity.SysMenu;
import com.rexel.common.utils.CollectionUtils;
import com.rexel.system.domain.SysRoleMenu;
import com.rexel.system.domain.SysUserPost;
import com.rexel.system.domain.SysUserRole;
import com.rexel.system.mq.RedisSystemProducer;
import com.rexel.system.service.*;
import com.rexel.tenant.context.TenantContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @ClassName PermissionServiceImpl
 * @Description
 * @Author 孟开通
 * @Date 2022/12/5 14:01
 **/
@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private ISysRoleMenuService sysRoleMenuService;

    @Autowired
    private ISysUserPostService sysUserPostService;

    @Autowired
    private ISysUserRoleService sysUserRoleService;

    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private ISysMenuService menuService;

    @Autowired
    private RedisSystemProducer redisSystemProducer;


    /**
     * 获得角色拥有的菜单编号集合
     *
     * @param roleId 角色编号
     * @return 菜单编号集合
     */
    @Override
    public Set<Long> getRoleMenuIds(Long roleId) {
        //如果是管理员的情况下，获取全部菜单编号
        if (roleService.hasAnySuperAdmin(Collections.singleton(roleId))) {
            return menuService.getMenus().stream().map(SysMenu::getMenuId).collect(Collectors.toSet());
        }
        // 如果是非管理员的情况下，获得拥有的菜单编号
        return sysRoleMenuService.selectListByRoleId(roleId).stream().map(SysRoleMenu::getMenuId).collect(Collectors.toSet());
    }

    /**
     * @param roleId
     * @return
     */
    @Override
    public Integer getRoleUserCount(Long roleId) {
        return sysUserRoleService.lambdaQuery().eq(SysUserRole::getRoleId, roleId).count();
    }

    /**
     * 设置角色菜单
     *
     * @param roleId  角色编号
     * @param menuIds 菜单编号集合
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignRoleMenu(Long roleId, Set<Long> menuIds) {
        // 获得角色拥有菜单编号
        Set<Long> dbMenuIds = sysRoleMenuService.selectListByRoleId(roleId).stream().map(SysRoleMenu::getMenuId).collect(Collectors.toSet());
        // 计算新增和删除的菜单编号
        Collection<Long> createMenuIds = CollUtil.subtract(menuIds, dbMenuIds);
        Collection<Long> deleteMenuIds = CollUtil.subtract(dbMenuIds, menuIds);
        // 执行新增和删除。对于已经授权的菜单，不用做任何处理
        if (!CollectionUtil.isEmpty(createMenuIds)) {
            sysRoleMenuService.saveBatch(createMenuIds.stream().map(menuId -> {
                SysRoleMenu roleMenu = new SysRoleMenu();
                roleMenu.setRoleId(roleId);
                roleMenu.setMenuId(menuId);
                return roleMenu;
            }).collect(Collectors.toList()));
        }
        if (!CollectionUtil.isEmpty(deleteMenuIds)) {
            sysRoleMenuService.deleteListByRoleIdAndMenuIds(roleId, deleteMenuIds);
        }
        if (CollectionUtil.isNotEmpty(createMenuIds) || CollectionUtil.isNotEmpty(deleteMenuIds)) {
            //如果租户调用，设置为false,否则设置为true   设置为false 暂时加入集合，等到事务提交后，再发送消息
            if (RedisSystemProducer.IS_SEND_MESSAGE.get() == null || !RedisSystemProducer.IS_SEND_MESSAGE.get()) {
                redisSystemProducer.sendUserRefreshMessage(null, TenantContextHolder.getTenantId());
                return;
            }
            //不是租户调用，事务结束后发送
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    redisSystemProducer.sendUserRefreshMessage(null, null);
                }
            });
        }
    }

    /**
     * 获得用户拥有的角色编号集合
     *
     * @param userId 用户编号
     * @return 角色编号集合
     */
    @Override
    public Set<Long> getUserRoleIdListByUserId(Long userId) {
        return sysUserRoleService.selectListByUserId(userId).stream().map(SysUserRole::getRoleId).collect(Collectors.toSet());
    }

    /**
     * 设置用户角色
     *
     * @param userId  角色编号
     * @param roleIds 角色编号集合
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignUserRole(Long userId, Set<Long> roleIds) {
        // 获得角色拥有角色编号
        Set<Long> dbRoleIds = sysUserRoleService.selectListByUserId(userId).stream().map(SysUserRole::getRoleId).collect(Collectors.toSet());
        // 计算新增和删除的角色编号
        Collection<Long> createRoleIds = CollUtil.subtract(roleIds, dbRoleIds);
        Collection<Long> deleteRoleIds = CollUtil.subtract(dbRoleIds, roleIds);
        // 执行新增和删除。对于已经授权的角色，不用做任何处理
        if (!CollectionUtil.isEmpty(createRoleIds)) {
            sysUserRoleService.saveBatch(CollectionUtils.convertList(createRoleIds, roleId -> {
                SysUserRole entity = new SysUserRole();
                entity.setUserId(userId);
                entity.setRoleId(roleId);
                return entity;
            }));
        }
        if (!CollectionUtil.isEmpty(deleteRoleIds)) {
            sysUserRoleService.deleteListByUserIdAndRoleIdIds(userId, deleteRoleIds);
        }
    }
    /**
     * @param userId
     * @param postIds
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignUserPost(Long userId, Set<Long> postIds) {
        // 获得角色拥有角色编号
        Set<Long> dbPostIds = sysUserPostService.selectListByUserId(userId).stream().map(SysUserPost::getPostId).collect(Collectors.toSet());
        // 计算新增和删除的角色编号
        Collection<Long> createPostIds = CollUtil.subtract(postIds, dbPostIds);
        Collection<Long> deletePostIds = CollUtil.subtract(dbPostIds, postIds);
        // 执行新增和删除。对于已经授权的角色，不用做任何处理
        if (!CollectionUtil.isEmpty(createPostIds)) {
            sysUserPostService.saveBatch(CollectionUtils.convertList(createPostIds, postId -> {
                SysUserPost entity = new SysUserPost();
                entity.setUserId(userId);
                entity.setPostId(postId);
                return entity;
            }));
        }
        if (!CollectionUtil.isEmpty(deletePostIds)) {
            sysUserPostService.deleteListByUserIdAndPostIds(userId, deletePostIds);
        }
    }

    /**
     * 处理角色删除时，删除关联授权数据
     *
     * @param roleId 角色编号
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void processRoleDeleted(Long roleId) {
        // 标记删除 UserRole
        sysUserRoleService.deleteListByRoleId(roleId);
        // 标记删除 RoleMenu
        sysRoleMenuService.deleteListByRoleId(roleId);
    }

    /**
     * 处理菜单删除时，删除关联授权数据
     *
     * @param menuId 菜单编号
     */
    @Override
    public void processMenuDeleted(Long menuId) {
        sysRoleMenuService.deleteListByMenuId(menuId);
    }

    /**
     * 处理用户删除是，删除关联授权数据
     *
     * @param userId 用户编号
     */
    @Override
    public void processUserDeleted(Long userId) {
        sysUserRoleService.deleteListByUserId(userId);
    }

    /**
     *
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByTenantId() {
        sysRoleMenuService.lambdaUpdate().remove();
        sysUserRoleService.lambdaUpdate().remove();
        sysUserPostService.lambdaUpdate().remove();
    }
}
