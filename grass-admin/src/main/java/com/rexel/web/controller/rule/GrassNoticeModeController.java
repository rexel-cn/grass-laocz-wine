package com.rexel.web.controller.rule;

import com.rexel.common.annotation.Log;
import com.rexel.common.core.controller.BaseController;
import com.rexel.common.core.domain.AjaxResult;
import com.rexel.common.core.page.TableDataInfo;
import com.rexel.common.enums.BusinessType;
import com.rexel.common.utils.poi.ExcelUtil;
import com.rexel.system.domain.GrassNoticeMode;
import com.rexel.system.service.IGrassNoticeModeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * 通知模板对应方式Controller
 *
 * @author grass-service
 * @date 2022-08-01
 */
@RestController
@RequestMapping("/rexel-api/mode")
public class GrassNoticeModeController extends BaseController {
    @Autowired
    private IGrassNoticeModeService grassNoticeModeService;

    /**
     * 查询通知模板对应方式列表
     */
    @PreAuthorize("@ss.hasPermi('system:mode:list')")
    @GetMapping("/list")
    public TableDataInfo list(GrassNoticeMode grassNoticeMode) {
        startPage();
        List<GrassNoticeMode> list = grassNoticeModeService.selectGrassNoticeModeList(grassNoticeMode);
        return getDataTable(list);
    }


    /**
     * 获取通知模板对应方式详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:mode:query')")
    @GetMapping(value = "/{noticeTemplateId}")
    public AjaxResult getInfo(@PathVariable("noticeTemplateId") Long noticeTemplateId) {
        return AjaxResult.success(grassNoticeModeService.getById(noticeTemplateId));
    }

    /**
     * 新增通知模板对应方式
     */
    @PreAuthorize("@ss.hasPermi('system:mode:add')")
    @Log(title = "通知模板对应方式", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody GrassNoticeMode grassNoticeMode) {
        return toAjax(grassNoticeModeService.save(grassNoticeMode));
    }

    /**
     * 修改通知模板对应方式
     */
    @PreAuthorize("@ss.hasPermi('system:mode:edit')")
    @Log(title = "通知模板对应方式", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody GrassNoticeMode grassNoticeMode) {
        return toAjax(grassNoticeModeService.updateById(grassNoticeMode));
    }

    /**
     * 删除通知模板对应方式
     */
    @PreAuthorize("@ss.hasPermi('system:mode:remove')")
    @Log(title = "通知模板对应方式", businessType = BusinessType.DELETE)
    @DeleteMapping("/{noticeTemplateIds}")
    public AjaxResult remove(@PathVariable Long[] noticeTemplateIds) {
        return toAjax(grassNoticeModeService.removeByIds(Arrays.asList(noticeTemplateIds)));
    }

    /**
     * 导出通知模板对应方式列表
     */
    @PreAuthorize("@ss.hasPermi('system:mode:export')")
    @Log(title = "通知模板对应方式", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, GrassNoticeMode grassNoticeMode) throws IOException {
        List<GrassNoticeMode> list = grassNoticeModeService.selectGrassNoticeModeList(grassNoticeMode);
        ExcelUtil<GrassNoticeMode> util = new ExcelUtil<>(GrassNoticeMode.class);
        util.exportExcel(response, list, "通知模板对应方式数据");
    }
}
