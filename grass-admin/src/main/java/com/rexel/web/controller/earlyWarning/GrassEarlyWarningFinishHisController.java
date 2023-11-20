package com.rexel.web.controller.earlyWarning;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.rexel.earlywarning.domain.GrassEarlyWarningFinishHis;
import com.rexel.earlywarning.service.IGrassEarlyWarningFinishHisService;
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
 * 预警规则结束条件历史Controller
 *
 * @author grass-service
 * @date 2023-10-17
 */
@RestController
@RequestMapping("/rexel-api/earlyWarning/finish/his")
public class GrassEarlyWarningFinishHisController extends BaseController {
    @Autowired
    private IGrassEarlyWarningFinishHisService grassEarlyWarningFinishHisService;

    /**
     * 查询预警规则结束条件历史列表
     */
    @GetMapping("/list")
    public TableDataInfo list(GrassEarlyWarningFinishHis finishHis) {
        startPage();
        List<GrassEarlyWarningFinishHis> list =
                grassEarlyWarningFinishHisService.selectGrassEarlyWarningFinishHisList(finishHis);
        return getDataTable(list);
    }

    /**
     * 获取预警规则结束条件历史详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(grassEarlyWarningFinishHisService.getById(id));
    }

    /**
     * 新增预警规则结束条件历史
     */
    @Log(title = "预警规则结束条件历史", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody GrassEarlyWarningFinishHis finishHis) {
        return toAjax(grassEarlyWarningFinishHisService.save(finishHis));
    }

    /**
     * 修改预警规则结束条件历史
     */
    @Log(title = "预警规则结束条件历史", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody GrassEarlyWarningFinishHis finishHis) {
        return toAjax(grassEarlyWarningFinishHisService.updateById(finishHis));
    }

    /**
     * 删除预警规则结束条件历史
     */
    @Log(title = "预警规则结束条件历史", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(grassEarlyWarningFinishHisService.removeByIds(Arrays.asList(ids)));
    }

    /**
     * 导出预警规则结束条件历史列表
     */
    @Log(title = "预警规则结束条件历史", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, GrassEarlyWarningFinishHis finishHis) throws IOException {
        List<GrassEarlyWarningFinishHis> list =
                grassEarlyWarningFinishHisService.selectGrassEarlyWarningFinishHisList(finishHis);
        ExcelUtil<GrassEarlyWarningFinishHis> util = new ExcelUtil<>(GrassEarlyWarningFinishHis.class);
        util.exportExcel(response, list, "预警规则结束条件历史数据");
    }
}
