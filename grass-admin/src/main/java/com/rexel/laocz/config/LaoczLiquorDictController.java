package com.rexel.laocz.config;

import java.util.Arrays;
import java.util.List;

import com.rexel.laocz.domain.LaoczLiquorDict;
import com.rexel.laocz.service.ILaoczLiquorDictService;
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
import com.rexel.common.core.page.TableDataInfo;

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
    public TableDataInfo list(LaoczLiquorDict laoczLiquorDict) {
        startPage();
        List<LaoczLiquorDict> list = laoczLiquorDictService.selectLaoczLiquorDictList(laoczLiquorDict);
        return getDataTable(list);
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
    @PostMapping
    public AjaxResult add(@RequestBody LaoczLiquorDict laoczLiquorDict) {
        return toAjax(laoczLiquorDictService.save(laoczLiquorDict));
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

}
