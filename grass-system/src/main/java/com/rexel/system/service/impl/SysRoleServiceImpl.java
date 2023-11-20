package com.rexel.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.common.core.domain.entity.SysRole;
import com.rexel.common.enums.CommonStatusEnum;
import com.rexel.common.enums.RoleCodeEnum;
import com.rexel.common.enums.RoleTypeEnum;
import com.rexel.common.exception.ServiceException;
import com.rexel.system.domain.dto.role.RoleCreateReqDTO;
import com.rexel.system.domain.dto.role.RolePageReqDTO;
import com.rexel.system.domain.dto.role.RoleUpdateReqDTO;
import com.rexel.system.domain.vo.role.RoleRespVO;
import com.rexel.system.mapper.SysRoleMapper;
import com.rexel.system.mq.RedisSystemProducer;
import com.rexel.system.service.ISysMenuService;
import com.rexel.system.service.ISysRoleService;
import com.rexel.system.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;
import java.util.Set;

/**
 * @ClassName SysRoleServiceImpl1
 * @Description
 * @Author 孟开通
 * @Date 2022/12/5 11:29
 **/
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private ISysMenuService menuService;
    @Autowired
    private RedisSystemProducer redisSystemProducer;

    /**
     * 查询
     *
     * @param pageReqDTO
     * @return
     */
    @Override
    public List<SysRole> pageRoles(RolePageReqDTO pageReqDTO) {
        return lambdaQuery()
                .like(StrUtil.isNotEmpty(pageReqDTO.getRoleName()), SysRole::getRoleName, pageReqDTO.getRoleName())
                .orderByAsc(SysRole::getRoleSort)
                .list();
    }

    /**
     * 创建角色
     *
     * @param reqVO 创建角色信息
     * @param type  角色类型
     * @return 角色编号
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createRole(RoleCreateReqDTO reqVO, Integer type) {
        // 校验角色
        checkDuplicateRole(reqVO.getRoleName(), null);
        // 插入到数据库
        SysRole role = BeanUtil.copyProperties(reqVO, SysRole.class);
        role.setRoleType(ObjectUtil.defaultIfNull(type, RoleTypeEnum.CUSTOM.getType()));
        role.setRoleKey(ObjectUtil.defaultIfNull(reqVO.getRoleKey(), RoleCodeEnum.ADMIN.getCode()));
        role.setStatus(CommonStatusEnum.ENABLE.getStatus());
        //设置排序
        role.setRoleSort(getRoleSort(role));
        baseMapper.insert(role);
        permissionService.assignRoleMenu(role.getRoleId(), reqVO.getMenuIds());
        // 返回
        return role.getRoleId();
    }

    private Long getRoleSort(SysRole role) {
        Integer count = lambdaQuery()
                .eq(SysRole::getRoleSort, role.getRoleSort())
                .ne(role.getRoleId() != null, SysRole::getRoleId, role.getRoleId())
                .count();
        if (count != null && count > 0) {
            Long roleSort = role.getRoleSort() + 1L;
            Long integer = baseMapper.selectMaxRoleSort(role.getRoleId());
            if (integer == null) {
                return 0L;
            } else if (integer.equals(roleSort)) {
                return integer + 1L;
            } else {
                return roleSort;
            }
        }
        return role.getRoleSort();
    }

    /**
     * 更新角色
     *
     * @param reqVO 更新角色信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRole(RoleUpdateReqDTO reqVO, Boolean isUpdate) {
        // 校验是否可以更新
        checkUpdateRole(reqVO.getRoleId(), isUpdate);
        // 校验角色的唯一字段是否重复
        checkDuplicateRole(reqVO.getRoleName(), reqVO.getRoleId());
        // 更新到数据库
        SysRole updateObject = BeanUtil.copyProperties(reqVO, SysRole.class);
        updateObject.setRoleSort(getRoleSort(updateObject));
        updateById(updateObject);
        permissionService.assignRoleMenu(reqVO.getRoleId(), reqVO.getMenuIds());
        // 发送刷新消息
    }

    public SysRole checkUpdateRole(Long id, Boolean isUpdate) {
        SysRole roleDO = getById(id);
        if (roleDO == null) {
            throw new ServiceException("角色不存在");
        }
        // true 租户操作可以修改，  false 普通操作不可修改
        if (isUpdate != null || isUpdate) {
            return roleDO;
        }
        // 内置角色，不允许删除
        if (RoleTypeEnum.SYSTEM.getType().equals(roleDO.getRoleType())) {
            throw new ServiceException("不能操作类型为系统内置的角色");
        }
        return roleDO;
    }

    /**
     * 删除角色
     *
     * @param id 角色编号
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRole(Long id) {
        // 校验是否可以更新
        SysRole sysRole = this.checkUpdateRole(id, false);
        Integer roleUserCount = permissionService.getRoleUserCount(id);
        if (ObjectUtil.isNotNull(roleUserCount) && roleUserCount > 0) {
            throw new ServiceException(String.format("%1$s已分配,不能删除", sysRole.getRoleName()));
        }
        // 删除
        removeById(id);
        // 删除相关数据
        permissionService.processRoleDeleted(id);

        // 发送刷新消息. 注意，需要事务提交后，在进行发送刷新消息。不然 db 还未提交，结果缓存先刷新了
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {

            @Override
            public void afterCommit() {
                redisSystemProducer.sendUserRefreshMessage(null, null);
            }

        });
    }

    /**
     * 获得角色
     *
     * @param id 角色编号
     * @return 角色
     */
    @Override
    public SysRole getRole(Long id) {
        return getById(id);
    }

    /**
     * @return
     */
    @Override
    public List<SysRole> getRoles() {
        return list();
    }

    /**
     * @param tenantId
     * @return
     */
    @Override
    public List<SysRole> selectSysRoleMenuByTenantIdChildren(String tenantId) {
        return baseMapper.selectSysRoleMenuByTenantIdChildren(tenantId);
    }

    /**
     * @param
     * @return
     */
    @Override
    public SysRole getSystemRole() {
        return lambdaQuery().eq(SysRole::getRoleType, RoleTypeEnum.SYSTEM.getType()).one();
    }

    /**
     * @param roleId
     * @return
     */
    @Override
    public RoleRespVO selectRoleById(Long roleId) {
        SysRole SysRole = getById(roleId);
        RoleRespVO roleRespVO = BeanUtil.copyProperties(SysRole, RoleRespVO.class);
        List<Integer> checkedKeys = menuService.selectMenuListByRoleId(roleId);
        roleRespVO.setCheckedKeys(checkedKeys);
        return roleRespVO;
    }

    /**
     * @return
     */
    @Override
    public List<SysRole> selectCustomRoleAll() {
        return lambdaQuery()
                .list();
    }

    /**
     * @param userId
     * @return
     */
    @Override
    public List<SysRole> selectRolesByUserId(Long userId) {
        return baseMapper.selectRolesByUserId(userId);
    }

    /**
     * @param roleList
     * @return
     */
    @Override
    public boolean hasAnySuperAdmin(Set<Long> roleList) {
        List<SysRole> list = lambdaQuery().in(SysRole::getRoleId, roleList).list();
        if (CollectionUtil.isEmpty(list)) {
            return false;
        }
        return list.stream().anyMatch(role -> RoleCodeEnum.isSuperAdmin(role.getRoleKey()));
    }

    public void checkDuplicateRole(String roleName, Long roleId) {
        // 0. 超级管理员，不允许创建
//        if (RoleCodeEnum.isSuperAdmin(code)) {
//            throw exception(ROLE_ADMIN_CODE_ERROR, code);
//        }
        // 1. 该 name 名字被其它角色所使用
        SysRole role = lambdaQuery().eq(SysRole::getRoleName, roleName).one();
        if (role != null && !role.getRoleId().equals(roleId)) {
            //throw exception(ROLE_NAME_DUPLICATE, name);
        }
        // 2. 是否存在相同编码的角色
//        if (!StringUtils.hasText(code)) {
//            return;
//        }
        // 该 code 编码被其它角色所使用
//        role = roleMapper.selectByCode(code);
//        if (role != null && !role.getId().equals(id)) {
//            throw exception(ROLE_CODE_DUPLICATE, code);
//        }
    }
}
