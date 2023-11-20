package com.rexel.system.service.impl.framework.user;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.common.core.domain.entity.SysTenant;
import com.rexel.system.mapper.SysTenantMapper;
import com.rexel.user.ISysTenantServiceFrameworkApi;
import org.springframework.stereotype.Service;

/**
 * @ClassName ISysTenantServiceFrameworkApiImpl
 * @Description
 * @Author 孟开通
 * @Date 2023/1/31 11:30
 **/
@Service
public class ISysTenantServiceFrameworkApiImpl extends ServiceImpl<SysTenantMapper, SysTenant> implements ISysTenantServiceFrameworkApi {
}
