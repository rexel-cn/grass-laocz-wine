package com.rexel.web.controller.earlyWarning;

import com.rexel.common.annotation.Log;
import com.rexel.common.core.controller.BaseController;
import com.rexel.common.core.domain.AjaxResult;
import com.rexel.common.core.page.TableDataInfo;
import com.rexel.common.enums.BusinessType;
import com.rexel.common.utils.poi.ExcelUtil;
import com.rexel.earlywarning.domain.GrassEarlyWarningFinish;
import com.rexel.earlywarning.service.IGrassEarlyWarningFinishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 预警规则结束条件Controller
 *
 * @author admin
 * @date 2022-01-14
 */
@RestController
@RequestMapping("/rexel/earlyWarning/finish")
public class GrassEarlyWarningFinishController extends BaseController {
    @Autowired
    private IGrassEarlyWarningFinishService grassEarlyWarningFinishService;

    /**
     * 查询预警规则结束条件列表
     *
     * @param finish finish
     * @return TableDataInfo
     */
    @PostMapping("/list")
    public TableDataInfo list(@RequestBody GrassEarlyWarningFinish finish) {
        startPage();
        List<GrassEarlyWarningFinish> list =
                grassEarlyWarningFinishService.selectGrassEarlyWarningFinishList(finish);
        return getDataTable(list,"earlyWarningFinish");
    }

    /**
     * 导出预警规则结束条件列表
     *
     * @param finish finish
     * @return AjaxResult
     */
    @Log(title = "预警规则结束条件", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public AjaxResult exportExcel(@RequestBody GrassEarlyWarningFinish finish) {
        List<GrassEarlyWarningFinish> list =
                grassEarlyWarningFinishService.selectGrassEarlyWarningFinishList(finish);
        ExcelUtil<GrassEarlyWarningFinish> util = new ExcelUtil<>(GrassEarlyWarningFinish. class);
        return util.exportExcel(list, "finish数据","预警规则结束条件");
    }

    /**
     * 新增预警规则结束条件
     *
     * @param finish finish
     * @return AjaxResult
     */
    @Log(title = "预警规则结束条件", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@RequestBody GrassEarlyWarningFinish finish) {
        return toAjax(grassEarlyWarningFinishService.insertGrassEarlyWarningFinish(finish));
    }

    /**
     * 修改预警规则结束条件
     *
     * @param finish finish
     * @return AjaxResult
     */
    @Log(title = "预警规则结束条件", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody GrassEarlyWarningFinish finish) {
        return toAjax(grassEarlyWarningFinishService.updateGrassEarlyWarningFinish(finish));
    }

    /**
     * 删除预警规则结束条件
     *
     * @param finish finish
     * @return AjaxResult
     */
    @Log(title = "预警规则结束条件", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody GrassEarlyWarningFinish finish) {
        return toAjax(grassEarlyWarningFinishService.deleteGrassEarlyWarningFinishById(finish.getRulesId()));
    }
}
