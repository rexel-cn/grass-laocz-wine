package com.rexel.web.controller.system;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rexel.common.annotation.Log;
import com.rexel.common.annotation.RepeatSubmit;
import com.rexel.common.core.controller.BaseController;
import com.rexel.common.core.domain.AjaxResult;
import com.rexel.common.core.domain.entity.SysTenant;
import com.rexel.common.core.page.TableDataInfo;
import com.rexel.common.enums.BusinessType;
import com.rexel.system.domain.dto.tenant.TenantCreateReqDTO;
import com.rexel.system.domain.dto.tenant.TenantPageReqDTO;
import com.rexel.system.domain.dto.tenant.TenantUpdateReqDTO;
import com.rexel.system.domain.dto.tenant.TenantUserUpdatePasswordReqDTO;
import com.rexel.system.service.ISysTenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 参数配置 信息操作处理
 *
 * @author ids-admin
 */
@RestController
@RequestMapping("/system/tenant")
public class SysTenantController extends BaseController {
    @Autowired
    private ISysTenantService sysTenantService;

    @PreAuthorize("@ss.hasPermi('system:tenant:list')")
    @GetMapping("/list")
    public TableDataInfo list(TenantPageReqDTO pageReqDTO) {
        startPage();
        pageReqDTO.setTenantId(getTenantId());
        return getDataTable(sysTenantService.getTenantPage(pageReqDTO), "tenant-table");
    }

    @GetMapping("/dropDown")
    public AjaxResult dropDown() {
        QueryWrapper<SysTenant> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");
        //排除   00000000 管理员
        queryWrapper.ne("tenant_id", "00000000");
        List<SysTenant> list = sysTenantService.list(queryWrapper);
        return AjaxResult.success(list);
    }

    /**
     * 获取租户管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:tenant:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(sysTenantService.getTenant(id));
    }

    /**
     * 租户详情
     * @return
     */
    @GetMapping("/detail")
    public AjaxResult getInfo() {
        return AjaxResult.success(sysTenantService.getDetail());
    }
    /**
     * 检查租户管理
     */
    @PostMapping(value = "/checkAuth")
    public AjaxResult checkAuth() {
        sysTenantService.checkTenantType();
        return AjaxResult.success();
    }

    /**
     * 新增租户管理
     */
    @PreAuthorize("@ss.hasPermi('system:tenant:add')")
    @Log(title = "租户管理", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit(interval = 1000)
    public AjaxResult add(@Validated @RequestBody TenantCreateReqDTO createReqVO) {
        return toAjax(sysTenantService.createTenant(createReqVO));
    }

    /**
     * 修改租户管理
     */
    @PreAuthorize("@ss.hasPermi('system:tenant:edit')")
    @Log(title = "租户管理", businessType = BusinessType.UPDATE)
    @PutMapping
    @RepeatSubmit(interval = 1000)
    public AjaxResult edit(@Validated @RequestBody TenantUpdateReqDTO updateReqVO) {
        return toAjax(sysTenantService.updateTenant(updateReqVO));
    }

    /**
     * 删除租户管理
     */
    @PreAuthorize("@ss.hasPermi('system:tenant:remove')")
    @Log(title = "租户管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{infoId}")
    @RepeatSubmit(interval = 1000)
    public AjaxResult remove(@PathVariable Long infoId) {
        return toAjax(sysTenantService.deleteTenant(infoId));
    }

    @PreAuthorize("@ss.hasPermi('system:user:resetPwd')")
    @Log(title = "重置密码", businessType = BusinessType.UPDATE)
    @PutMapping("/resetPwd")
    @RepeatSubmit(interval = 1000)
    public AjaxResult resetPwd(@RequestBody TenantUserUpdatePasswordReqDTO user) {
        sysTenantService.updateUserPassword(user);
        return toAjax(true);
    }
}