package com.rexel.web.controller.earlyWarning;

import java.util.List;

import com.rexel.common.annotation.Log;
import com.rexel.common.core.controller.BaseController;
import com.rexel.common.core.domain.AjaxResult;
import com.rexel.common.core.page.TableDataInfo;
import com.rexel.common.enums.BusinessType;
import com.rexel.common.utils.poi.ExcelUtil;
import com.rexel.earlywarning.domain.GrassEarlyWarningTrigger;
import com.rexel.earlywarning.service.IGrassEarlyWarningTriggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 预警规则触发条件Controller
 *
 * @author admin
 * @date 2022-01-14
 */
@RestController
@RequestMapping("/rexel/earlyWarning/trigger")
public class GrassEarlyWarningTriggerController extends BaseController {
    @Autowired
    private IGrassEarlyWarningTriggerService grassEarlyWarningTriggerService;

    /**
     * 查询预警规则触发条件列表
     *
     * @param trigger trigger
     * @return TableDataInfo
     */
    @PostMapping("/list")
    public TableDataInfo list(@RequestBody GrassEarlyWarningTrigger trigger) {
        startPage();
        List<GrassEarlyWarningTrigger> list = grassEarlyWarningTriggerService.selectGrassEarlyWarningTriggerList(trigger);
        return getDataTable(list,"earlyWarningTrigger");
    }

    /**
     * 导出预警规则触发条件列表
     *
     * @param trigger trigger
     * @return AjaxResult
     */
    @Log(title = "预警规则触发条件", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public AjaxResult exportExcel(@RequestBody GrassEarlyWarningTrigger trigger) {
        List<GrassEarlyWarningTrigger> list = grassEarlyWarningTriggerService.selectGrassEarlyWarningTriggerList(trigger);
        ExcelUtil<GrassEarlyWarningTrigger> util = new ExcelUtil<>(GrassEarlyWarningTrigger. class);
        return util.exportExcel(list, "trigger数据","预警规则触发条件");
    }

    /**
     * 新增预警规则触发条件
     *
     * @param trigger trigger
     * @return AjaxResult
     */
    @Log(title = "预警规则触发条件", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@RequestBody GrassEarlyWarningTrigger trigger) {
        return toAjax(grassEarlyWarningTriggerService.insertGrassEarlyWarningTrigger(trigger));
    }

    /**
     * 修改预警规则触发条件
     *
     * @param trigger trigger
     * @return AjaxResult
     */
    @Log(title = "预警规则触发条件", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody GrassEarlyWarningTrigger trigger) {
        return toAjax(grassEarlyWarningTriggerService.updateGrassEarlyWarningTrigger(trigger));
    }

    /**
     * 删除预警规则触发条件
     *
     * @param trigger trigger
     * @return AjaxResult
     */
    @Log(title = "预警规则触发条件", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody GrassEarlyWarningTrigger trigger) {
        return toAjax(grassEarlyWarningTriggerService.deleteGrassEarlyWarningTriggerById(trigger.getRulesId()));
    }
}
