package com.rexel.web.controller.webconf;


import com.rexel.common.annotation.Log;
import com.rexel.common.core.controller.BaseController;
import com.rexel.common.core.domain.AjaxResult;
import com.rexel.common.core.page.TableDataInfo;
import com.rexel.common.enums.BusinessType;
import com.rexel.system.domain.GrassWebConf;
import com.rexel.system.service.IGrassWebConfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 工艺组态地址信息Controller
 *
 * @author grass-service
 * @date 2022-07-18
 */
@RestController
@RequestMapping("/rexel-api/web-config")
public class GrassWebConfController extends BaseController {
    @Autowired
    private IGrassWebConfService grassWebConfService;

    /**
     * 查询工艺组态地址信息列表
     */
    @GetMapping("/list")
    public TableDataInfo list(GrassWebConf grassWebConf) {
        startPage();
        List<GrassWebConf> list = grassWebConfService.selectGrassWebConfList(grassWebConf);
        return getDataTable(list, "grassWebConf-table");
    }
    /**
     * 查询工艺组态地址信息列表
     *
     * @param grassWebConf grassWebConf
     * @return AjaxResult
     */
    @PostMapping("/listAll")
    public AjaxResult listAll(@RequestBody GrassWebConf grassWebConf) {
        List<GrassWebConf> list = grassWebConfService.selectGrassWebConfList(grassWebConf);
        return AjaxResult.success(list);
    }


    /**
     * 获取工艺组态地址信息详细信息
     */
    @PostMapping(value = "/getInfo")
    public AjaxResult getInfo(@RequestBody GrassWebConf grassWebConf) {
        return AjaxResult.success(grassWebConfService.getById(grassWebConf.getId()));
    }

    /**
     * 新增工艺组态地址信息
     */
    @Log(title = "工艺组态地址信息", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@RequestBody GrassWebConf grassWebConf) {
        return toAjax(grassWebConfService.save(grassWebConf));
    }

    /**
     * 修改工艺组态地址信息
     */
    @Log(title = "工艺组态地址信息", businessType = BusinessType.UPDATE)
    @PutMapping("/edit")
    public AjaxResult edit(@RequestBody GrassWebConf grassWebConf) {
        return toAjax(grassWebConfService.updateById(grassWebConf));
    }

    /**
     * 删除工艺组态地址信息
     */
    @Log(title = "工艺组态地址信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/remove")
    public AjaxResult remove(@RequestBody List<GrassWebConf> grassWebConf) {
        List<Long> collect = grassWebConf.stream().map(GrassWebConf::getId).collect(Collectors.toList());
        return toAjax(grassWebConfService.removeByIds(collect));
    }
}
