package com.rexel.web.controller.system;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.PageInfo;
import com.rexel.common.annotation.Log;
import com.rexel.common.annotation.RepeatSubmit;
import com.rexel.common.constant.UserConstants;
import com.rexel.common.core.controller.BaseController;
import com.rexel.common.core.domain.AjaxResult;
import com.rexel.common.core.domain.entity.SysDept;
import com.rexel.common.core.domain.entity.SysUser;
import com.rexel.common.core.page.TableDataInfo;
import com.rexel.common.enums.BusinessType;
import com.rexel.common.utils.poi.ExcelUtil;
import com.rexel.system.domain.dto.user.*;
import com.rexel.system.domain.vo.user.UserExcelRespVO;
import com.rexel.system.domain.vo.user.UserRespVO;
import com.rexel.system.service.ISysDeptService;
import com.rexel.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用户信息
 *
 * @author ids-admin
 */
@RestController
@RequestMapping("/system/user")
public class SysUserController extends BaseController {

    @Autowired
    private ISysUserService userService;
    @Autowired
    private ISysDeptService deptService;

    @PreAuthorize("@ss.hasPermi('system:user:list')")
    @GetMapping("/list")
    public TableDataInfo list(UserPageReqDTO user) {
        startPage();
        PageInfo pageInfo = PageInfo.of(userService.selectUserList(user));

        List<UserRespVO> userRespVOVOS = BeanUtil.copyToList(pageInfo.getList(), UserRespVO.class);
        Map<String, SysDept> deptMap = deptService.getDeptMap(userRespVOVOS.stream().map(UserRespVO::getDeptId).collect(Collectors.toList()));
        userRespVOVOS.forEach(userRespVO -> {
            if (deptMap.containsKey(userRespVO.getDeptId())) {
                userRespVO.setDeptName(deptMap.get(userRespVO.getDeptId()).getDeptName());
            }
        });
        PageInfo<UserRespVO> of = PageInfo.of(userRespVOVOS);
        of.setTotal(pageInfo.getTotal());
        return getDataTable(of, "user-table");
    }

    @Log(title = "用户管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('system:user:export')")
    @GetMapping("/export")
    public AjaxResult export(UserExcelReqDTO user) {
        List<SysUser> list = userService.getUsers(user);
        List<UserExcelRespVO> userExcelRespVOS = BeanUtil.copyToList(list, UserExcelRespVO.class);

        Map<String, SysDept> deptMap = deptService.getDeptMap(userExcelRespVOS.stream().map(UserExcelRespVO::getDeptId).collect(Collectors.toList()));
        userExcelRespVOS.forEach(userRespVO -> {
            if (deptMap.containsKey(userRespVO.getDeptId())) {
                userRespVO.setDeptName(deptMap.get(userRespVO.getDeptId()).getDeptName());
            }
        });

        ExcelUtil<UserExcelRespVO> util = new ExcelUtil<>(UserExcelRespVO.class);
        return util.exportExcel(userExcelRespVOS, "用户数据");
    }
    /**
     * 新增用户
     */
    @PreAuthorize("@ss.hasPermi('system:user:add')")
    @Log(title = "新增用户", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit(interval = 1000)
    public AjaxResult add(@Validated @RequestBody UserCreateReqDTO user) {
        userService.createUser(user);
        return toAjax(true);
    }

    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @Log(title = "修改用户", businessType = BusinessType.UPDATE)
    @PutMapping
    @RepeatSubmit(interval = 1000)
    public AjaxResult edit(@Validated @RequestBody UserUpdateReqDTO user) {
        userService.updateUser(user);
        return toAjax(true);
    }

    @PreAuthorize("@ss.hasPermi('system:user:remove')")
    @Log(title = "删除用户", businessType = BusinessType.DELETE)
    @DeleteMapping("/{userIds}")
    @RepeatSubmit(interval = 1000)
    public AjaxResult remove(@PathVariable Long userIds) {
        userService.deleteUser(userIds);
        return toAjax(true);
    }

    @PreAuthorize("@ss.hasPermi('system:user:resetPwd')")
    @Log(title = "重置密码", businessType = BusinessType.UPDATE)
    @PutMapping("/resetPwd")
    @RepeatSubmit(interval = 1000)
    public AjaxResult resetPwd(@RequestBody UserUpdatePasswordReqDTO user) {
        userService.updateUserPassword(user);
        return toAjax(true);
    }

    @GetMapping("/dropDown")
    public AjaxResult userDropdownList() {
        return AjaxResult.success(userService.userDropdownList());
    }


    @PreAuthorize("@ss.hasPermi('system:user:query')")
    @GetMapping(value = "/{userId}")
    public AjaxResult getInfo(@PathVariable(value = "userId") Long userId) {
        return AjaxResult.success(BeanUtil.copyProperties(userService.getInfo(userId), UserRespVO.class));
    }

}
