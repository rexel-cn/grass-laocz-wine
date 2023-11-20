package com.rexel.web.controller.rule;

import com.rexel.common.annotation.Log;
import com.rexel.common.core.controller.BaseController;
import com.rexel.common.core.domain.AjaxResult;
import com.rexel.common.core.page.TableDataInfo;
import com.rexel.common.enums.BusinessType;
import com.rexel.common.utils.poi.ExcelUtil;
import com.rexel.system.domain.GrassNoticeScope;
import com.rexel.system.domain.vo.NoticeScopeDropDown;
import com.rexel.system.service.IGrassNoticeScopeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * 通知模板对应范围Controller
 *
 * @author grass-service
 * @date 2022-08-01
 */
@RestController
@RequestMapping("/rexel-api/scope")
public class GrassNoticeScopeController extends BaseController {
    @Autowired
    private IGrassNoticeScopeService grassNoticeScopeService;

    /**
     * 查询通知模板对应范围列表
     */
    @PreAuthorize("@ss.hasPermi('system:scope:list')")
    @GetMapping("/list")
    public TableDataInfo list(GrassNoticeScope grassNoticeScope) {
        startPage();
        List<GrassNoticeScope> list = grassNoticeScopeService.selectGrassNoticeScopeList(grassNoticeScope);
        return getDataTable(list);
    }


    /**
     * 获取通知模板对应范围详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:scope:query')")
    @GetMapping(value = "/{noticeTemplateId}")
    public AjaxResult getInfo(@PathVariable("noticeTemplateId") Long noticeTemplateId) {
        return AjaxResult.success(grassNoticeScopeService.getById(noticeTemplateId));
    }

    /**
     * 新增通知模板对应范围
     */
    @PreAuthorize("@ss.hasPermi('system:scope:add')")
    @Log(title = "通知模板对应范围", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody GrassNoticeScope grassNoticeScope) {
        return toAjax(grassNoticeScopeService.save(grassNoticeScope));
    }

    /**
     * 修改通知模板对应范围
     */
    @PreAuthorize("@ss.hasPermi('system:scope:edit')")
    @Log(title = "通知模板对应范围", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody GrassNoticeScope grassNoticeScope) {
        return toAjax(grassNoticeScopeService.updateById(grassNoticeScope));
    }

    /**
     * 删除通知模板对应范围
     */
    @PreAuthorize("@ss.hasPermi('system:scope:remove')")
    @Log(title = "通知模板对应范围", businessType = BusinessType.DELETE)
    @DeleteMapping("/{noticeTemplateIds}")
    public AjaxResult remove(@PathVariable Long[] noticeTemplateIds) {
        return toAjax(grassNoticeScopeService.removeByIds(Arrays.asList(noticeTemplateIds)));
    }

    /**
     * 导出通知模板对应范围列表
     */
    @PreAuthorize("@ss.hasPermi('system:scope:export')")
    @Log(title = "通知模板对应范围", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, GrassNoticeScope grassNoticeScope) throws IOException {
        List<GrassNoticeScope> list = grassNoticeScopeService.selectGrassNoticeScopeList(grassNoticeScope);
        ExcelUtil<GrassNoticeScope> util = new ExcelUtil<>(GrassNoticeScope.class);
        util.exportExcel(response, list, "通知模板对应范围数据");
    }

    /**
     * 通知范围下拉框
     */
    @GetMapping("/dropDown")
    public AjaxResult dropDown() {
        List<NoticeScopeDropDown> list = grassNoticeScopeService.noticeScopeList();
        return AjaxResult.success(list);
    }
}
