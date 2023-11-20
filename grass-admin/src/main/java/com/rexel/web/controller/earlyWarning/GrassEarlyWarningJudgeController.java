package com.rexel.web.controller.earlyWarning;

import com.rexel.common.annotation.Log;
import com.rexel.common.core.controller.BaseController;
import com.rexel.common.core.domain.AjaxResult;
import com.rexel.common.core.page.TableDataInfo;
import com.rexel.common.enums.BusinessType;
import com.rexel.common.utils.poi.ExcelUtil;
import com.rexel.earlywarning.domain.GrassEarlyWarningJudge;
import com.rexel.earlywarning.service.IGrassEarlyWarningJudgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 预警规则判断条件Controller
 *
 * @author admin
 * @date 2022-01-14
 */
@RestController
@RequestMapping("/rexel/earlyWarning/judge")
public class GrassEarlyWarningJudgeController extends BaseController {
    @Autowired
    private IGrassEarlyWarningJudgeService grassEarlyWarningJudgeService;

    /**
     * 查询预警规则判断条件列表
     *
     * @param judge judge
     * @return TableDataInfo
     */
    @PostMapping("/list")
    public TableDataInfo list(@RequestBody GrassEarlyWarningJudge judge) {
        startPage();
        List<GrassEarlyWarningJudge> list = grassEarlyWarningJudgeService.selectGrassEarlyWarningJudgeList(judge);
        return getDataTable(list,"earlyWarningJudge");
    }

    /**
     * 导出预警规则判断条件列表
     *
     * @param judge judge
     * @return AjaxResult
     */
    @PostMapping("/export")
    public AjaxResult exportExcel(@RequestBody GrassEarlyWarningJudge judge) {
        List<GrassEarlyWarningJudge> list = grassEarlyWarningJudgeService.selectGrassEarlyWarningJudgeList(judge);
        ExcelUtil<GrassEarlyWarningJudge> util = new ExcelUtil<>(GrassEarlyWarningJudge. class);
        return util.exportExcel(list, "judge数据","预警规则判断条件");
    }

    /**
     * 新增预警规则判断条件
     *
     * @param judge judge
     * @return AjaxResult
     */
    @Log(title = "预警规则判断条件", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@RequestBody GrassEarlyWarningJudge judge) {
        return toAjax(grassEarlyWarningJudgeService.insertGrassEarlyWarningJudge(judge));
    }

    /**
     * 修改预警规则判断条件
     *
     * @param judge judge
     * @return AjaxResult
     */
    @Log(title = "预警规则判断条件", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody GrassEarlyWarningJudge judge) {
        return toAjax(grassEarlyWarningJudgeService.updateGrassEarlyWarningJudge(judge));
    }

    /**
     * 删除预警规则判断条件
     *
     * @param judge judge
     * @return AjaxResult
     */
    @Log(title = "预警规则判断条件", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody GrassEarlyWarningJudge judge) {
        return toAjax(grassEarlyWarningJudgeService.deleteGrassEarlyWarningJudgeById(judge.getRulesId()));
    }
}
