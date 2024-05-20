package com.rexel.laocz.config;


import com.rexel.common.annotation.Log;
import com.rexel.common.core.controller.BaseController;
import com.rexel.common.core.domain.AjaxResult;
import com.rexel.common.enums.BusinessType;
import com.rexel.laocz.domain.LaoczLiquorDict;
import com.rexel.laocz.service.ILaoczLiquorDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 酒品字典Controller
 *
 * @author grass-service
 * @date 2024-03-07
 */
@RestController
@RequestMapping("/rexel-api/liquorDict")
@Validated
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
    public AjaxResult add(@RequestBody @Valid List<LaoczLiquorDict> laoczLiquorDicts) {
        return toAjax(laoczLiquorDictService.addLiquorDict(laoczLiquorDicts));
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
