package com.rexel.system.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rexel.common.core.domain.entity.SysTenant;
import com.rexel.system.domain.ConfigurePhone;
import com.rexel.system.domain.dto.tenant.TenantPageReqDTO;
import com.rexel.system.domain.vo.tenant.TenantRespVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @version V1.0
 * @package sys
 * @title: 租户管理控制器
 * @description: 租户管理控制器
 * @author: 未知
 * @date: 2019-11-28 06:24:52
 * @copyright: Inc. All rights reserved.
 */
@Mapper
public interface SysTenantMapper extends BaseMapper<SysTenant> {
    @InterceptorIgnore(tenantLine = "true")
    List<TenantRespVO> selectSysTenantVOList(TenantPageReqDTO sysTenant);


    @InterceptorIgnore(tenantLine = "true")
    List<ConfigurePhone> selectPhoneFromTenant();
    @InterceptorIgnore(tenantLine = "true")
    String selectPhoneByTenantId(@Param("id") Long id);
}
