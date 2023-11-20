package com.rexel.system.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.rexel.common.core.domain.entity.SysRole;
import com.rexel.system.domain.dto.role.RoleCreateReqDTO;
import com.rexel.system.domain.dto.role.RolePageReqDTO;
import com.rexel.system.domain.dto.role.RoleUpdateReqDTO;
import com.rexel.system.domain.vo.role.RoleRespVO;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

/**
 * 角色业务层
 *
 * @author ids-admin
 */
public interface ISysRoleService extends IService<SysRole> {
    /**
     * 查询
     *
     * @param createReqVO
     * @return
     */
    List<SysRole> pageRoles(RolePageReqDTO pageReqDTO);

    /**
     * 创建角色
     *
     * @param reqVO 创建角色信息
     * @param type  角色类型
     * @return 角色编号
     */
    Long createRole(@Valid RoleCreateReqDTO reqVO, Integer type);

    /**
     * 更新角色
     *
     * @param reqVO    更新角色信息
     * @param isUpdate true 租户操作可以修改，  false 普通操作不可修改
     */
    void updateRole(@Valid RoleUpdateReqDTO reqVO, Boolean isUpdate);

    /**
     * 删除角色
     *
     * @param id 角色编号
     */
    void deleteRole(Long id);

    /**
     * 获得角色
     *
     * @param id 角色编号
     * @return 角色
     */
    SysRole getRole(Long id);

    List<SysRole> getRoles();

    List<SysRole> selectSysRoleMenuByTenantIdChildren(String tenantId);

    SysRole getSystemRole();

    RoleRespVO selectRoleById(Long roleId);

    List<SysRole> selectCustomRoleAll();

    List<SysRole> selectRolesByUserId(Long userId);

    boolean hasAnySuperAdmin(Set<Long> singleton);
}
