package com.rexel.laocz.asset;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.rexel.laocz.domain.LaoczPump;
import com.rexel.laocz.domain.vo.LaoczPumpVo;
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
        return getDataTable(list,"laoczPump");
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
    public AjaxResult add(@RequestBody LaoczPump laoczPump) {
        return toAjax(laoczPumpService.save(laoczPump));
    }

    /**
     * 修改泵管理
     */
    @Log(title = "泵管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody LaoczPump laoczPump) {
        return toAjax(laoczPumpService.updateById(laoczPump));
    }

    /**
     * 删除泵管理
     */
    @Log(title = "泵管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{pumpIds}")
    public AjaxResult remove(@PathVariable Long[] pumpIds) {
        return toAjax(laoczPumpService.removeByIds(Arrays.asList(pumpIds)));
    }

    /**
     * 绑定测点详情
     */
    @GetMapping("/getPointInfo")
    public AjaxResult getPointInfo(Long pumpId) {

        //laoczPumpService.getByIdWithPonitInfo();

        return null;
    }
    /**
     * 查询泵管理详情
     */
    @GetMapping("/getPumpDetail")
    public AjaxResult getPumpDetail(Long pumpId){
       LaoczPumpVo laoczPumpVo =  laoczPumpService.getPumpDetail(pumpId);
        return AjaxResult.success(laoczPumpVo);

    }
}