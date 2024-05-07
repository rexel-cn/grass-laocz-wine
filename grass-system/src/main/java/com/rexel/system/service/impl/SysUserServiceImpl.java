package com.rexel.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.common.constant.Constants;
import com.rexel.common.constant.UserConstants;
import com.rexel.common.core.domain.entity.SysUser;
import com.rexel.common.core.domain.model.LoginUser;
import com.rexel.common.core.domain.vo.BaseNameValueVO;
import com.rexel.common.core.redis.RedisCache;
import com.rexel.common.enums.CommonStatusEnum;
import com.rexel.common.enums.UserStatus;
import com.rexel.common.exception.CustomException;
import com.rexel.common.exception.ServiceException;
import com.rexel.common.utils.CollectionUtils;
import com.rexel.common.utils.LoginTokenUtils;
import com.rexel.common.utils.SecurityUtils;
import com.rexel.framework.web.service.SysPermissionService;
import com.rexel.system.domain.dto.user.*;
import com.rexel.system.domain.dto.user.profile.UserProfileUpdatePasswordReqDTO;
import com.rexel.system.domain.dto.user.profile.UserProfileUpdateReqDTO;
import com.rexel.system.domain.vo.user.UserRespVO;
import com.rexel.system.mapper.SysUserMapper;
import com.rexel.system.mq.RedisSystemProducer;
import com.rexel.system.service.*;
import com.rexel.tenant.context.TenantContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName ISysUserService1Impl
 * @Description
 * @Author 孟开通
 * @Date 2022/12/4 17:29
 **/
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private ISysUserRoleService sysUserRoleService;

    @Autowired
    private ISysUserPostService sysUserPostService;

    @Autowired
    private ISysUserPostService userPostService;
    @Autowired
    private RedisCache redisCache;

    @Autowired
    private SysPermissionService permission;


    @Autowired
    private RedisSystemProducer redisSystemProducer;


    /**
     *
     */
    @Override
    public void refreshUserCache(Long userId) {
        List<LoginUser> loginUserList = LoginTokenUtils.getLoginUserList();
        Map<Long, List<LoginUser>> userMap = loginUserList.stream().collect(Collectors.groupingBy(LoginUser::getUserId));
        if (ObjectUtil.isNotEmpty(userId)) {
            if (userMap.containsKey(userId)) {
                SysUser sysUser = baseMapper.selectUserCacheById(userId);
                Set<String> menuPermission = permission.getMenuPermission(sysUser);
                sysUser.setPassword(null);
                LoginUser loginUser = new LoginUser(sysUser, menuPermission);
                userMap.get(sysUser.getUserId()).forEach(user -> {
                    getLoginUser(loginUser, user);
                    redisCache.setCacheObject(Constants.LOGIN_TOKEN_KEY + user.getToken(), loginUser);
                });
            }
        } else {
            List<SysUser> sysUsers = baseMapper.selectUserList();
            for (SysUser sysUser : sysUsers) {
                if (userMap.containsKey(sysUser.getUserId())) {
                    Set<String> menuPermission = permission.getMenuPermission(sysUser);
                    sysUser.setPassword(null);
                    LoginUser loginUser = new LoginUser(sysUser, menuPermission);
                    userMap.get(sysUser.getUserId()).forEach(user -> {
                        getLoginUser(loginUser, user);
                        redisCache.setCacheObject(Constants.LOGIN_TOKEN_KEY + user.getToken(), loginUser);
                    });
                }
            }
        }
    }

    private LoginUser getLoginUser(LoginUser loginUser, LoginUser user) {
        loginUser.setBrowser(user.getBrowser());
        loginUser.setIpaddr(user.getIpaddr());
        loginUser.setExpireTime(user.getExpireTime());
        loginUser.setLoginLocation(user.getLoginLocation());
        loginUser.setOs(user.getOs());
        loginUser.setToken(user.getToken());
        loginUser.setLoginTime(user.getLoginTime());
        return loginUser;
    }

    /**
     * @param reqVO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createUser(UserCreateReqDTO reqVO) {
        //
        checkCreateOrUpdate(null, reqVO.getUserName(), reqVO.getPhoneNumber(), reqVO.getEmail(),
                reqVO.getDeptId(), reqVO.getPostIds());

        //设置用户类型
        reqVO.setUserType(setUserType(SecurityUtils.getLoginUser().getUser().getUserType()));

        SysUser user = BeanUtil.copyProperties(reqVO, SysUser.class);
        user.setStatus(UserStatus.OK.getCode()); // 默认开启
        user.setPassword(SecurityUtils.encryptPassword(reqVO.getPassword())); // 加密密码
        save(user);
        if (CollectionUtil.isNotEmpty(reqVO.getPostIds())) {
            // 更新用户岗位关联
            permissionService.assignUserPost(user.getUserId(), reqVO.getPostIds());
        }
        // 更新用户与角色管理
        permissionService.assignUserRole(user.getUserId(), reqVO.getRoleIds());
        return user.getUserId();
    }

    /**
     * 修改用户
     *
     * @param reqVO 用户信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(UserUpdateReqDTO reqVO) {

        //checkStatus(reqVO.getUserId());

        // 校验正确性
        checkCreateOrUpdate(reqVO.getUserId(), reqVO.getUserName(), reqVO.getPhoneNumber(), reqVO.getEmail(),
                reqVO.getDeptId(), reqVO.getPostIds());
        // 更新用户
        SysUser updateObj = BeanUtil.copyProperties(reqVO, SysUser.class);
        updateById(updateObj);
        if (ObjectUtil.isNotNull(reqVO.getPostIds())) {
            // 更新岗位
            permissionService.assignUserPost(reqVO.getUserId(), reqVO.getPostIds());
        }
        // 更新角色
        permissionService.assignUserRole(reqVO.getUserId(), reqVO.getRoleIds());

        //是否禁用用户
        isDisableUser(updateObj);


        //如果租户调用，设置为false,否则设置为true   设置为false 暂时加入集合，等到事务提交后，再发送消息
        if (RedisSystemProducer.IS_SEND_MESSAGE.get() == null || !RedisSystemProducer.IS_SEND_MESSAGE.get()) {
            redisSystemProducer.sendUserRefreshMessage(null, TenantContextHolder.getTenantId());
            return;
        }
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                redisSystemProducer.sendUserRefreshMessage(reqVO.getUserId(), null);
            }
        });
    }

    private void checkStatus(Long userId) {
        if (userId.equals(SecurityUtils.getUserId())) {
            throw new ServiceException("不能修改自己的状态");
        }
    }

    /**
     * 更新用户的最后登陆信息
     *
     * @param userId  用户编号
     * @param loginIp 登陆 IP
     */
    @Override
    public void updateUserLogin(Long userId, String loginIp) {
        // 校验用户存在
        checkUserExists(userId);
        updateById(new SysUser().setUserId(userId).setLoginIp(loginIp).setLoginDate(new Date()));
    }

    /**
     * 修改用户个人信息
     *
     * @param reqVO 用户个人信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserProfile(UserProfileUpdateReqDTO reqVO) {
        // 校验用户存在
        checkUserExists(reqVO.getUserId());
        // 校验正确性
        checkCreateOrUpdate(reqVO.getUserId(), null, null, reqVO.getEmail(),
                null, null);
        updateById(BeanUtil.copyProperties(reqVO, SysUser.class));

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                //事务提交后执行
                redisSystemProducer.sendUserRefreshMessage(reqVO.getUserId(), null);
            }
        });
    }

    /**
     * 修改用户个人密码
     *
     * @param userId 用户编号
     * @param reqVO  更新用户个人密码
     */
    @Override
    public void updateUserPassword(Long userId, UserProfileUpdatePasswordReqDTO reqVO) {
        // 校验用户存在
        checkUserExists(userId);
        // 校验旧密码密码
        checkOldPassword(userId, reqVO.getOldPassword());
        // 执行更新
        SysUser updateObj = new SysUser().setUserId(userId);
        updateObj.setPassword(SecurityUtils.encryptPassword(reqVO.getNewPassword())); // 加密密码
        updateById(updateObj);
        redisCache.deleteObject(Constants.LOGIN_TOKEN_KEY + SecurityUtils.getLoginUser().getToken());
    }

    /**
     * 修改用户头像
     *
     * @param userId 用户名
     * @param avatar 头像地址
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUserAvatar(Long userId, String avatar) {
        // 校验用户存在
        checkUserExists(userId);
        boolean update = lambdaUpdate().eq(SysUser::getUserId, userId).set(SysUser::getAvatar, avatar).update();
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                //事务提交后执行
                redisSystemProducer.sendUserRefreshMessage(userId, null);
            }
        });
        return update;

    }

    /**
     * 修改密码
     *
     * @param user
     */
    @Override
    public void updateUserPassword(UserUpdatePasswordReqDTO user) {
        // 校验用户存在
        checkUserExists(user.getUserId());
        // 更新密码
        SysUser updateObj = new SysUser();
        updateObj.setUserId(user.getUserId());
        updateObj.setPassword(SecurityUtils.encryptPassword(user.getPassword())); // 加密密码
        updateById(updateObj);
        //redisCache.deleteObject(Constants.LOGIN_TOKEN_KEY + SecurityUtils.getLoginUser().getToken());
        //redisCache.deleteObject(Constants.LOGIN_TOKEN_KEY + SecurityUtils.getLoginUser().getToken());
    }

    /**
     * 删除用户
     *
     * @param id 用户编号
     */
    @Override
    public void deleteUser(Long id) {
        //自己不能删除自己
        if (SecurityUtils.getUserId().equals(id)) {
            throw new ServiceException("不允许删除当前用户");
        }
        // 校验用户存在
        checkUserExists(id);
        // 删除用户
        removeById(id);
        // 删除用户关联数据
        permissionService.processUserDeleted(id);
        //删除用户与岗位关联表
        userPostService.deleteByUserId(id);

        //强制下线
        LoginTokenUtils.getTokenByUserId(id).forEach(token -> redisCache.deleteObject(Constants.LOGIN_TOKEN_KEY + token));
    }

    /**
     * 通过用户名查询用户
     *
     * @param username 用户名
     * @return 用户对象信息
     */
    @Override
    public SysUser getUserByUsername(String username) {
        return lambdaQuery().eq(SysUser::getUserName, username).one();
    }

    /**
     * 根据条件分页查询用户列表
     *
     * @param reqVO@return 用户信息集合信息
     */
    @Override
    public List<SysUser> selectUserList(UserPageReqDTO reqVO) {
        return lambdaQuery()
                .like(StrUtil.isNotBlank(reqVO.getUserName()), SysUser::getUserName, reqVO.getUserName())
                .like(StrUtil.isNotBlank(reqVO.getPhoneNumber()), SysUser::getPhoneNumber, reqVO.getPhoneNumber())
                .eq(reqVO.getStatus() != null, SysUser::getStatus, reqVO.getStatus())
                .eq(StrUtil.isNotEmpty(reqVO.getDeptId()), SysUser::getDeptId, reqVO.getDeptId())
                .or()
                .inSql(StrUtil.isNotEmpty(reqVO.getDeptId()) && !reqVO.getDeptId().equals("0"),
                        SysUser::getDeptId, "SELECT dept_id FROM sys_dept  WHERE find_in_set(" + reqVO.getDeptId() + ",ancestors)")
                .list();
    }

    /**
     * 获得用户列表
     *
     * @param reqVO 列表请求
     * @return 用户列表
     */
    @Override
    public List<SysUser> getUsers(UserExcelReqDTO reqVO) {
        return lambdaQuery()
                .like(StrUtil.isNotBlank(reqVO.getUserName()), SysUser::getUserName, reqVO.getUserName())
                .like(StrUtil.isNotBlank(reqVO.getPhoneNumber()), SysUser::getPhoneNumber, reqVO.getPhoneNumber())
                .eq(reqVO.getStatus() != null, SysUser::getStatus, reqVO.getStatus())
                .list();
    }

    /**
     * 通过用户 ID 查询用户
     *
     * @param id 用户ID
     * @return 用户对象信息
     */
    @Override
    public SysUser getUser(Long id) {
        return getById(id);
    }


    private void checkCreateOrUpdate(Long userId, String username, String mobile, String email,
                                     String deptId, Set<Long> postIds) {
        // 校验用户存在
        checkUserExists(userId);
        // 校验手机号唯一
        checkMobileUnique(userId, mobile);
        // 校验邮箱唯一
        checkEmailUnique(userId, email);
        // 校验部门处于开启状态
        //deptService.validDepts(CollectionUtils.singleton(deptId));
        // 校验岗位处于开启状态
        //postService.validPosts(postIds);
    }

    public void checkUserExists(Long id) {
        if (id == null) {
            return;
        }
        SysUser user = getById(id);
        if (user == null) {
            throw new ServiceException("用户不存在");
        }
    }


    public void checkMobileUnique(Long userId, String mobile) {
        if (StrUtil.isBlank(mobile)) {
            return;
        }
        SysUser user = lambdaQuery().eq(SysUser::getPhoneNumber, mobile).one();
        if (user == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的用户
        if (!user.getUserId().equals(userId)) {
            throw new ServiceException("手机号已经存在");
        }
    }

    public void checkEmailUnique(Long userId, String email) {
        if (StrUtil.isBlank(email)) {
            return;
        }
        SysUser user = lambdaQuery().eq(SysUser::getEmail, email).one();
        if (user == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的用户
        if (!user.getUserId().equals(userId)) {
            throw new ServiceException("邮箱已经存在");
        }
    }

    public void checkOldPassword(Long id, String oldPassword) {
        SysUser user = getById(id);
        if (user == null) {
            throw new ServiceException("用户不存在");
        }
        if (!isPasswordMatch(oldPassword, user.getPassword())) {
            throw new ServiceException("用户密码校验失败");
        }
    }

    @Override
    public boolean isPasswordMatch(String rawPassword, String encodedPassword) {
        return SecurityUtils.matchesPassword(rawPassword, encodedPassword);
    }

    /**
     * @return
     */
    @Override
    public List<BaseNameValueVO> userDropdownList() {
        List<BaseNameValueVO> nameValueVOList = new ArrayList<>();
        List<SysUser> userList = list();
        userList.forEach(b -> {
            BaseNameValueVO nameValueVO = new BaseNameValueVO();
            nameValueVO.setName(b.getUserName());
            nameValueVO.setValue(b.getUserId());
            nameValueVOList.add(nameValueVO);
        });
        return nameValueVOList;
    }

    /**
     * @param userIds
     * @return
     */
    @Override
    public List<SysUser> selectUserByIds(Long[] userIds) {
        return lambdaQuery().in(SysUser::getUserId, Arrays.asList(userIds)).list();
    }

    /**
     * @param valueOf
     * @return
     */
    @Override
    public SysUser selectUserById(Long valueOf) {
        return baseMapper.selectUserById(valueOf);
    }

    /**
     * @param value
     * @return
     */
    @Override
    public SysUser selectUserByUserNameAndEngName(String value) {
        return baseMapper.selectUserByUserAndEngName(null, value);
    }

    /**
     * @param phoneNumber
     * @return
     */
    @Override
    public SysUser selectUserByPhoneAndEngName(String phoneNumber) {
        return baseMapper.selectUserByUserAndEngName(phoneNumber, null);
    }

    /**
     * @param userId
     * @return
     */
    /**
     * @param userId
     * @return
     */
    @Override
    public UserRespVO getInfo(Long userId) {
        UserRespVO userRespVO = new UserRespVO();
        SysUser byId = getById(userId);
        BeanUtil.copyProperties(byId, userRespVO);
        userRespVO.setPostIds(sysUserPostService.selectPostIdsByUserId(userId));
        userRespVO.setRoleIds(sysUserRoleService.selectRoleIdsByUserId(userId));
        return userRespVO;
    }

    @Override
    public List<SysUser> timingUserByIds(Long[] splitNoticePeople) {
        return baseMapper.timingUserByIds(splitNoticePeople);
    }


    private void isDisableUser(SysUser sysUser) {
        if (UserStatus.DISABLE.getCode().equals(sysUser.getStatus())) {
            List<LoginUser> loginUserList = LoginTokenUtils.getLoginUserList();
            Map<Long, List<LoginUser>> userMap = loginUserList.stream().collect(Collectors.groupingBy(LoginUser::getUserId));
            if (userMap.containsKey(sysUser.getUserId())) {
                List<LoginUser> list = userMap.get(sysUser.getUserId());
                if (CollectionUtil.isNotEmpty(list)) {
                    list.forEach(loginUser -> LoginTokenUtils.offline(loginUser.getToken()));
                }
            }
        }
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
     * * 根据当前登录用户类型，设置新增用户类型
     * * 超级管理员 可以添加 平台管理员
     * * 平台管理员 可以添加 租户管理员
     * * 租户管理员 可以添加 租户下的普通用户
     *
     * @param userType 当前登录用户类型
     * @return 构建的用户类型
     */
    @Override
    public String setUserType(String userType) {
        if (userType.equals(UserConstants.USER_TYPE_SUPER)) {
            return UserConstants.USER_TYPE_PLATFORM_MANAGER;
        }
        if (userType.equals(UserConstants.USER_TYPE_PLATFORM_MANAGER)) {
            return UserConstants.USER_TYPE_TENANT_MANAGER;
        }
        if (userType.equals(UserConstants.USER_TYPE_TENANT_MANAGER)) {
            return UserConstants.USER_TYPE_TENANT_USER;
        }
        return null;
    }

    /**
     * 校验用户们是否有效。如下情况，视为无效：
     * 1. 用户编号不存在
     * 2. 用户被禁用
     *
     * @param ids 用户编号数组
     */
    @Override
    public void validateUserList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return;
        }
        // 获得岗位信息
        List<SysUser> users = selectUserByIds(ids.toArray(new Long[0]));
        Map<Long, SysUser> userMap = CollectionUtils.convertMap(users, SysUser::getUserId);
        // 校验
        ids.forEach(id -> {
            SysUser user = userMap.get(id);
            if (user == null) {
                throw new  CustomException("用户不存在");
            }
            if (!CommonStatusEnum.ENABLE.getStatus().equals(user.getStatus())) {
                throw new  CustomException("名字为【{}】的用户已被禁用", user.getUserName());
            }
        });
    }
}
