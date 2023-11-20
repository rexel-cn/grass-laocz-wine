package com.rexel.web.controller.earlyWarning;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.rexel.earlywarning.domain.GrassEarlyWarningTriggerHis;
import com.rexel.earlywarning.service.IGrassEarlyWarningTriggerHisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.rexel.common.annotation.Log;
import com.rexel.common.core.controller.BaseController;
import com.rexel.common.core.domain.AjaxResult;
import com.rexel.common.enums.BusinessType;
import com.rexel.common.utils.poi.ExcelUtil;
import com.rexel.common.core.page.TableDataInfo;

/**
 * 预警规则触发条件历史Controller
 *
 * @author grass-service
 * @date 2023-10-17
 */
@RestController
@RequestMapping("/rexel-api/earlyWarning/trigger/his")
public class GrassEarlyWarningTriggerHisController extends BaseController {
    @Autowired
    private IGrassEarlyWarningTriggerHisService grassEarlyWarningTriggerHisService;

    /**
     * 查询预警规则触发条件历史列表
     */
    @GetMapping("/list")
    public TableDataInfo list(GrassEarlyWarningTriggerHis triggerHis) {
        startPage();
        List<GrassEarlyWarningTriggerHis> list =
                grassEarlyWarningTriggerHisService.selectGrassEarlyWarningTriggerHisList(triggerHis);
        return getDataTable(list);
    }

    /**
     * 获取预警规则触发条件历史详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(grassEarlyWarningTriggerHisService.getById(id));
    }

    /**
     * 新增预警规则触发条件历史
     */
    @Log(title = "预警规则触发条件历史", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody GrassEarlyWarningTriggerHis triggerHis) {
        return toAjax(grassEarlyWarningTriggerHisService.save(triggerHis));
    }

    /**
     * 修改预警规则触发条件历史
     */
    @Log(title = "预警规则触发条件历史", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody GrassEarlyWarningTriggerHis triggerHis) {
        return toAjax(grassEarlyWarningTriggerHisService.updateById(triggerHis));
    }

    /**
     * 删除预警规则触发条件历史
     */
    @Log(title = "预警规则触发条件历史", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(grassEarlyWarningTriggerHisService.removeByIds(Arrays.asList(ids)));
    }

    /**
     * 导出预警规则触发条件历史列表
     */
    @Log(title = "预警规则触发条件历史", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, GrassEarlyWarningTriggerHis triggerHis) throws IOException {
        List<GrassEarlyWarningTriggerHis> list =
                grassEarlyWarningTriggerHisService.selectGrassEarlyWarningTriggerHisList(triggerHis);
        ExcelUtil<GrassEarlyWarningTriggerHis> util = new ExcelUtil<>(GrassEarlyWarningTriggerHis.class);
        util.exportExcel(response, list, "预警规则触发条件历史数据");
    }
}
