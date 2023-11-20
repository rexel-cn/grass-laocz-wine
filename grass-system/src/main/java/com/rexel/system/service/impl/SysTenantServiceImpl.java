package com.rexel.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.common.config.ConfigurationConfig;
import com.rexel.common.constant.Constants;
import com.rexel.common.constant.UserConstants;
import com.rexel.common.core.domain.entity.SysRole;
import com.rexel.common.core.domain.entity.SysTenant;
import com.rexel.common.core.domain.entity.SysUser;
import com.rexel.common.core.domain.model.LoginUser;
import com.rexel.common.core.redis.RedisCache;
import com.rexel.common.enums.CommonStatusEnum;
import com.rexel.common.enums.RoleCodeEnum;
import com.rexel.common.enums.RoleTypeEnum;
import com.rexel.common.exception.CustomException;
import com.rexel.common.exception.ServiceException;
import com.rexel.common.utils.LoginTokenUtils;
import com.rexel.common.utils.SecurityUtils;
import com.rexel.common.utils.StringUtils;
import com.rexel.common.utils.http.HttpUtils;
import com.rexel.oss.utils.AttachmentHelper;
import com.rexel.system.domain.ConfigurePhone;
import com.rexel.system.domain.ConfigureRegister;
import com.rexel.system.domain.GrassBucketInfo;
import com.rexel.system.domain.SysTenantDetail;
import com.rexel.system.domain.dto.role.RoleCreateReqDTO;
import com.rexel.system.domain.dto.role.RoleUpdateReqDTO;
import com.rexel.system.domain.dto.tenant.TenantCreateReqDTO;
import com.rexel.system.domain.dto.tenant.TenantPageReqDTO;
import com.rexel.system.domain.dto.tenant.TenantUpdateReqDTO;
import com.rexel.system.domain.dto.tenant.TenantUserUpdatePasswordReqDTO;
import com.rexel.system.domain.dto.user.UserCreateReqDTO;
import com.rexel.system.domain.dto.user.UserUpdatePasswordReqDTO;
import com.rexel.system.domain.dto.user.UserUpdateReqDTO;
import com.rexel.system.domain.vo.tenant.TenantDetailVO;
import com.rexel.system.domain.vo.tenant.TenantRespVO;
import com.rexel.system.mapper.SysTenantMapper;
import com.rexel.system.mq.RedisSystemProducer;
import com.rexel.system.service.*;
import com.rexel.tenant.util.TenantUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @version V1.0
 * @package sys
 * @title: 租户管理控制器
 * @description: 租户管理控制器
 * @author: 未知
 * @date: 2019-11-28 06:24:52
 * @copyright: Inc. All rights reserved.
 */
@Service
@Slf4j
public class SysTenantServiceImpl extends ServiceImpl<SysTenantMapper, SysTenant> implements ISysTenantService {
    @Resource
    private ISysRoleService roleService;
    @Resource
    private PermissionService permissionService;
    @Resource
    private ISysUserService sysUserService;
    @Resource
    private ISysMenuService menuService;
    @Autowired
    private ISysDeptService sysDeptService;
    @Autowired
    private ISysPostService postService;
    @Autowired
    private IGrassAlarmHistoryService iGrassAlarmHistoryService;
    @Autowired
    private IGrassAssetService iGrassAssetService;
    @Autowired
    private IGrassAssetTypeService iGrassAssetTypeService;
    @Autowired
    private IGrassPointUserStarService iGrassPointUserStarService;
    @Autowired
    private IGrassNoticeConfigService iGrassNoticeConfigService;
    @Autowired
    private IGrassNoticeDingdingService iGrassNoticeDingdingService;
    @Autowired
    private IGrassNoticeMailboxService iGrassNoticeMailboxService;
    @Autowired
    private IGrassNoticeSmsService iGrassNoticeSmsService;
    @Autowired
    private IGrassNoticeTemplateService iGrassNoticeTemplateService;
    @Autowired
    private IGrassPointService iGrassPointService;
    @Autowired
    private IGrassPointTagInfoService iGrassPointTagInfoService;
    @Autowired
    private IGrassPointTagToPointService iGrassPointTagToPointService;
    @Autowired
    private IGrassUserMessageService iGrassUserMessageService;
    @Autowired
    private IGrassUserSmsMailboxDingdingService iGrassUserSmsMailboxDingdingService;
    @Autowired
    private IGrassRuleNoticeService iGrassRuleNoticeService;
    @Autowired
    private IGrassRulesInfoService iGrassRulesInfoService;
    @Autowired
    private IGrassWebConfService iGrassWebConfService;
    @Autowired
    private IGrassWebBoardService iGrassWebBoardService;
    @Autowired
    private ISysOperLogService iSysOperLogService;
    @Autowired
    private ISysLogininforService iSysLogininforService;
    @Autowired
    private IGrassBucketInfoService iGrassBucketInfoService;
    @Autowired
    private IGrassDeviceInfoService iGrassDeviceInfoService;
    @Autowired
    private AttachmentHelper attachmentHelper;
    @Autowired
    private RedisSystemProducer redisSystemProducer;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private ISysTenantDetailService tenantDetailService;
    @Autowired
    private ConfigurationConfig configurationConfig;

