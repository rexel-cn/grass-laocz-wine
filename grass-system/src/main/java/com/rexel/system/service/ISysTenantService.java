package com.rexel.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rexel.common.core.domain.entity.SysTenant;
import com.rexel.system.domain.ConfigurePhone;
import com.rexel.system.domain.dto.tenant.TenantCreateReqDTO;
import com.rexel.system.domain.dto.tenant.TenantPageReqDTO;
import com.rexel.system.domain.dto.tenant.TenantUpdateReqDTO;
import com.rexel.system.domain.dto.tenant.TenantUserUpdatePasswordReqDTO;
import com.rexel.system.domain.vo.tenant.TenantDetailVO;
import com.rexel.system.domain.vo.tenant.TenantRespVO;

import javax.validation.Valid;
import java.util.List;

/**
 * @version V1.0
 * @package sys
 * @title: 租户管理控制器
 * @description: 租户管理控制器
 * @author: 未知
 * @date: 2019-11-28 06:24:52
 */
public interface ISysTenantService extends IService<SysTenant> {

    /**
     * 获得租户分页
     *
     * @param pageReqDTO 分页查询
     * @return 租户分页
     */
    List<TenantRespVO> getTenantPage(@Valid TenantPageReqDTO pageReqDTO);

    /**
     * 创建租户
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Boolean createTenant(@Valid TenantCreateReqDTO createReqVO);

    /**
     * 更新租户
     *
     * @param updateReqVO 更新信息
     * @return 结果
     */
    Boolean updateTenant(@Valid TenantUpdateReqDTO updateReqVO);

    /**
     * 删除租户
     *
     * @param id 编号
     */
    Boolean deleteTenant(Long id);

    /**
     * 获得租户
     *
     * @param id 编号
     * @return 租户
     */
    TenantRespVO getTenant(Long id);

    /**
     * 修改密码
     *
     * @param user user
     */
    void updateUserPassword(TenantUserUpdatePasswordReqDTO user);

    /**
     * 检查租户类型
     */
    void checkTenantType();

    /**
     * 租户详情
     *
     * @return
     */
    TenantDetailVO getDetail();

    /**
     * 查询租户表所有租户userid
     */
    List<ConfigurePhone> selectPhoneFromTenant();

    /**
     * 查询租户表所有租户userid
     */
    String selectPhoneByTenantId(long id);
}
