package com.rexel.web.controller.system;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.rexel.common.annotation.Log;
import com.rexel.common.annotation.RepeatSubmit;
import com.rexel.common.constant.Constants;
import com.rexel.common.core.controller.BaseController;
import com.rexel.common.core.domain.AjaxResult;
import com.rexel.common.core.domain.entity.SysDept;
import com.rexel.common.core.domain.entity.SysRole;
import com.rexel.common.core.domain.entity.SysUser;
import com.rexel.common.core.domain.model.LoginUser;
import com.rexel.common.enums.BusinessType;
import com.rexel.common.exception.CustomException;
import com.rexel.common.utils.SecurityUtils;
import com.rexel.framework.web.service.TokenService;
import com.rexel.oss.exception.FileNameLengthLimitExceededException;
import com.rexel.oss.exception.InvalidExtensionException;
import com.rexel.oss.utils.AttachmentHelper;
import com.rexel.system.domain.SysPost;
import com.rexel.system.domain.dto.user.profile.UserProfileUpdatePasswordReqDTO;
import com.rexel.system.domain.dto.user.profile.UserProfileUpdateReqDTO;
import com.rexel.system.domain.vo.user.profile.UserProfileRespVO;
import com.rexel.system.service.ISysDeptService;
import com.rexel.system.service.ISysPostService;
import com.rexel.system.service.ISysRoleService;
import com.rexel.system.service.ISysUserService;
import org.apache.commons.fileupload.FileUploadBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 个人信息 业务处理
 *
 * @author ids-admin
 */
@RestController
@RequestMapping("/system/user/profile")
public class SysProfileController extends BaseController {
    @Autowired
    private ISysUserService userService;
    @Autowired
    private TokenService tokenService;

    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private ISysPostService iSysPostService;

    @Autowired
    private ISysDeptService iSysDeptService;


    @Autowired
    private AttachmentHelper attachmentHelper;

    /**
     * 个人信息
     */
    @GetMapping
    public AjaxResult profile() {
        UserProfileRespVO userProfileRespVO = new UserProfileRespVO();
        LoginUser loginUser = getLoginUser();
        SysUser user = loginUser.getUser();
        BeanUtil.copyProperties(user, userProfileRespVO);
        SysDept sysDept = iSysDeptService.selectDeptById(user.getDeptId());
        if (ObjectUtil.isNotNull(sysDept)) {
            userProfileRespVO.setDept(sysDept);
            userProfileRespVO.setDeptName(sysDept.getDeptName());
        }
        //逗号分隔的角色名称
        List<SysPost> sysPosts = iSysPostService.selectPostByIds(user.getPostIds());
        if (ObjectUtil.isNotNull(sysPosts)) {
            userProfileRespVO.setPosts(sysPosts);
            userProfileRespVO.setPostName(sysPosts.stream().map(SysPost::getPostName).collect(Collectors.joining(",")));
        }
        List<SysRole> sysRoles = roleService.selectRolesByUserId(user.getUserId());
        if (ObjectUtil.isNotNull(sysRoles)) {
            userProfileRespVO.setRoles(sysRoles);
            userProfileRespVO.setRoleName(sysRoles.stream().map(SysRole::getRoleName).collect(Collectors.joining(",")));
        }
        return AjaxResult.success(userProfileRespVO);
    }

    /**
     * 修改用户
     */
    @Log(title = "个人信息", businessType = BusinessType.UPDATE)
    @PutMapping
    @RepeatSubmit(interval = 1000)
    public AjaxResult updateProfile(@RequestBody UserProfileUpdateReqDTO user) {
        userService.updateUserProfile(user);
        return AjaxResult.success();
    }

    /**
     * 重置密码
     */
    @Log(title = "个人信息", businessType = BusinessType.UPDATE)
    @PutMapping("/updatePwd")
    @RepeatSubmit(interval = 1000)
    public AjaxResult updatePwd(String oldPassword, String newPassword) {
        userService.updateUserPassword(getUserId(), new UserProfileUpdatePasswordReqDTO(oldPassword, newPassword));
        return AjaxResult.success();
    }

    /**
     * 头像上传
     */
    @Log(title = "用户头像", businessType = BusinessType.UPDATE)
    @PostMapping("/avatar")
    @RepeatSubmit(interval = 1000)
    public AjaxResult avatar(@RequestParam("avatarfile") MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            LoginUser loginUser = getLoginUser();
            String avatar;
            try {
                avatar = attachmentHelper.upload(file, Constants.USER_AVATAR, SecurityUtils.getTenantId());
            } catch (FileUploadBase.FileSizeLimitExceededException | FileNameLengthLimitExceededException |
                     InvalidExtensionException e) {
                e.printStackTrace();
                throw new CustomException("上传图片异常，请联系管理员");
            }
            if (userService.updateUserAvatar(loginUser.getUserId(), avatar)) {
                AjaxResult ajax = AjaxResult.success();
                ajax.put("imgUrl", avatar);
                // 更新缓存用户头像
                loginUser.getUser().setAvatar(avatar);
                tokenService.setLoginUser(loginUser);
                return ajax;
            }
        }
        throw new CustomException("上传图片异常，请联系管理员");
    }
}