    private static boolean isSystemTenant(SysTenant tenant) {
        return Objects.equals(tenant.getTenantType(), UserConstants.TENANT_TYPE_SUPER);
    }

    /**
     * 获得租户分页
     *
     * @param pageReqDTO 分页查询
     * @return 租户分页
     */
    @Override
    public List<TenantRespVO> getTenantPage(TenantPageReqDTO pageReqDTO) {
        return baseMapper.selectSysTenantVOList(pageReqDTO);
    }

    /**
     * 获得租户
     *
     * @param id 编号
     * @return 租户
     */
    @Override
    public TenantRespVO getTenant(Long id) {
        SysTenant byId = getById(id);
        if (ObjectUtil.isNull(byId)) {
            throw new CustomException("不存在此租户");
        }
        TenantRespVO tenantRespVO = BeanUtil.copyProperties(byId, TenantRespVO.class);
        tenantRespVO.setLogo(byId.getLogo());
        TenantUtils.execute(byId.getTenantId(), () -> {
            SysUser user = sysUserService.getUser(byId.getUserId());
            if (ObjectUtil.isNull(user)) {
                throw new CustomException("租户管理员异常，请联系管理员");
            }
            tenantRespVO.setUserId(user.getUserId());
            tenantRespVO.setUserName(user.getUserName());
//            tenantRespVO.setNickName(user.getNickName());
            tenantRespVO.setPhoneNumber(user.getPhoneNumber());
            tenantRespVO.setEmail(user.getEmail());
            GrassBucketInfo grassBucketInfo = iGrassBucketInfoService.selectGrassBucketInfoByTenantId(byId.getTenantId());
            tenantRespVO.setEverySeconds(String.valueOf(grassBucketInfo.getEverySeconds()));
            List<Integer> checkedKeys = menuService.selectMenuListByRoleId(roleService.getSystemRole().getRoleId());
            tenantRespVO.setCheckedKeys(checkedKeys);

            List<SysRole> roles = roleService.getRoles();
            for (SysRole role : roles) {
                // 如果是租户管理员
                if (Objects.equals(RoleTypeEnum.SYSTEM.getType(), role.getRoleType())) {
                    tenantRespVO.setMenuCheckStrictly(role.isMenuCheckStrictly());
                    break;
                }
            }
        });
        return tenantRespVO;
    }

    /**
     * 修改密码
     *
     * @param user user
     */
    @Override
    public void updateUserPassword(TenantUserUpdatePasswordReqDTO user) {
        SysTenant byId = getById(user.getId());
        if (ObjectUtil.isNull(byId)) {
            throw new CustomException("不存在此租户");
        }
        TenantUtils.execute(byId.getTenantId(), () ->
                sysUserService.updateUserPassword(BeanUtil.copyProperties(user, UserUpdatePasswordReqDTO.class)));
    }

    /**
     * 检查租户类型
     */
    @Override
    public void checkTenantType() {
        getTenantType();
    }

    @Override
    public TenantDetailVO getDetail() {
        SysTenant tenant = lambdaQuery().eq(SysTenant::getTenantId, SecurityUtils.getTenantId()).oneOpt().get();
        return BeanUtil.copyProperties(tenant, TenantDetailVO.class);
    }

