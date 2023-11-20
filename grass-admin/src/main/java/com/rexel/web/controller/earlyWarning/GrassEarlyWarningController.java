package com.rexel.web.controller.earlyWarning;

import com.rexel.common.annotation.Log;
import com.rexel.common.core.controller.BaseController;
import com.rexel.common.core.domain.AjaxResult;
import com.rexel.common.core.page.TableDataInfo;
import com.rexel.common.enums.BusinessType;
import com.rexel.common.utils.poi.ExcelUtil;
import com.rexel.earlywarning.domain.GrassEarlyWarning;
import com.rexel.earlywarning.service.IGrassEarlyWarningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 预警规则Controller
 *
 * @author admin
 * @date 2022-01-14
 */
@RestController
@RequestMapping("/rexel/earlyWarning")
public class GrassEarlyWarningController extends BaseController {
    @Autowired
    private IGrassEarlyWarningService grassEarlyWarningService;

    /**
     * 查询预警规则列表
     *
     * @param grassEarlyWarning grassEarlyWarning
     * @return TableDataInfo
     */
    @PostMapping("/list")
    public TableDataInfo list(@RequestBody GrassEarlyWarning grassEarlyWarning) {
        startPage();
        List<GrassEarlyWarning> list = grassEarlyWarningService.selectGrassEarlyWarningList(grassEarlyWarning);
        return getDataTable(list,"earlyWarningList");
    }

    /**
     * 导出预警规则列表
     *
     * @param grassEarlyWarning grassEarlyWarning
     * @return AjaxResult
     */
    @PostMapping("/export")
    public AjaxResult exportExcel(@RequestBody GrassEarlyWarning grassEarlyWarning) {
        List<GrassEarlyWarning> list = grassEarlyWarningService.selectGrassEarlyWarningList(grassEarlyWarning);
        ExcelUtil<GrassEarlyWarning> util = new ExcelUtil<>(GrassEarlyWarning. class);
        return util.exportExcel(list, "warning数据","预警规则");
    }

    /**
     * 获取预警规则详细信息
     *
     * @param grassEarlyWarning grassEarlyWarning
     * @return AjaxResult
     */
    @PostMapping("/info")
    public AjaxResult getInfo(@RequestBody GrassEarlyWarning grassEarlyWarning) {
        Long rulesId = grassEarlyWarning.getRulesId();
        GrassEarlyWarning info = grassEarlyWarningService.selectGrassEarlyWarningById(rulesId);
        return AjaxResult.success(info);
    }

    /**
     * 新增预警规则
     *
     * @param grassEarlyWarning grassEarlyWarning
     * @return AjaxResult
     */
    @Log(title = "预警规则", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@RequestBody GrassEarlyWarning grassEarlyWarning) {
        grassEarlyWarningService.insertGrassEarlyWarning(grassEarlyWarning);
        return AjaxResult.success();
    }

    /**
     * 修改预警规则
     *
     * @param grassEarlyWarning grassEarlyWarning
     * @return AjaxResult
     */
    @Log(title = "预警规则", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody GrassEarlyWarning grassEarlyWarning) {
        grassEarlyWarningService.updateGrassEarlyWarning(grassEarlyWarning);
        return AjaxResult.success();
    }

    /**
     * 修改预警规则状态
     *
     * @param grassEarlyWarning grassEarlyWarning
     * @return AjaxResult
     */
    @Log(title = "预警规则", businessType = BusinessType.UPDATE)
    @PostMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody GrassEarlyWarning grassEarlyWarning) {
        grassEarlyWarningService.updateGrassEarlyWarningStatus(grassEarlyWarning);
        return AjaxResult.success();
    }

    /**
     * 删除预警规则
     *
     * @param grassEarlyWarning grassEarlyWarning
     * @return AjaxResult
     */
    @Log(title = "预警规则", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody GrassEarlyWarning grassEarlyWarning) {
        grassEarlyWarningService.deleteGrassEarlyWarningById(grassEarlyWarning.getRulesId());
        return AjaxResult.success();
    }

    /**
     * 删除预警规则
     *
     * @param grassEarlyWarning grassEarlyWarning
     * @return AjaxResult
     */
    @Log(title = "预警规则", businessType = BusinessType.UPDATE)
    @PostMapping("/template/update")
    public AjaxResult updateTemplate(@RequestBody GrassEarlyWarning grassEarlyWarning) {
        grassEarlyWarningService.updateGrassEarlyWarningTemplate(grassEarlyWarning);
        return AjaxResult.success();
    }

    /**
     * 删除预警规则
     *
     * @return AjaxResult
     */
    @Log(title = "预警规则", businessType = BusinessType.UPDATE)
    @PostMapping("/template/list")
    public TableDataInfo getTemplateList() {
        startPage();
        GrassEarlyWarning select = new GrassEarlyWarning();
        select.setIsTemplate(true);
        List<GrassEarlyWarning> list = grassEarlyWarningService.selectGrassEarlyWarningList(select);
        return getDataTable(list,"earlyWarningList");
    }
}
