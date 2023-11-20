package com.rexel.web.controller.earlyWarning;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.rexel.earlywarning.domain.GrassEarlyWarningSuggestHis;
import com.rexel.earlywarning.service.IGrassEarlyWarningSuggestHisService;
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
 * 处置建议历史Controller
 *
 * @author grass-service
 * @date 2023-10-17
 */
@RestController
@RequestMapping("/rexel-api/earlyWarning/suggest/his")
public class GrassEarlyWarningSuggestHisController extends BaseController {
    @Autowired
    private IGrassEarlyWarningSuggestHisService grassEarlyWarningSuggestHisService;

    /**
     * 查询处置建议历史列表
     */
    @GetMapping("/list")
    public TableDataInfo list(GrassEarlyWarningSuggestHis suggestHis) {
        startPage();
        List<GrassEarlyWarningSuggestHis> list =
                grassEarlyWarningSuggestHisService.selectGrassEarlyWarningSuggestHisList(suggestHis);
        return getDataTable(list);
    }

    /**
     * 获取处置建议历史详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(grassEarlyWarningSuggestHisService.getById(id));
    }

    /**
     * 新增处置建议历史
     */
    @Log(title = "处置建议历史", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody GrassEarlyWarningSuggestHis suggestHis) {
        return toAjax(grassEarlyWarningSuggestHisService.save(suggestHis));
    }

    /**
     * 修改处置建议历史
     */
    @Log(title = "处置建议历史", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody GrassEarlyWarningSuggestHis suggestHis) {
        return toAjax(grassEarlyWarningSuggestHisService.updateById(suggestHis));
    }

    /**
     * 删除处置建议历史
     */
    @Log(title = "处置建议历史", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(grassEarlyWarningSuggestHisService.removeByIds(Arrays.asList(ids)));
    }

    /**
     * 导出处置建议历史列表
     */
    @Log(title = "处置建议历史", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, GrassEarlyWarningSuggestHis suggestHis) throws IOException {
        List<GrassEarlyWarningSuggestHis> list =
                grassEarlyWarningSuggestHisService.selectGrassEarlyWarningSuggestHisList(suggestHis);
        ExcelUtil<GrassEarlyWarningSuggestHis> util = new ExcelUtil<>(GrassEarlyWarningSuggestHis.class);
        util.exportExcel(response, list, "处置建议历史数据");
    }
}