    /**
     * 创建租户
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean createTenant(TenantCreateReqDTO createReqVO) {
        //检查租户创建条件
        checkTenantCreate(createReqVO);
        SysTenant SysTenant = BeanUtil.copyProperties(createReqVO, SysTenant.class);
        String databaseName = replaceDatabaseName(createReqVO.getEngName());
        String tenantId = StringUtils.randomNumber(8);
        SysTenant.setTenantType(getTenantType());
        SysTenant.setTenantId(tenantId);
        String tenantId1 = SecurityUtils.getTenantId();
        SysTenant.setParentTenantId(tenantId1);
        //生成bucketId
        SysTenant.setBucketId(databaseName);
        save(SysTenant);
        TenantUtils.execute(SysTenant.getTenantId(), () -> {
            // 创建角色
            Long roleId = createRole(createReqVO);
            // 创建用户，并分配角色
            Long userId = createUser(roleId, createReqVO);

            insertGrassBucketInfo(createReqVO, databaseName);
            // 修改租户的管理员
            updateById(new SysTenant().setId(SysTenant.getId()).setUserId(userId));

            // 初始化企业详情（产品首页使用）
            tenantDetailService.save(SysTenantDetail.builder().tenantId(tenantId).build());

        });
        String phoneNumber = createReqVO.getPhoneNumber();
        String password = createReqVO.getPassword();
        ConfigureRegister configureRegister = new ConfigureRegister();
        configureRegister.setPhone(phoneNumber);
        configureRegister.setPassword(password);
        // 获取配置
        String url = configurationConfig.getRegisterUrl();
        // 同时传给组态
        registerConfigure(url, configureRegister);
        return true;
    }

    /**
     * 更新租户
     *
     * @param updateReqVO 更新信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateTenant(TenantUpdateReqDTO updateReqVO) {
        try {
            RedisSystemProducer.IS_SEND_MESSAGE.set(false);

            //检查租户更新条件
            SysTenant SysTenant = checkUpdateTenant(updateReqVO);
            SysTenant upSysTenant = BeanUtil.copyProperties(updateReqVO, SysTenant.class);
            updateById(upSysTenant);
            TenantUtils.execute(SysTenant.getTenantId(), () -> {
                // 更新权限
                updateTenantRoleMenu(SysTenant.getTenantId(), updateReqVO);
                // 更新用户
                updateUser(updateReqVO);
                iGrassBucketInfoService.updateGrassBucketInfo(Long.valueOf(updateReqVO.getEverySeconds()));
            });
            //是否禁用租户
            isDisableTenant(upSysTenant, SysTenant.getTenantId());
            // 发送刷新消息
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    redisSystemProducer.flushRefreshMessage();
                }
            });
        } catch (Exception e) {
            redisSystemProducer.clear();
            throw e;
        }
        return true;
    }

    public void updateTenantRoleMenu(String tenantId, TenantUpdateReqDTO updateReqVO) {
        // 获得所有角色
        List<SysRole> roles = roleService.selectSysRoleMenuByTenantIdChildren(tenantId);
        Map<String, List<SysRole>> sysRoleMap = roles.stream().collect(Collectors.groupingBy(SysRole::getTenantId, Collectors.toList()));
        sysRoleMap.forEach((tenantI, sysRoles) -> {
            for (SysRole role : sysRoles) {
                TenantUtils.execute(tenantI, () -> {
                    // 重新分配每个角色的权限
                    // 如果是租户管理员，重新分配其权限为租户套餐的权限
                    if (tenantI.equals(tenantId) && Objects.equals(RoleCodeEnum.TENANT_ADMIN.getCode(), role.getRoleKey())) {
                        RoleUpdateReqDTO roleUpdateReqDTO = BeanUtil.copyProperties(role, RoleUpdateReqDTO.class);
                        roleUpdateReqDTO.setMenuCheckStrictly(updateReqVO.getMenuCheckStrictly());
                        roleUpdateReqDTO.setMenuIds(updateReqVO.getMenuIds());
                        roleService.updateRole(roleUpdateReqDTO, true);
                        //permissionService.assignRoleMenu(role.getRoleId(), updateReqVO.getMenuIds());
                        log.info("[updateTenantRoleMenu][租户管理员({}/{}) 的权限修改为({})]", role.getRoleId(), role.getTenantId(), updateReqVO.getMenuIds());
                        return;
                    }
                    // 如果是其他角色，则去掉超过的权限
                    Set<Long> roleMenuIds = permissionService.getRoleMenuIds(role.getRoleId());
                    roleMenuIds = CollUtil.intersectionDistinct(roleMenuIds, updateReqVO.getMenuIds());
                    permissionService.assignRoleMenu(role.getRoleId(), roleMenuIds);
                    log.info("[updateTenantRoleMenu][角色({}/{}) 的权限修改为({})]", role.getRoleId(), role.getTenantId(), roleMenuIds);
                });
            }
        });
    }

    /**
     * 删除租户
     *
     * @param id 编号
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteTenant(Long id) {
        String phoneNumber = selectPhoneByTenantId(id);
        // 校验存在
        SysTenant sysTenant = checkDeleteTenant(id);
        String tenantId = sysTenant.getTenantId();
        TenantUtils.execute(tenantId, () -> {
            //在删除租户Id 的上下文来查询验证
            checkDeleteToTenantId(sysTenant);
            //删除租户关联的各种
            List<SysUser> list = sysUserService.list();
            for (SysUser sysUser : list) {
                sysUserService.deleteUser(sysUser.getUserId());
            }
            roleService.lambdaUpdate().remove();
            permissionService.removeByTenantId();
            //删除岗位
            postService.lambdaUpdate().remove();
            //部门
            sysDeptService.deleteDeptByTenantIds(Collections.singletonList(tenantId));
            //删除报警记录 grass_alarm_history
            iGrassAlarmHistoryService.deleteByTenantId(tenantId);
            //删除资产相关 grass_asset_point,grass_asset，grass_asset_type，grass_point_user_star
            iGrassAssetService.deleteByTenantId(tenantId);

            iGrassAssetTypeService.deleteByTenantId(tenantId);

            iGrassPointUserStarService.deleteByTenantId(tenantId);

            //删除物联设备 grass_device_info
            iGrassDeviceInfoService.deleteByTenantId(tenantId);

            //删除通知配置 grass_notice_config ,grass_notice_dingding,grass_notice_mailbox,grass_notice_sms,
            iGrassNoticeConfigService.deleteByTenantId(tenantId);
            iGrassNoticeDingdingService.deleteByTenantId(tenantId);
            iGrassNoticeMailboxService.deleteByTenantId(tenantId);
            iGrassNoticeSmsService.deleteByTenantId(tenantId);
            // grass_notice_template,grass_notice_mode,grass_notice_scope
            iGrassNoticeTemplateService.deleteByTenantId(tenantId);

            //删除测点 grass_point_info
            iGrassPointService.deleteByTenantId(tenantId);

            //删除测点标签 grass_point_tag_info，grass_point_tag_point
            iGrassPointTagInfoService.deleteByTenantId(tenantId);
            iGrassPointTagToPointService.deleteByTenantId(tenantId);
            //删除用户站内信   grass_user_message
            iGrassUserMessageService.deleteByTenantId(tenantId);
            //删除邮件，钉钉，短信发送记录  grass_user_sms_mailbox_dingding
            iGrassUserSmsMailboxDingdingService.deleteByTenantId(tenantId);
            //删除报警规则 grass_rule_notice，grass_rules_info
            iGrassRuleNoticeService.deleteByTenantId(tenantId);
            iGrassRulesInfoService.deleteByTenantId(tenantId);

            //删除组态地址 grass_web_conf
            iGrassWebConfService.deleteByTenantId(tenantId);
            //删除看板地址grass_web_board
            iGrassWebBoardService.deleteByTenantId(tenantId);

            //删除操作日志 sys_oper_log
            iSysOperLogService.deleteByTenantId(tenantId);

            //删除登录日志 sys_logininfor
            iSysLogininforService.deleteByTenantId(tenantId);
            //删除定时调度日志 sys_job_log
        });

        //事务成功后删除
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                //删除租户文件
                try {
                    attachmentHelper.delete(tenantId);
                } catch (Exception e) {
                    log.error("删除租户文件失败", e);
                }
                //删除存储信息 grass_bucket_info
                iGrassBucketInfoService.deleteGrassBucketInfoByTenantId(tenantId);

                //删除租户缓存
                LoginTokenUtils.getTokenByUserId(sysTenant.getUserId()).forEach(token ->
                        redisCache.deleteObject(Constants.LOGIN_TOKEN_KEY + token));
            }
        });

        deleteConfiguration(phoneNumber);
        removeById(id);
        return true;
    }

    private SysTenant checkDeleteTenant(Long id) {
        SysTenant tenant = getById(id);
        if (tenant == null) {
            throw new ServiceException("租户不存在");
        }
        // 内置租户，不允许删除
        if (isSystemTenant(tenant)) {
            throw new ServiceException("内置租户不能修改");
        }
        String parentTenantId = SecurityUtils.getTenantId();
        if (!parentTenantId.equals(tenant.getParentTenantId())) {
            throw new ServiceException("无权删除");
        }
        return tenant;
    }

    private void checkDeleteToTenantId(SysTenant tenant) {
        //验证当前租户下有无用户
        List<SysUser> list = sysUserService.lambdaQuery()
                .eq(SysUser::getDelFlag, 0)
                .ne(SysUser::getUserId, tenant.getUserId())
                .list();
        if (CollectionUtil.isNotEmpty(list)) {
            throw new ServiceException("当前租户下存在用户不允许删除");
        }
        //验证当前租户下无物联设备
        Integer count = iGrassDeviceInfoService.lambdaQuery().count();
        if (count != null && count > 0) {
            throw new ServiceException("当前租户下存在物联设备不允许删除");
        }
    }

    private SysTenant checkUpdateTenant(TenantUpdateReqDTO updateReqVO) {
        SysTenant tenant = getById(updateReqVO.getId());
        if (tenant == null) {
            throw new ServiceException("租户不存在");
        }
        // 内置租户，不允许删除
        if (isSystemTenant(tenant)) {
            throw new ServiceException("内置租户不能修改");
        }
        //只能修改自己的子租户
        if (!SecurityUtils.getTenantId().equals(tenant.getParentTenantId())) {
            throw new ServiceException("无权修改");
        }
        checkEngNameUnique(updateReqVO.getId(), updateReqVO.getEngName());
        return tenant;
    }

    private void insertGrassBucketInfo(TenantCreateReqDTO createReqVO, String databaseName) {
        //新增存储信息
        GrassBucketInfo grassBucketInfo = new GrassBucketInfo();
        grassBucketInfo.setBucketId(databaseName);
        grassBucketInfo.setEverySeconds(Long.parseLong(createReqVO.getEverySeconds().trim()));
        grassBucketInfo.setBucketName(createReqVO.getTenantName());
        iGrassBucketInfoService.insertGrassBucketInfo(grassBucketInfo);
    }

    private void checkTenantCreate(TenantCreateReqDTO createReqVO) {
        String tenantType = getTenantType();
        if (tenantType.equals(UserConstants.TENANT_TYPE_PLATFORM_MANAGE)) {
            List<SysTenant> list = this.lambdaQuery().eq(SysTenant::getTenantType, UserConstants.TENANT_TYPE_PLATFORM_MANAGE).list();
            if (CollectionUtil.isNotEmpty(list) && list.size() >= 1) {
                throw new CustomException("平台管理员只能创建一个");
            }
        }
        //检查租户英文名是否重复
        checkEngNameUnique(null, createReqVO.getEngName());
    }

    private Long createRole(TenantCreateReqDTO createReqVO) {
        // 创建角色
        RoleCreateReqDTO reqVO = new RoleCreateReqDTO();
        reqVO.setRoleName("系统管理员");
        reqVO.setRoleSort(0);
        reqVO.setRemark("系统自动生成");
        reqVO.setMenuCheckStrictly(createReqVO.getMenuCheckStrictly());
        reqVO.setMenuIds(createReqVO.getMenuIds());
        reqVO.setRoleKey(RoleCodeEnum.TENANT_ADMIN.getCode());
        return roleService.createRole(reqVO, RoleTypeEnum.SYSTEM.getType());
    }

    private Long createUser(Long roleId, TenantCreateReqDTO createReqVO) {
        UserCreateReqDTO userCreateReqDTO = BeanUtil.copyProperties(createReqVO, UserCreateReqDTO.class);
        userCreateReqDTO.setRoleIds(new HashSet<Long>() {{
            add(roleId);
        }});

        userCreateReqDTO.setUserType(sysUserService.setUserType(SecurityUtils.getLoginUser().getUser().getUserType()));
        // 创建用户
        return sysUserService.createUser(userCreateReqDTO);
    }

    private void updateUser(TenantUpdateReqDTO updateReqVO) {
        UserUpdateReqDTO userUpdateReqDTOVO = BeanUtil.copyProperties(updateReqVO, UserUpdateReqDTO.class);
        userUpdateReqDTOVO.setRoleIds(new HashSet<Long>() {{
            add(roleService.getSystemRole().getRoleId());
        }});
        //更新租户状态，不更新用户状态， 租户状态和用户状态字段一样
        userUpdateReqDTOVO.setStatus(null);
        sysUserService.updateUser(userUpdateReqDTOVO);
    }

    private String getTenantType() {
        SysTenant sysTenant = this.lambdaQuery().eq(SysTenant::getTenantId, SecurityUtils.getTenantId()).one();
        if (sysTenant == null) {
            throw new ServiceException("禁止创建");
        }
        String tenantType = sysTenant.getTenantType();
        if (tenantType.equals(UserConstants.TENANT_TYPE_SUPER)) {
            return UserConstants.TENANT_TYPE_PLATFORM_MANAGE;
        } else if (tenantType.equals(UserConstants.TENANT_TYPE_PLATFORM_MANAGE)) {
            return UserConstants.TENANT_TYPE_NORMAL_MANAGER;
        } else {
            throw new ServiceException("当前账号没有权限创建或删除租户");
        }
    }

    public void checkEngNameUnique(Long id, String engName) {
        if (StrUtil.isBlank(engName)) {
            return;
        }
        SysTenant sysTenant = this.lambdaQuery().eq(SysTenant::getEngName, engName).one();
        if (sysTenant == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的用户
        if (!sysTenant.getId().equals(id)) {
            throw new ServiceException("企业英文名已存在:" + engName);
        }
    }

    private String replaceDatabaseName(String engName) {
        return engName.replace("-", "").toLowerCase();
    }

    /**
     * 租户是否禁用  如果禁用，则下线所有此租户下用户
     */
    private void isDisableTenant(SysTenant sysTenant, String tenantId) {
        if (CommonStatusEnum.DISABLE.getStatus().equals(sysTenant.getStatus())) {
            List<LoginUser> loginUserList = LoginTokenUtils.getLoginUserList();
            Map<String, List<LoginUser>> userMap = loginUserList.stream().collect(Collectors.groupingBy(LoginUser::getTenantId));
            if (userMap.containsKey(tenantId)) {
                List<LoginUser> list = userMap.get(tenantId);
                if (CollectionUtil.isNotEmpty(list)) {
                    list.forEach(loginUser -> LoginTokenUtils.offline(loginUser.getToken()));
                }
            }
        }
    }

    /**
     * 将账号和密码传给组态接口(Post)
     */
    public void registerConfigure(String url, Object obj) {
        //创建httpClient连接
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //调用接口传递参数
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(new StringEntity(JSON.toJSONString(obj),
                ContentType.create("application/json", "UTF-8")));
        try {
            CloseableHttpResponse execute = httpClient.execute(httpPost);
            HttpEntity entity = execute.getEntity();
            String str = EntityUtils.toString(entity);
            log.info("账号和密码传给组态接口调用成功{}", str);
        } catch (IOException e) {
            log.info("账号和密码传给组态接口调用失败");
            e.printStackTrace();
        }
    }

    @Override
    public List<ConfigurePhone> selectPhoneFromTenant() {
        return baseMapper.selectPhoneFromTenant();
    }

    @Override
    public String selectPhoneByTenantId(long id) {
        return baseMapper.selectPhoneByTenantId(id);
    }

    private void deleteConfiguration(String phoneNumber) {
        String phone = "phone=" + phoneNumber;
        //读取配置
        String url = configurationConfig.getDeleteUrl();
        //同时传给组态
        HttpUtils.sendGet(url, phone, Constants.UTF8);
    }
}
