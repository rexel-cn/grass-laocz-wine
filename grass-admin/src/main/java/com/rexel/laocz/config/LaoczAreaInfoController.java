package com.rexel.laocz.config;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.rexel.laocz.domain.LaoczAreaInfo;
import com.rexel.laocz.domain.LaoczFireZoneInfo;
import com.rexel.laocz.domain.vo.FireZoneInfoVo;
import com.rexel.laocz.service.ILaoczAreaInfoService;
import org.springframework.security.access.prepost.PreAuthorize;
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
 * 场区信息Controller
 *
 * @author grass-service
 * @date 2024-03-06
 */
@RestController
@RequestMapping("/rexel-api/area")
public class LaoczAreaInfoController extends BaseController {
    @Autowired
    private ILaoczAreaInfoService laoczAreaInfoService;

    /**
     * 查询场区信息列表
     */
    @GetMapping("/list")
    public TableDataInfo list(LaoczAreaInfo laoczAreaInfo) {
        startPage();
        List<LaoczAreaInfo> list = laoczAreaInfoService.selectLaoczAreaInfoList(laoczAreaInfo);
        return getDataTable(list, "area");
    }

    /**
     * 获取场区信息详细信息
     */
    @GetMapping(value = "/{areaId}")
    public AjaxResult getInfo(@PathVariable("areaId") Long areaId) {
        return AjaxResult.success(laoczAreaInfoService.getById(areaId));
    }

    /**
     * 新增场区信息
     */
    @Log(title = "场区信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody LaoczAreaInfo laoczAreaInfo) {
        return toAjax(laoczAreaInfoService.save(laoczAreaInfo));
    }

    /**
     * 修改场区信息
     */
    @Log(title = "场区信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody LaoczAreaInfo laoczAreaInfo) {
        return toAjax(laoczAreaInfoService.updateById(laoczAreaInfo));
    }

    /**
     * 删除场区信息
     */
    @Log(title = "场区信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{areaIds}")
    public AjaxResult remove(@PathVariable Long[] areaIds) {
        return toAjax(laoczAreaInfoService.removeByIds(Arrays.asList(areaIds)));
    }

    /**
     * 导出场区信息列表
     */
    @Log(title = "场区信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, LaoczAreaInfo laoczAreaInfo) throws IOException {
        List<LaoczAreaInfo> list = laoczAreaInfoService.selectLaoczAreaInfoList(laoczAreaInfo);
        ExcelUtil<LaoczAreaInfo> util = new ExcelUtil<>(LaoczAreaInfo.class);
        util.exportExcel(response, list, "场区信息数据");
    }

    /**
     * 场区下拉
     *
     * @return
     */
    @GetMapping("/dropDown")
    public AjaxResult dropDown() {
        return AjaxResult.success(laoczAreaInfoService.dropDown());
    }

    /**
     * 通过场区Id查询防火区信息
     */
    @GetMapping("/get/{id}")
    public AjaxResult get(@PathVariable Long id) {
        List<LaoczFireZoneInfo> laoczFireZoneInfos = laoczAreaInfoService.getByIdWithfireZoneName(id);
        return AjaxResult.success(laoczFireZoneInfos);
    }
}
