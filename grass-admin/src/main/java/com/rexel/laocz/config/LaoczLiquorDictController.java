package com.rexel.laocz.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rexel.common.utils.poi.ExcelUtil;
import com.rexel.laocz.domain.LaoczLiquorDict;
import com.rexel.laocz.domain.vo.LiquorVo;
import com.rexel.laocz.service.ILaoczLiquorDictService;
import com.rexel.system.domain.vo.PointTagExportVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import com.rexel.common.annotation.Log;
import com.rexel.common.core.controller.BaseController;
import com.rexel.common.core.domain.AjaxResult;
import com.rexel.common.enums.BusinessType;
import com.rexel.common.core.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * 酒品字典Controller
 *
 * @author grass-service
 * @date 2024-03-07
 */
@RestController
@RequestMapping("/rexel-api/liquorDict")
public class LaoczLiquorDictController extends BaseController {
    @Autowired
    private ILaoczLiquorDictService laoczLiquorDictService;

    /**
     * 查询酒品字典列表
     */
    @GetMapping("/list")
    public AjaxResult list(LaoczLiquorDict laoczLiquorDict) {
        List<LaoczLiquorDict> list = laoczLiquorDictService.selectLaoczLiquorDictList(laoczLiquorDict);
        return AjaxResult.success(list);
    }

    /**
     * 获取酒品字典详细信息
     */
    @GetMapping(value = "/{liquorDictId}")
    public AjaxResult getInfo(@PathVariable("liquorDictId") Long liquorDictId) {
        return AjaxResult.success(laoczLiquorDictService.getById(liquorDictId));
    }

    /**
     * 新增酒品字典
     */
    @Log(title = "酒品字典", businessType = BusinessType.INSERT)
    @Transactional(rollbackFor = Exception.class)
    @PostMapping
    public AjaxResult add(@RequestBody List<LaoczLiquorDict> laoczLiquorDicts) {

        return toAjax(laoczLiquorDictService.addLiquorDict(laoczLiquorDicts));
    }

    /**
     * 修改酒品字典
     */
    @Log(title = "酒品字典", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody LaoczLiquorDict laoczLiquorDict) {
        return toAjax(laoczLiquorDictService.updateById(laoczLiquorDict));
    }

    /**
     * 删除酒品字典
     */
    @Log(title = "酒品字典", businessType = BusinessType.DELETE)
    @DeleteMapping("/{liquorDictIds}")
    public AjaxResult remove(@PathVariable Long[] liquorDictIds) {
        return toAjax(laoczLiquorDictService.removeByIds(Arrays.asList(liquorDictIds)));
    }

    /**
     * 酒液等级 香型名称 下拉
     *
     * @return
     */
    @GetMapping("/dropDown")
    public AjaxResult dropDownLiquor(LaoczLiquorDict laoczLiquorDict) {
        return AjaxResult.success(laoczLiquorDictService.dropDownLiquor(laoczLiquorDict));
    }



}
