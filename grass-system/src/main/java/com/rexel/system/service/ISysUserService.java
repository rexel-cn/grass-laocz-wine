package com.rexel.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rexel.common.core.domain.entity.SysUser;
import com.rexel.common.core.domain.vo.BaseNameValueVO;
import com.rexel.common.utils.CollectionUtils;
import com.rexel.system.domain.dto.user.*;
import com.rexel.system.domain.dto.user.profile.UserProfileUpdatePasswordReqDTO;
import com.rexel.system.domain.dto.user.profile.UserProfileUpdateReqDTO;
import com.rexel.system.domain.vo.NotificationVO;
import com.rexel.system.domain.vo.user.UserRespVO;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 用户 业务层
 *
 * @author ids-admin
 */
public interface ISysUserService extends IService<SysUser> {

    void refreshUserCache(Long userId);

    Long createUser(@Valid UserCreateReqDTO reqVO);

    /**
     * 修改用户
     *
     * @param reqVO 用户信息
     */
    void updateUser(@Valid UserUpdateReqDTO reqVO);

    /**
     * 更新用户的最后登陆信息
     *
     * @param userId  用户编号
     * @param loginIp 登陆 IP
     */
    void updateUserLogin(Long userId, String loginIp);

    /**
     * 修改用户个人信息
     *
     * @param reqVO 用户个人信息
     */
    void updateUserProfile(@Valid UserProfileUpdateReqDTO reqVO);

    /**
     * 修改用户个人密码
     *
     * @param userId 用户编号
     * @param reqVO  更新用户个人密码
     */
    void updateUserPassword(Long userId, @Valid UserProfileUpdatePasswordReqDTO reqVO);

    /**
     * 修改用户头像
     *
     * @param userId 用户名
     * @param avatar 头像地址
     * @return 结果
     */
    boolean updateUserAvatar(Long userId, String avatar);

    /**
     * 修改密码
     *
     * @param user 用户
     */
    void updateUserPassword(@Valid UserUpdatePasswordReqDTO user);

    /**
     * 删除用户
     *
     * @param id 用户编号
     */
    void deleteUser(Long id);

    /**
     * 通过用户名查询用户
     *
     * @param username 用户名
     * @return 用户对象信息
     */
    SysUser getUserByUsername(String username);

    /**
     * 根据条件分页查询用户列表
     *
     * @param reqVO 用户信息
     * @return 用户信息集合信息
     */
    List<SysUser> selectUserList(UserPageReqDTO reqVO);

    /**
     * 获得用户列表
     *
     * @param reqVO 列表请求
     * @return 用户列表
     */
    List<SysUser> getUsers(UserExcelReqDTO reqVO);


    /**
     * 通过用户 ID 查询用户
     *
     * @param id 用户ID
     * @return 用户对象信息
     */
    SysUser getUser(Long id);


    /**
     * 判断密码是否匹配
     *
     * @param rawPassword     未加密的密码
     * @param encodedPassword 加密后的密码
     * @return 是否匹配
     */
    boolean isPasswordMatch(String rawPassword, String encodedPassword);

    List<BaseNameValueVO> userDropdownList();

    List<SysUser> selectUserByIds(Long[] userIds);

    SysUser selectUserById(Long valueOf);

    SysUser selectUserByUserNameAndEngName(String userName);

    SysUser selectUserByPhoneAndEngName(String phone);

    UserRespVO getInfo(Long userId);

    /**
     * 定时任务获取用户id.排除租户id
     *
     * @param splitNoticePeople
     * @return
     */
    List<SysUser> timingUserByIds(Long[] splitNoticePeople);


    /**
     * 根据电话号查询租户id  登录校验使用
     *
     * @param phoneNUmber
     * @return
     */
    String selectTenantIdByPhoneNumber(String phoneNUmber);

    /**
     * 根据当前登录用户类型，设置增加的用户类型
     * * 根据当前登录用户类型，设置新增用户类型
     * * 超级管理员 可以添加 平台管理员
     * * 平台管理员 可以添加 租户管理员
     * * 租户管理员 可以添加 租户下的普通用户
     *
     * @param userType
     * @return
     */
    String setUserType(String userType);

    default Map<Long, SysUser> getUserMap(Collection<Long> ids) {
        List<SysUser> sysUsers = selectUserByIds(ids.toArray(new Long[0]));
        return CollectionUtils.convertMap(sysUsers, SysUser::getUserId);
    }
    /**
     * 校验用户们是否有效。如下情况，视为无效：
     * 1. 用户编号不存在
     * 2. 用户被禁用
     *
     * @param ids 用户编号数组
     */
    void validateUserList(Collection<Long> ids);

}
