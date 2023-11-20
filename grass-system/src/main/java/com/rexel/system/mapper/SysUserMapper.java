package com.rexel.system.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rexel.common.core.domain.entity.SysUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户表 数据层
 *
 * @author ids-admin
 */
@Repository
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * 登录 验证
     *
     * @param phoneNumber 手机号
     * @param userName    账户名
     * @return
     */
    SysUser selectUserByUserAndEngName(@Param("phoneNumber") String phoneNumber,
                                       @Param("userName") String userName);


    /**
     * 根据用户id查询用户  缓存使用
     *
     * @param userId
     * @return
     */
    SysUser selectUserCacheById(Long userId);

    /**
     * 查询所有用户  缓存使用
     *
     * @return
     */
    List<SysUser> selectUserList();

    @InterceptorIgnore(tenantLine = "true")
    SysUser selectUserById(Long valueOf);

    /**
     * 定时任务根据id 获取用户。排除租户id
     *
     * @param splitNoticePeople
     * @return
     */
    @InterceptorIgnore(tenantLine = "on")
    List<SysUser> timingUserByIds(Long[] splitNoticePeople);


    /**
     * 根据电话号查询租户id  登录校验使用
     *
     * @param phoneNumber
     * @return
     */
    @InterceptorIgnore(tenantLine = "true")
    String selectTenantIdByPhoneNumber(String phoneNumber);

    /**
     * 根据电话号查询用户
     *
     * @param phoneNumber
     * @return
     */
    @InterceptorIgnore(tenantLine = "true")
    SysUser selectUserByPhoneNumber(String phoneNumber);


    /**
     * 根据电话号查询用户名称
     *
     * @param phoneNumber
     * @return
     */
    @InterceptorIgnore(tenantLine = "true")
    String selectUserNameByPhoneNumber(String phoneNumber);

    @InterceptorIgnore(tenantLine = "true")
    List<SysUser> selectSysUser(@Param("tenantIds") List<String> tenantIds);
}
