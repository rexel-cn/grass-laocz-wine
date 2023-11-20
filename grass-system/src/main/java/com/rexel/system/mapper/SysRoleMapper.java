package com.rexel.system.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rexel.common.core.domain.entity.SysRole;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 角色表 数据层
 *
 * @author ids-admin
 */
@Repository
public interface SysRoleMapper extends BaseMapper<SysRole> {

    List<SysRole> selectRolesByUserId(Long userId);


    /**
     * 通过角色ID查询角色
     *
     * @param roleId 角色ID
     * @return 角色对象信息
     */
    SysRole selectRoleById(Long roleId);

    /**
     * 查询排序最大值
     *
     * @return
     */
    Long selectMaxRoleSort(Long roleId);

    @InterceptorIgnore(tenantLine = "on")
    List<SysRole> selectSysRoleMenuByTenantIdChildren(String tenantId);
}
