package com.rexel.web.controller.system;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.PageInfo;
import com.rexel.common.annotation.Log;
import com.rexel.common.annotation.RepeatSubmit;
import com.rexel.common.core.controller.BaseController;
import com.rexel.common.core.domain.AjaxResult;
import com.rexel.common.core.domain.entity.SysRole;
import com.rexel.common.core.page.TableDataInfo;
import com.rexel.common.enums.BusinessType;
import com.rexel.common.enums.RoleTypeEnum;
import com.rexel.system.domain.dto.role.RoleCreateReqDTO;
import com.rexel.system.domain.dto.role.RolePageReqDTO;
import com.rexel.system.domain.dto.role.RoleUpdateReqDTO;
import com.rexel.system.domain.vo.role.RoleRespVO;
import com.rexel.system.service.ISysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色信息
 *
 * @author ids-admin
 */
@RestController
@RequestMapping("/system/role")
public class SysRoleController extends BaseController {

    @Autowired
    private ISysRoleService roleService;


    @PreAuthorize("@ss.hasPermi('system:role:list')")
    @GetMapping("/list")
    public TableDataInfo list(RolePageReqDTO pageReqDTO) {
        startPage();
        PageInfo<SysRole> list = PageInfo.of(roleService.pageRoles(pageReqDTO));
        List<RoleRespVO> roleRespVOS = BeanUtil.copyToList(list.getList(), RoleRespVO.class);
        return getDataTable(roleRespVOS, list.getTotal());
    }

    /**
     * 根据角色编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:role:query')")
    @GetMapping(value = "/{roleId}")
    public AjaxResult getInfo(@PathVariable Long roleId) {
        return AjaxResult.success(roleService.selectRoleById(roleId));
    }

    /**
     * 新增角色
     */
    @PreAuthorize("@ss.hasPermi('system:role:add')")
    @Log(title = "角色管理", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit(interval = 1000)
    public AjaxResult add(@Validated @RequestBody RoleCreateReqDTO role) {
        roleService.createRole(role, RoleTypeEnum.CUSTOM.getType());
        return AjaxResult.success();
    }

    /**
     * 修改保存角色
     */
    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PutMapping
    @RepeatSubmit(interval = 1000)
    public AjaxResult edit(@Validated @RequestBody RoleUpdateReqDTO role) {
        roleService.updateRole(role, false);
        return AjaxResult.success();
    }

    /**
     * 删除角色
     */
    @PreAuthorize("@ss.hasPermi('system:role:remove')")
    @Log(title = "角色管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{roleIds}")
    public AjaxResult remove(@PathVariable Long roleIds) {
        roleService.deleteRole(roleIds);
        return AjaxResult.success();
    }

    /**
     * 角色下拉框
     */
    @GetMapping("/option-select")
    public AjaxResult optionSelect() {
        List<SysRole> roles = roleService.selectCustomRoleAll();
        return AjaxResult.success(roles);
    }
}
