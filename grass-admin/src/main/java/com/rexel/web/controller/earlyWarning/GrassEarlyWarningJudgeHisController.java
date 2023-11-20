package com.rexel.web.controller.earlyWarning;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.rexel.earlywarning.domain.GrassEarlyWarningJudgeHis;
import com.rexel.earlywarning.service.IGrassEarlyWarningJudgeHisService;
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
 * 预警规则判断条件历史Controller
 *
 * @author grass-service
 * @date 2023-10-17
 */
@RestController
@RequestMapping("/rexel-api/earlyWarning/judge/his")
public class GrassEarlyWarningJudgeHisController extends BaseController {
    @Autowired
    private IGrassEarlyWarningJudgeHisService grassEarlyWarningJudgeHisService;

    /**
     * 查询预警规则判断条件历史列表
     */
    @GetMapping("/list")
    public TableDataInfo list(GrassEarlyWarningJudgeHis judgeHis) {
        startPage();
        List<GrassEarlyWarningJudgeHis> list =
                grassEarlyWarningJudgeHisService.selectGrassEarlyWarningJudgeHisList(judgeHis);
        return getDataTable(list);
    }

    /**
     * 获取预警规则判断条件历史详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(grassEarlyWarningJudgeHisService.getById(id));
    }

    /**
     * 新增预警规则判断条件历史
     */
    @Log(title = "预警规则判断条件历史", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody GrassEarlyWarningJudgeHis judgeHis) {
        return toAjax(grassEarlyWarningJudgeHisService.save(judgeHis));
    }

    /**
     * 修改预警规则判断条件历史
     */
    @Log(title = "预警规则判断条件历史", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody GrassEarlyWarningJudgeHis judgeHis) {
        return toAjax(grassEarlyWarningJudgeHisService.updateById(judgeHis));
    }

    /**
     * 删除预警规则判断条件历史
     */
    @Log(title = "预警规则判断条件历史", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(grassEarlyWarningJudgeHisService.removeByIds(Arrays.asList(ids)));
    }

    /**
     * 导出预警规则判断条件历史列表
     */
    @Log(title = "预警规则判断条件历史", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, GrassEarlyWarningJudgeHis judgeHis) throws IOException {
        List<GrassEarlyWarningJudgeHis> list =
                grassEarlyWarningJudgeHisService.selectGrassEarlyWarningJudgeHisList(judgeHis);
        ExcelUtil<GrassEarlyWarningJudgeHis> util = new ExcelUtil<>(GrassEarlyWarningJudgeHis.class);
        util.exportExcel(response, list, "预警规则判断条件历史数据");
    }
}
