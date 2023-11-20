package com.rexel.system.service.impl.framework.user;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.common.core.domain.entity.SysUser;
import com.rexel.system.mapper.SysUserMapper;
import com.rexel.system.service.ISysUserService;
import com.rexel.user.ISysUserServiceFrameworkApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName ISysUserServiceFrameworkApiImpl
 * @Description
 * @Author 孟开通
 * @Date 2023/1/31 11:33
 **/
@Service
public class ISysUserServiceFrameworkApiImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserServiceFrameworkApi {


    @Autowired
    private ISysUserService sysUserService;

    /**
     * 更新用户的最后登陆信息
     *
     * @param userId  用户编号
     * @param loginIp 登陆 IP
     */
    @Override
    public void updateUserLogin(Long userId, String loginIp) {
        sysUserService.updateUserLogin(userId, loginIp);
    }

    /**
     * @param userName
     * @return
     */
    @Override
    public SysUser selectUserByUserNameAndEngName(String userName) {
        return sysUserService.selectUserByUserNameAndEngName(userName);
    }

    /**
     * @param phone
     * @return
     */
    @Override
    public SysUser selectUserByPhoneAndEngName(String phone) {
        return sysUserService.selectUserByPhoneAndEngName(phone);
    }

    /**
     * @param valueOf
     * @return
     */
    @Override
    public SysUser selectUserById(Long valueOf) {
        return sysUserService.selectUserById(valueOf);
    }


    /**
     * @param phoneNumber
     * @return
     */
    @Override
    public String selectTenantIdByPhoneNumber(String phoneNumber) {
        return baseMapper.selectTenantIdByPhoneNumber(phoneNumber);
    }


    /**
     * @param phoneNumber
     * @return
     */
    public SysUser selectUserByPhoneNumber(String phoneNumber) {
        return baseMapper.selectUserByPhoneNumber(phoneNumber);
    }


    /**
     * @param phoneNumber
     * @return
     */
    public String selectUserNameByPhoneNumber(String phoneNumber) {
        return baseMapper.selectUserNameByPhoneNumber(phoneNumber);
    }
}
