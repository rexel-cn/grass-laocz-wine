package com.rexel.web.controller.earlyWarning;

import com.rexel.common.annotation.Log;
import com.rexel.common.core.controller.BaseController;
import com.rexel.common.core.domain.AjaxResult;
import com.rexel.common.core.page.TableDataInfo;
import com.rexel.common.enums.BusinessType;
import com.rexel.common.utils.poi.ExcelUtil;
import com.rexel.earlywarning.domain.GrassEarlyWarningAlarmHis;
import com.rexel.earlywarning.service.IGrassEarlyWarningAlarmHisService;
import com.rexel.earlywarning.vo.GrassEarlyWarningAlarmHisQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 预警规则报警历史Controller
 *
 * @author admin
 * @date 2022-01-14
 */
@RestController
@RequestMapping("/rexel/earlyWarning/alarmHis")
public class GrassEarlyWarningAlarmHisController extends BaseController {
    @Autowired
    private IGrassEarlyWarningAlarmHisService grassEarlyWarningAlarmHisService;

    /**
     * 查询预警规则报警历史列表
     *
     * @param alarmHisQuery alarmHisQuery
     * @return TableDataInfo
     */
    @PostMapping("/list")
    public TableDataInfo list(@RequestBody GrassEarlyWarningAlarmHisQuery alarmHisQuery) {
        startPage();
        List<GrassEarlyWarningAlarmHis> list =
                grassEarlyWarningAlarmHisService.selectGrassEarlyWarningAlarmHisList(alarmHisQuery);
        return getDataTable(list,"earlyWarningAlarmHis");
    }

    /**
     * 查询预警规则报警历史聚合结果
     *
     * @param alarmHisQuery alarmHisQuery
     * @return AjaxResult
     */
    @PostMapping("/group")
    public AjaxResult group(@RequestBody GrassEarlyWarningAlarmHisQuery alarmHisQuery) {
        return AjaxResult.success(
                grassEarlyWarningAlarmHisService.selectGrassEarlyWarningAlarmHisGroup(alarmHisQuery));
    }

    /**
     * 查询预警规则报警历史聚合结果
     *
     * @param alarmHisQuery alarmHisQuery
     * @return AjaxResult
     */
    @PostMapping("/getPieChartData")
    public AjaxResult getPieChartData(@RequestBody GrassEarlyWarningAlarmHisQuery alarmHisQuery) {
        return grassEarlyWarningAlarmHisService.getPieChartData(alarmHisQuery.getTenantId());
    }

    /**
     * 导出预警规则报警历史列表
     *
     * @param alarmHisQuery alarmHisQuery
     * @return AjaxResult
     */
    @Log(title = "预警规则报警历史", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public AjaxResult exportExcel(@RequestBody GrassEarlyWarningAlarmHisQuery alarmHisQuery) {
        List<GrassEarlyWarningAlarmHis> list =
                grassEarlyWarningAlarmHisService.selectGrassEarlyWarningAlarmHisList(alarmHisQuery);
        ExcelUtil<GrassEarlyWarningAlarmHis> util = new ExcelUtil<>(GrassEarlyWarningAlarmHis. class);
        return util.exportExcel(list, "his数据","预警规则报警历史");
    }

    /**
     * 获取预警规则报警历史详细信息
     *
     * @param alarmHis alarmHis
     * @return AjaxResult
     */
    @PostMapping(value = "/info")
    public AjaxResult getInfo(@RequestBody GrassEarlyWarningAlarmHis alarmHis) {
        return AjaxResult.success(
                grassEarlyWarningAlarmHisService.selectGrassEarlyWarningAlarmHisById(alarmHis.getHisId()));
    }

    /**
     * 新增预警规则报警历史
     *
     * @param alarmHis alarmHis
     * @return AjaxResult
     */
    @Log(title = "预警规则报警历史", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@RequestBody GrassEarlyWarningAlarmHis alarmHis) {
        return toAjax(grassEarlyWarningAlarmHisService.insertGrassEarlyWarningAlarmHis(alarmHis));
    }

    /**
     * 修改预警规则报警历史
     *
     * @param alarmHis alarmHis
     * @return AjaxResult
     */
    @Log(title = "预警规则报警历史", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody GrassEarlyWarningAlarmHis alarmHis) {
        return toAjax(grassEarlyWarningAlarmHisService.updateGrassEarlyWarningAlarmHis(alarmHis));
    }

    /**
     * 删除预警规则报警历史
     *
     * @param alarmHis alarmHis
     * @return AjaxResult
     */
    @Log(title = "预警规则报警历史", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody GrassEarlyWarningAlarmHis alarmHis) {
        return toAjax(grassEarlyWarningAlarmHisService.deleteGrassEarlyWarningAlarmHisById(alarmHis.getHisId()));
    }
}
