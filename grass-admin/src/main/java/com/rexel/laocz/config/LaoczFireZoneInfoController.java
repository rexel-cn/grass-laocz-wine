package com.rexel.laocz.config;

import com.rexel.common.annotation.Log;
import com.rexel.common.core.controller.BaseController;
import com.rexel.common.core.domain.AjaxResult;
import com.rexel.common.core.page.TableDataInfo;
import com.rexel.common.enums.BusinessType;
import com.rexel.laocz.domain.LaoczFireZoneInfo;
import com.rexel.laocz.domain.vo.FireZoneInfoVo;
import com.rexel.laocz.service.ILaoczFireZoneInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 防火区信息Controller
 *
 * @author grass-service
 * @date 2024-03-07
 */
@RestController
@RequestMapping("/rexel-api/fireZone")
public class LaoczFireZoneInfoController extends BaseController {
    @Autowired
    private ILaoczFireZoneInfoService laoczFireZoneInfoService;

    /**
     * 查询防火区信息列表
     *
     * @param laoczFireZoneInfo
     */
    @GetMapping("/list")
    public TableDataInfo list(LaoczFireZoneInfo laoczFireZoneInfo) {
        startPage();
        List<FireZoneInfoVo> list = laoczFireZoneInfoService.selectLaoczFireZoneInfoList(laoczFireZoneInfo);
        return getDataTable(list, "fireZone");
    }

    /**
     * 获取防火区信息详细信息
     */
    @GetMapping(value = "/{fireZoneId}")
    public AjaxResult getInfo(@PathVariable("fireZoneId") Long fireZoneId) {
        return AjaxResult.success(laoczFireZoneInfoService.getById(fireZoneId));
    }

    /**
     * 新增防火区信息
     */
    @Log(title = "防火区信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody LaoczFireZoneInfo laoczFireZoneInfo) {
        return toAjax(laoczFireZoneInfoService.save(laoczFireZoneInfo));
    }

    /**
     * 修改防火区信息
     */
    @Log(title = "防火区信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody LaoczFireZoneInfo laoczFireZoneInfo) {
        return toAjax(laoczFireZoneInfoService.updateById(laoczFireZoneInfo));
    }

    /**
     * 删除防火区信息
     */
    @Log(title = "防火区信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{fireZoneIds}")
    public AjaxResult remove(@PathVariable Long[] fireZoneIds) {
        return toAjax(laoczFireZoneInfoService.removeByIds(Arrays.asList(fireZoneIds)));
    }

    /**
     * 树状下拉框
     *
     * @return
     */
    @GetMapping("/treePullDown")
    public AjaxResult getTreePullDown() {
        return AjaxResult.success(laoczFireZoneInfoService.selectTreePullDown());
    }

}
