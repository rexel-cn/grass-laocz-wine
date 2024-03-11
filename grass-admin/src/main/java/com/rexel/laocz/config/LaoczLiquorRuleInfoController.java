package com.rexel.laocz.config;

import java.util.Arrays;
import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rexel.laocz.domain.LaoczLiquorRuleInfo;
import com.rexel.laocz.service.ILaoczLiquorRuleInfoService;
import com.rexel.laocz.vo.LiquorRuleInfoVo;
import com.rexel.laocz.vo.UserInfoVo;
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
 * 酒液批次存储报警规则信息Controller
 *
 * @author grass-service
 * @date 2024-03-07
 */
@RestController
@RequestMapping("/rexel-api/liquorRule")
public class LaoczLiquorRuleInfoController extends BaseController {
    @Autowired
    private ILaoczLiquorRuleInfoService laoczLiquorRuleInfoService;

    /**
     * 查询酒液批次存储报警规则信息列表
     */
    @GetMapping("/list")
    public TableDataInfo list(LaoczLiquorRuleInfo laoczLiquorRuleInfo) {
        startPage();
        List<LiquorRuleInfoVo> list = laoczLiquorRuleInfoService.selectLaoczLiquorRuleInfoList(laoczLiquorRuleInfo);
        return getDataTable(list);
    }

    /**
     * 获取酒液批次存储报警规则信息详细信息
     */
    @GetMapping(value = "/{liquorRuleId}")
    public AjaxResult getInfo(@PathVariable("liquorRuleId") Long liquorRuleId) {
        return AjaxResult.success(laoczLiquorRuleInfoService.getById(liquorRuleId));
    }

    /**
     * 新增酒液批次存储报警规则信息
     */
    @Log(title = "酒液批次存储报警规则信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody LaoczLiquorRuleInfo laoczLiquorRuleInfo) {
        return toAjax(laoczLiquorRuleInfoService.save(laoczLiquorRuleInfo));
    }

    /**
     * 修改酒液批次存储报警规则信息
     */
    @Log(title = "酒液批次存储报警规则信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody LaoczLiquorRuleInfo laoczLiquorRuleInfo) {
        return toAjax(laoczLiquorRuleInfoService.updateById(laoczLiquorRuleInfo));
    }

    /**
     * 删除酒液批次存储报警规则信息
     */
    @Log(title = "酒液批次存储报警规则信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{liquorRuleIds}")
    public AjaxResult remove(@PathVariable Long[] liquorRuleIds) {
        return toAjax(laoczLiquorRuleInfoService.removeByIds(Arrays.asList(liquorRuleIds)));
    }


    /**
     * 根据id查询通知人员信息
     * @param id 酒液批次报警ID
     * @return 用户信息
     */
    @GetMapping("/getByIdWithUserInfo/{id}")
    public TableDataInfo get(@PathVariable Long id){
        startPage();
        List<UserInfoVo> userInfos = laoczLiquorRuleInfoService.getByIdWithUserInfo(id);
        return getDataTable(userInfos,"byIdUserInfo");
    }
}
