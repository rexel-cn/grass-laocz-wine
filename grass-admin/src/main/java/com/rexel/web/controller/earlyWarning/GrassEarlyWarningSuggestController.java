package com.rexel.web.controller.earlyWarning;

import java.util.List;

import com.rexel.common.annotation.Log;
import com.rexel.common.core.controller.BaseController;
import com.rexel.common.core.domain.AjaxResult;
import com.rexel.common.core.page.TableDataInfo;
import com.rexel.common.enums.BusinessType;
import com.rexel.common.utils.poi.ExcelUtil;
import com.rexel.earlywarning.domain.GrassEarlyWarningSuggest;
import com.rexel.earlywarning.service.IGrassEarlyWarningSuggestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 处置建议Controller
 *
 * @author admin
 * @date 2022-01-14
 */
@RestController
@RequestMapping("/rexel/suggest")
public class GrassEarlyWarningSuggestController extends BaseController {
    @Autowired
    private IGrassEarlyWarningSuggestService grassEarlyWarningSuggestService;

    /**
     * 查询处置建议列表
     *
     * @param suggest suggest
     * @return TableDataInfo
     */
    @PostMapping("/list")
    public TableDataInfo list(@RequestBody GrassEarlyWarningSuggest suggest) {
        startPage();
        List<GrassEarlyWarningSuggest> list =
                grassEarlyWarningSuggestService.selectGrassEarlyWarningSuggestList(suggest);
        return getDataTable(list,"suggestInfo");
    }

    /**
     * 查询处置建议列表
     *
     * @param suggest suggest
     * @return AjaxResult
     */
    @PostMapping("/dropDown")
    public AjaxResult dropDown(@RequestBody GrassEarlyWarningSuggest suggest) {
        List<GrassEarlyWarningSuggest> list =
                grassEarlyWarningSuggestService.selectGrassEarlyWarningSuggestList(suggest);
        return AjaxResult.success(list);
    }

    /**
     * 导出处置建议列表
     *
     * @param suggest suggest
     * @return AjaxResult
     */
    @Log(title = "处置建议", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public AjaxResult exportExcel(@RequestBody GrassEarlyWarningSuggest suggest) {
        List<GrassEarlyWarningSuggest> list =
                grassEarlyWarningSuggestService.selectGrassEarlyWarningSuggestList(suggest);
        ExcelUtil<GrassEarlyWarningSuggest> util = new ExcelUtil<>(GrassEarlyWarningSuggest. class);
        return util.exportExcel(list, "info数据","处置建议");
    }

    /**
     * 获取处置建议详细信息
     *
     * @param suggest suggest
     * @return AjaxResult
     */
    @PostMapping("/info")
    public AjaxResult getInfo(@RequestBody GrassEarlyWarningSuggest suggest) {
        return AjaxResult.success(
                grassEarlyWarningSuggestService.selectGrassEarlyWarningSuggestById(suggest.getSuggestId()));
    }

    /**
     * 新增处置建议
     *
     * @param suggest suggest
     * @return AjaxResult
     */
    @Log(title = "处置建议", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@RequestBody GrassEarlyWarningSuggest suggest) {
        return toAjax(grassEarlyWarningSuggestService.insertGrassEarlyWarningSuggest(suggest));
    }

    /**
     * 修改处置建议
     *
     * @param suggest suggest
     * @return AjaxResult
     */
    @Log(title = "处置建议", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody GrassEarlyWarningSuggest suggest) {
        return toAjax(grassEarlyWarningSuggestService.updateGrassEarlyWarningSuggest(suggest));
    }

    /**
     * 删除处置建议
     *
     * @param suggest suggest
     * @return AjaxResult
     */
    @Log(title = "处置建议", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody GrassEarlyWarningSuggest suggest) {
        Long suggestId = suggest.getSuggestId();
        return toAjax(grassEarlyWarningSuggestService.deleteGrassEarlyWarningSuggestById(suggestId));
    }
}
