package com.rexel.web.controller.earlyWarning;

import com.rexel.common.annotation.Log;
import com.rexel.common.core.controller.BaseController;
import com.rexel.common.core.domain.AjaxResult;
import com.rexel.common.core.page.TableDataInfo;
import com.rexel.common.enums.BusinessType;
import com.rexel.common.utils.poi.ExcelUtil;
import com.rexel.earlywarning.domain.GrassEarlyWarningCarrier;
import com.rexel.earlywarning.service.IGrassEarlyWarningCarrierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 预警规则运行载体Controller
 *
 * @author admin
 * @date 2022-02-23
 */
@RestController
@RequestMapping("/rexel/earlyWarning/carrier")
public class GrassEarlyWarningCarrierController extends BaseController {
    @Autowired
    private IGrassEarlyWarningCarrierService grassEarlyWarningCarrierService;

    /**
     * 查询预警规则运行载体列表
     *
     * @param carrier carrier
     * @return TableDataInfo
     */
    @PostMapping("/list")
    public TableDataInfo list(@RequestBody GrassEarlyWarningCarrier carrier) {
        startPage();
        List<GrassEarlyWarningCarrier> list =
                grassEarlyWarningCarrierService.selectGrassEarlyWarningCarrierList(carrier);
        return getDataTable(list,"earlyWarningCarrier");
    }

    /**
     * 查询预警规则下拉列表
     *
     * @return AjaxResult
     */
    @PostMapping("/dropDown")
    public AjaxResult dropDown() {
        return AjaxResult.success(
                grassEarlyWarningCarrierService.selectGrassEarlyWarningCarrierList(null));
    }

    /**
     * 导出预警规则运行载体列表
     *
     * @param carrier carrier
     * @return AjaxResult
     */
    @Log(title = "预警规则运行载体", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public AjaxResult exportExcel(@RequestBody GrassEarlyWarningCarrier carrier) {
        List<GrassEarlyWarningCarrier> list =
                grassEarlyWarningCarrierService.selectGrassEarlyWarningCarrierList(carrier);
        ExcelUtil<GrassEarlyWarningCarrier> util = new ExcelUtil<>(GrassEarlyWarningCarrier. class);
        return util.exportExcel(list, "预警规则运行载体","预警规则运行载体");
    }

    /**
     * 新增预警规则运行载体
     *
     * @param carrier carrier
     * @return AjaxResult
     */
    @Log(title = "预警规则运行载体", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@RequestBody GrassEarlyWarningCarrier carrier) {
        return toAjax(grassEarlyWarningCarrierService.insertGrassEarlyWarningCarrier(carrier));
    }

    /**
     * 修改预警规则运行载体
     *
     * @param carrier carrier
     * @return AjaxResult
     */
    @Log(title = "预警规则运行载体", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody GrassEarlyWarningCarrier carrier) {
        return toAjax(grassEarlyWarningCarrierService.updateGrassEarlyWarningCarrier(carrier));
    }

    /**
     * 删除预警规则运行载体
     *
     * @param carrier carrier
     * @return AjaxResult
     */
    @Log(title = "预警规则运行载体", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody GrassEarlyWarningCarrier carrier) {
        return toAjax(grassEarlyWarningCarrierService.deleteGrassEarlyWarningCarrierById(carrier.getId()));
    }
}
