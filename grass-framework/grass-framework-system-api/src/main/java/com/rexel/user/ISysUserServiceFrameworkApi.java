package com.rexel.user;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rexel.common.core.domain.entity.SysUser;

/**
 * 用户 业务层
 *
 * @author ids-admin
 */
public interface ISysUserServiceFrameworkApi extends IService<SysUser> {

    /**
     * 更新用户的最后登陆信息
     *
     * @param userId  用户编号
     * @param loginIp 登陆 IP
     */
    void updateUserLogin(Long userId, String loginIp);

    SysUser selectUserByUserNameAndEngName(String userName);

    SysUser selectUserByPhoneAndEngName(String phone);


    SysUser selectUserById(Long valueOf);


    String selectTenantIdByPhoneNumber(String phoneNumber);


    SysUser selectUserByPhoneNumber(String phoneNumber);


    String selectUserNameByPhoneNumber(String phoneNumber);
}
