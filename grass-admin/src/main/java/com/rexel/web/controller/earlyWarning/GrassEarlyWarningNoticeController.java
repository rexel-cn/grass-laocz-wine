package com.rexel.web.controller.earlyWarning;

import com.rexel.common.annotation.Log;
import com.rexel.common.core.controller.BaseController;
import com.rexel.common.core.domain.AjaxResult;
import com.rexel.common.core.page.TableDataInfo;
import com.rexel.common.enums.BusinessType;
import com.rexel.common.utils.poi.ExcelUtil;
import com.rexel.earlywarning.domain.GrassEarlyWarningNotice;
import com.rexel.earlywarning.service.IGrassEarlyWarningNoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 预警规则通知范围Controller
 *
 * @author admin
 * @date 2022-01-14
 */
@RestController
@RequestMapping("/rexel/earlyWarning/scope")
public class GrassEarlyWarningNoticeController extends BaseController {
    @Autowired
    private IGrassEarlyWarningNoticeService grassEarlyWarningNoticeService;

    /**
     * 查询预警规则通知范围列表
     *
     * @param notice notice
     * @return TableDataInfo
     */
    @PostMapping("/list")
    public TableDataInfo list(@RequestBody GrassEarlyWarningNotice notice) {
        startPage();
        List<GrassEarlyWarningNotice> list = grassEarlyWarningNoticeService.selectGrassEarlyWarningNoticeList(notice);
        return getDataTable(list,"earlyWarningNoticeScope");
    }

    /**
     * 导出预警规则通知范围列表
     *
     * @param notice notice
     * @return AjaxResult
     */
    @PostMapping("/export")
    public AjaxResult exportExcel(@RequestBody GrassEarlyWarningNotice notice) {
        List<GrassEarlyWarningNotice> list = grassEarlyWarningNoticeService.selectGrassEarlyWarningNoticeList(notice);
        ExcelUtil<GrassEarlyWarningNotice> util = new ExcelUtil<>(GrassEarlyWarningNotice. class);
        return util.exportExcel(list, "scope数据","预警规则通知范围");
    }

    /**
     * 新增预警规则通知范围
     *
     * @param notice notice
     * @return AjaxResult
     */
    @Log(title = "预警规则通知范围", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@RequestBody GrassEarlyWarningNotice notice) {
        return toAjax(grassEarlyWarningNoticeService.insertGrassEarlyWarningNotice(notice));
    }

    /**
     * 修改预警规则通知范围
     *
     * @param notice notice
     * @return AjaxResult
     */
    @Log(title = "预警规则通知范围", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody GrassEarlyWarningNotice notice) {
        return toAjax(grassEarlyWarningNoticeService.updateGrassEarlyWarningNotice(notice));
    }

    /**
     * 删除预警规则通知范围
     *
     * @param notice notice
     * @return AjaxResult
     */
    @Log(title = "预警规则通知范围", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody GrassEarlyWarningNotice notice) {
        return toAjax(grassEarlyWarningNoticeService.deleteGrassEarlyWarningNoticeById(notice.getId()));
    }
}
