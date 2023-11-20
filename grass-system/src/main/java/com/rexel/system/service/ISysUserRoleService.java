package com.rexel.system.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.rexel.system.domain.SysUserRole;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 用户和角色关联Service接口
 *
 * @author grass-service
 * @date 2022-12-05
 */
public interface ISysUserRoleService extends IService<SysUserRole> {


    List<SysUserRole> selectListByUserId(Long userId);

    void deleteListByUserIdAndRoleIdIds(Long userId, Collection<Long> deleteRoleIds);

    void deleteListByRoleId(Long roleId);

    void deleteListByUserId(Long userId);

    Set<Long> selectRoleIdsByUserId(Long userId);
}
