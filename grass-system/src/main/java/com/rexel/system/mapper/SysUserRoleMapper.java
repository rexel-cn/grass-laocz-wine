package com.rexel.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rexel.system.domain.SysUserRole;
import org.springframework.stereotype.Repository;


/**
 * 用户与角色关联表 数据层
 *
 * @author ids-admin
 */
@Repository
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {
}
