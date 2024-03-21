package com.rexel.laocz.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletResponse;


import com.rexel.common.utils.poi.ExcelUtil;
import com.rexel.laocz.domain.LaoczLiquorManagement;
import com.rexel.laocz.domain.vo.LiquorVo;
import com.rexel.laocz.service.ILaoczLiquorManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.rexel.common.annotation.Log;
import com.rexel.common.core.controller.BaseController;
import com.rexel.common.core.domain.AjaxResult;
import com.rexel.common.enums.BusinessType;
import com.rexel.common.core.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 酒品管理Controller
 *
 * @author grass-service
 * @date 2024-03-07
 */
@RestController
@RequestMapping("/rexel-api/liquor")
public class LaoczLiquorManagementController extends BaseController {
    @Autowired
    private ILaoczLiquorManagementService laoczLiquorManagementService;

    /**
     * 查询酒品管理列表
     */
    @GetMapping("/list")
    public TableDataInfo list(LaoczLiquorManagement laoczLiquorManagement) {
        startPage();
        List<LaoczLiquorManagement> list = laoczLiquorManagementService.selectLaoczLiquorManagementList(laoczLiquorManagement);
        return getDataTable(list,"jiupin");
    }

    /**
     * 获取酒品管理详细信息
     */
    @GetMapping(value = "/{liquorManagementId}")
    public AjaxResult getInfo(@PathVariable("liquorManagementId") Long liquorManagementId) {
        return AjaxResult.success(laoczLiquorManagementService.getById(liquorManagementId));
    }

    /**
     * 新增酒品管理
     */
    @Log(title = "酒品管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody LaoczLiquorManagement laoczLiquorManagement) {
        return toAjax(laoczLiquorManagementService.save(laoczLiquorManagement));
    }

    /**
     * 修改酒品管理
     */
    @Log(title = "酒品管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody LaoczLiquorManagement laoczLiquorManagement) {
        return toAjax(laoczLiquorManagementService.updateById(laoczLiquorManagement));
    }

    /**
     * 删除酒品管理
     */
    @Log(title = "酒品管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{liquorManagementIds}")
    public AjaxResult remove(@PathVariable Long[] liquorManagementIds) {
        return toAjax(laoczLiquorManagementService.removeByIds(Arrays.asList(liquorManagementIds)));
    }

    /**
     * 酒品管理模板下载
     * @param response
     * @throws IOException
     */
    @PostMapping("/template")
    public void template(HttpServletResponse response) throws IOException {
        ExcelUtil<LiquorVo> util = new ExcelUtil<>(LiquorVo.class);
        util.exportExcel(response, new ArrayList<>(), "酒品管理");
    }

    /**
     * 酒品管理批量导入
     * @param file
     * @return
     * @throws Exception
     */
    @PostMapping("/import")
    public AjaxResult importLiquor(@RequestParam("file") MultipartFile file) throws Exception {
        ExcelUtil<LiquorVo> util = new ExcelUtil<>(LiquorVo.class);
        List<LiquorVo> LiquorVos = util.importExcel(file.getInputStream());
        return AjaxResult.success(laoczLiquorManagementService.importPoint(LiquorVos));
    }
}
