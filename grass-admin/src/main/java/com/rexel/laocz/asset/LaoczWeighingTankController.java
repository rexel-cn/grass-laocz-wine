package com.rexel.laocz.asset;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletResponse;


import com.rexel.laocz.domain.LaoczWeighingTank;
import com.rexel.laocz.domain.dto.WeighingTankDto;
import com.rexel.laocz.domain.vo.LiquorVo;
import com.rexel.laocz.domain.vo.WeighingTankVo;
import com.rexel.laocz.service.ILaoczWeighingTankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.rexel.common.annotation.Log;
import com.rexel.common.core.controller.BaseController;
import com.rexel.common.core.domain.AjaxResult;
import com.rexel.common.enums.BusinessType;
import com.rexel.common.utils.poi.ExcelUtil;
import com.rexel.common.core.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 称重罐管理Controller
 *
 * @author grass-service
 * @date 2024-03-20
 */
@RestController
@RequestMapping("/rexel-api/tank")
public class LaoczWeighingTankController extends BaseController {
    @Autowired
    private ILaoczWeighingTankService laoczWeighingTankService;

    /**
     * 查询称重罐管理列表
     */
    @GetMapping("/list")
    public TableDataInfo list(LaoczWeighingTank laoczWeighingTank) {
        startPage();
        List<LaoczWeighingTank> list = laoczWeighingTankService.selectLaoczWeighingTankList(laoczWeighingTank);
        return getDataTable(list);
    }

    /**
     * 查询称重罐管理列表详细信息
     */
    @GetMapping("/listDetail")
    public TableDataInfo listDetail(LaoczWeighingTank laoczWeighingTank) {
        startPage();
        List<WeighingTankVo> list = laoczWeighingTankService.selectLaoczWeighingTankListDetail(laoczWeighingTank);
        return getDataTable(list);
    }

    /**
     * 获取称重罐管理详细信息
     */
    @GetMapping(value = "/{weighingTankId}")
    public AjaxResult getInfo(@PathVariable("weighingTankId") Long weighingTankId) {
        return AjaxResult.success(laoczWeighingTankService.getByIdWithTank(weighingTankId));
    }

    /**
     * 新增称重罐管理
     */
    @Log(title = "称重罐管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody LaoczWeighingTank laoczWeighingTank) {
        return toAjax(laoczWeighingTankService.addWeighingTank(laoczWeighingTank));
    }

    /**
     * 修改称重罐管理
     */
    @Log(title = "称重罐管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody LaoczWeighingTank laoczWeighingTank) {
        return toAjax(laoczWeighingTankService.updateByIdWithWeighingTank(laoczWeighingTank));
    }

    /**
     * 删除称重罐管理
     */
    @Log(title = "称重罐管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{weighingTankIds}")
    public AjaxResult remove(@PathVariable Long[] weighingTankIds) {
        return toAjax(laoczWeighingTankService.removeByIds(Arrays.asList(weighingTankIds)));
    }

    /**
     * 导出称重罐管理列表
     */
    @Log(title = "称重罐管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response) throws IOException {
        ExcelUtil<WeighingTankDto> util = new ExcelUtil<>(WeighingTankDto.class);
        util.exportExcel(response, new ArrayList<>(), "称重罐管理");
    }

    /**
     * 导入称重罐管理列表
     */
    @PostMapping("/import")
    public AjaxResult importWeighingTank(@RequestParam("file") MultipartFile file) throws Exception {
        ExcelUtil<WeighingTankDto> util = new ExcelUtil<>(WeighingTankDto.class);
        List<WeighingTankDto> weighingTankVos = util.importExcel(file.getInputStream());
        return AjaxResult.success(laoczWeighingTankService.importWeighingTank(weighingTankVos));
    }
}
