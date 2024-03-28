package com.rexel.laocz.asset;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.rexel.laocz.domain.LaoczPump;
import com.rexel.laocz.domain.dto.PumpAddDto;
import com.rexel.laocz.domain.vo.LaoczPumpVo;
import com.rexel.laocz.domain.vo.PointInfo;
import com.rexel.laocz.service.ILaoczPumpService;
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
}