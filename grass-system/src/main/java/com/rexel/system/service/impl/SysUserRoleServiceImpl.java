package com.rexel.system.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.system.domain.SysUserRole;
import com.rexel.system.mapper.SysUserRoleMapper;
import com.rexel.system.service.ISysUserRoleService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用户和角色关联Service业务层处理
 *
 * @author grass-service
 * @date 2022-12-05
 */
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements ISysUserRoleService {


    /**
     * @param userId
     * @return
     */
    @Override
    public List<SysUserRole> selectListByUserId(Long userId) {
        return lambdaQuery().eq(userId != null, SysUserRole::getUserId, userId).list();
    }

    /**
     * @param userId
     * @param deleteRoleIds
     */
    @Override
    public void deleteListByUserIdAndRoleIdIds(Long userId, Collection<Long> deleteRoleIds) {
        lambdaUpdate()
                .eq(userId != null, SysUserRole::getUserId, userId)
                .in(deleteRoleIds != null && !deleteRoleIds.isEmpty(), SysUserRole::getRoleId, deleteRoleIds)
                .remove();

    }

    /**
     * @param roleId
     */
    @Override
    public void deleteListByRoleId(Long roleId) {
        lambdaUpdate()
                .eq(roleId != null, SysUserRole::getRoleId, roleId)
                .remove();
    }

    /**
     * @param userId
     */
    @Override
    public void deleteListByUserId(Long userId) {
        lambdaUpdate()
                .eq(userId != null, SysUserRole::getUserId, userId)
                .remove();
    }

    /**
     * @param userId
     * @return
     */
    @Override
    public Set<Long> selectRoleIdsByUserId(Long userId) {
        return lambdaQuery().eq(userId != null, SysUserRole::getUserId, userId).list().stream().map(SysUserRole::getRoleId).collect(Collectors.toSet());
    }
}
