package com.rexel.web.controller.system;

import com.rexel.common.annotation.Log;
import com.rexel.common.core.controller.BaseController;
import com.rexel.common.core.domain.AjaxResult;
import com.rexel.common.enums.BusinessType;
import com.rexel.system.domain.SysTenantDetail;
import com.rexel.system.service.ISysTenantDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * 租户(公司)详情Controller
 *
 * @author grass-service
 * @date 2023-03-01
 */
@RestController
@RequestMapping("/rexel-api/tenant/detail")
public class SysTenantDetailController extends BaseController {
    @Autowired
    private ISysTenantDetailService sysTenantDetailService;


    /**
     * 获取租户(公司)详情详细信息
     */
    // @PreAuthorize("@ss.hasPermi('system:detail:query')")
    @GetMapping()
    public AjaxResult getInfo() {
        return AjaxResult.success(sysTenantDetailService.getDetail());
    }


    /**
     * 更新详情 更新公司介绍，公司文本标题
     */
    //  @PreAuthorize("@ss.hasPermi('system:detail:edit')")
    @Log(title = "租户(公司)详情", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysTenantDetail sysTenantDetail) {
        return toAjax(sysTenantDetailService.updateDetail(sysTenantDetail));
    }

    /**
     * 更新详情  更新公司介绍图片
     */
    //  @PreAuthorize("@ss.hasPermi('system:detail:edit')")
    @Log(title = "租户(公司)详情", businessType = BusinessType.UPDATE)
    @PutMapping("/introduce")
    public AjaxResult editIntroduceUrl(@RequestBody SysTenantDetail sysTenantDetail) {
        return toAjax(sysTenantDetailService.updateIntroduce(sysTenantDetail));
    }

    /**
     * 用户与设备统计
     *
     * @return
     */
    @GetMapping("/user/asset")
    public AjaxResult getUserAssetInfo() {
        return AjaxResult.success(sysTenantDetailService.getUserAssetInfo());
    }

    /**
     * 物联设备与测点情况统计
     *
     * @return
     */
    @GetMapping("/device/point")
    public AjaxResult getDevicePointInfo() {
        return AjaxResult.success(sysTenantDetailService.getDevicePointInfo());
    }

    /**
     * 报警数量统计-柱状图表
     *
     * @return
     */
    @GetMapping("/alarm/chart/{type}")
    public AjaxResult getAlarmChart(@PathVariable("type") int type) {
        return AjaxResult.success(sysTenantDetailService.getAlarmChart(type));
    }


    /**
     * 删除租户(公司)详情
     */
    // @PreAuthorize("@ss.hasPermi('system:detail:remove')")
    @Log(title = "租户(公司)详情", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(sysTenantDetailService.removeByIds(Arrays.asList(ids)));
    }


}
