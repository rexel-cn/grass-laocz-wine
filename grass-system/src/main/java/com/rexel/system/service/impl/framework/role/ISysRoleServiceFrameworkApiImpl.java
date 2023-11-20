package com.rexel.system.service.impl.framework.role;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.common.core.domain.entity.SysRole;
import com.rexel.role.ISysRoleServiceFrameworkApi;
import com.rexel.system.mapper.SysRoleMapper;
import org.springframework.stereotype.Service;

/**
 * @ClassName ISysRoleServiceFrameworkApiImpl
 * @Description
 * @Author 孟开通
 * @Date 2023/1/31 11:36
 **/
@Service
public class ISysRoleServiceFrameworkApiImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleServiceFrameworkApi {
}
