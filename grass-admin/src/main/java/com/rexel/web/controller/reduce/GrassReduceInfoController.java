package com.rexel.web.controller.reduce;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.rexel.system.domain.vo.*;
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
import com.rexel.system.domain.GrassReduceInfo;
import com.rexel.system.service.IGrassReduceInfoService;
import com.rexel.common.utils.poi.ExcelUtil;
import com.rexel.common.core.page.TableDataInfo;

/**
 * 预聚合信息Controller
 *
 * @author grass-service
 * @date 2023-04-27
 */
@RestController
@RequestMapping("/rexel-api/reduceInfo")
public class GrassReduceInfoController extends BaseController {
    @Autowired
    private IGrassReduceInfoService grassReduceInfoService;

    /**
     * 查询预聚合信息列表
     */
    @GetMapping("/list")
    public TableDataInfo list(GrassReduceInfo grassReduceInfo) {
        startPage();
        List<GrassReduceInfo> list = grassReduceInfoService.selectGrassReduceInfoList(grassReduceInfo);
        return getDataTable(list);
    }

    /**
     * 查询预聚合信息列表
     */
    @GetMapping("/dropDown")
    public AjaxResult dropDown() {
        List<GrassReduceInfo> list = grassReduceInfoService.selectGrassReduceInfoList(null);
        return AjaxResult.success(list);
    }

    /**
     * 获取预聚合信息详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(grassReduceInfoService.getById(id));
    }

    /**
     * 新增预聚合信息
     */
    @Log(title = "预聚合信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody GrassReduceInfo grassReduceInfo) {
        return toAjax(grassReduceInfoService.save(grassReduceInfo));
    }

    /**
     * 修改预聚合信息
     */
    @Log(title = "预聚合信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody GrassReduceInfo grassReduceInfo) {
        return toAjax(grassReduceInfoService.updateById(grassReduceInfo));
    }

    /**
     * 删除预聚合信息
     */
    @Log(title = "预聚合信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(grassReduceInfoService.removeByIds(Arrays.asList(ids)));
    }

    /**
     * 导出预聚合信息列表
     */
    @Log(title = "预聚合信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, GrassReduceInfo grassReduceInfo) throws IOException {
        List<GrassReduceInfo> list = grassReduceInfoService.selectGrassReduceInfoList(grassReduceInfo);
        ExcelUtil<GrassReduceInfo> util = new ExcelUtil<>(GrassReduceInfo.class);
        util.exportExcel(response, list, "预聚合信息数据");
    }

    /**
     * 新增预聚合信息
     */
    @Log(title = "预聚合信息", businessType = BusinessType.UPDATE)
    @PostMapping("/update")
    public AjaxResult update(@RequestBody List<GrassReduceInfo> grassReduceInfoList) {
        grassReduceInfoService.updateGrassReduceInfo(grassReduceInfoList);
        return AjaxResult.success();
    }

    /**
     * 执行人工预聚合
     */
    @Log(title = "预聚合信息", businessType = BusinessType.UPDATE)
    @PostMapping("/execute")
    public AjaxResult execute(@RequestBody GrassReduceExecuteVO grassReduceExecuteVO) {
        return grassReduceInfoService.doManualExecuteReduce(grassReduceExecuteVO);
    }

    /**
     * 执行人工预聚合
     */
    @Log(title = "预聚合信息", businessType = BusinessType.UPDATE)
    @PostMapping("/executeOne")
    public AjaxResult executeOne(@RequestBody GrassReduceExecuteOneVO grassReduceExecuteOneVO) {
        return grassReduceInfoService.doManualExecuteOneReduce(grassReduceExecuteOneVO);
    }

    /**
     * 删除预聚合信息
     */
    @Log(title = "预聚合信息", businessType = BusinessType.DELETE)
    @PostMapping("/delete")
    public AjaxResult delete(@RequestBody GrassReduceDeleteVO grassReduceDeleteVO) {
        return grassReduceInfoService.doDeleteReduceData(grassReduceDeleteVO);
    }

    /**
     * 删除预聚合信息
     */
    @Log(title = "预聚合信息", businessType = BusinessType.DELETE)
    @PostMapping("/deleteOne")
    public AjaxResult deleteOne(@RequestBody GrassReduceDeleteOneVO grassReduceDeleteOneVO) {
        return grassReduceInfoService.doDeleteReduceDataOne(grassReduceDeleteOneVO);
    }

    /**
     * 删除预聚合信息
     */
    @Log(title = "预聚合信息", businessType = BusinessType.DELETE)
    @PostMapping("/deleteAll")
    public AjaxResult deleteAll() {
        return grassReduceInfoService.doDeleteReduceDataAll();
    }

    /**
     * 查询预聚合结果状态
     */
    @PostMapping("/planStatus")
    public AjaxResult planStatus(@RequestBody GrassReduceQueryPlanVO grassReduceQueryPlanVO) {
        return grassReduceInfoService.queryPlanStatusList(grassReduceQueryPlanVO);
    }
}