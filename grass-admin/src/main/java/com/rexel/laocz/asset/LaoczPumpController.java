package com.rexel.laocz.asset;

import com.rexel.common.annotation.Log;
import com.rexel.common.annotation.RateLimiter;
import com.rexel.common.core.controller.BaseController;
import com.rexel.common.core.domain.AjaxResult;
import com.rexel.common.core.page.TableDataInfo;
import com.rexel.common.enums.BusinessType;
import com.rexel.common.utils.poi.ExcelUtil;
import com.rexel.laocz.domain.LaoczPump;
import com.rexel.laocz.domain.dto.PumpAddDto;
import com.rexel.laocz.domain.dto.PumpImportDto;
import com.rexel.laocz.domain.vo.LaoczPumpVo;
import com.rexel.laocz.domain.vo.PointInfo;
import com.rexel.laocz.service.ILaoczPumpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 泵管理Controller
 *
 * @author grass-service
 * @date 2024-03-21
 */
@RestController
@RequestMapping("/rexel-api/pump")
public class LaoczPumpController extends BaseController {
    @Autowired
    private ILaoczPumpService laoczPumpService;

    /**
     * 查询泵管理列表
     */
    @GetMapping("/list")
    public TableDataInfo list(LaoczPump laoczPump) {
        startPage();
        List<LaoczPumpVo> list = laoczPumpService.selectLaoczPumpList(laoczPump);
        return getDataTable(list, "laoczPump");
    }

    /**
     * 获取泵管理详细信息
     */
    @GetMapping(value = "/{pumpId}")
    public AjaxResult getInfo(@PathVariable("pumpId") Long pumpId) {
        return AjaxResult.success(laoczPumpService.getById(pumpId));
    }

    /**
     * 新增泵管理
     */
    @Log(title = "泵管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult addPump(@RequestBody PumpAddDto pumpAddDto) {
        return toAjax(laoczPumpService.addPump(pumpAddDto));
    }

    /**
     * 修改泵管理
     */
    @Log(title = "泵管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PumpAddDto pumpAddDto) {
        return toAjax(laoczPumpService.updateByIdWithPump(pumpAddDto));
    }

    /**
     * 删除泵管理以及泵相关测点
     */
    @Log(title = "泵管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{pumpId}")
    public AjaxResult remove(@PathVariable Long pumpId) {
        return toAjax(laoczPumpService.removeByIdWithPoint(pumpId));
    }

    /**
     * 获取绑定测点详情
     */
    @GetMapping("/getPointInfo")
    public AjaxResult getPointInfo(Long pumpId) {
        List<PointInfo> pointInfos = laoczPumpService.getPointInfo(pumpId);
        return AjaxResult.success(pointInfos);
    }

    /**
     * 查询泵管理详情
     */
    @GetMapping("/getPumpDetail")
    public AjaxResult getPumpDetail(Long pumpId) {
        PumpAddDto pumpAddDto = laoczPumpService.getPumpDetail(pumpId);
        return AjaxResult.success(pumpAddDto);

    }

    /**
     * 导出泵管理模板
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response) throws IOException {
        ExcelUtil<PumpImportDto> util = new ExcelUtil<>(PumpImportDto.class);
        util.exportExcel(response, new ArrayList<>(), "泵管理");
    }

    /**
     * 导入泵管理列表
     */
    @PostMapping("/import")
    @RateLimiter(time = 2, count = 1)
    public AjaxResult importWeighingTank(@RequestParam("file") MultipartFile file) throws Exception {
        ExcelUtil<PumpImportDto> util = new ExcelUtil<>(PumpImportDto.class);
        List<PumpImportDto> PumpImportDtos = util.importExcel(file.getInputStream());
        return AjaxResult.success(laoczPumpService.importPump(PumpImportDtos));
    }
}
