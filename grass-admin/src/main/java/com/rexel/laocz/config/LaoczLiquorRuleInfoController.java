package com.rexel.laocz.config;

import com.rexel.common.annotation.Log;
import com.rexel.common.core.controller.BaseController;
import com.rexel.common.core.domain.AjaxResult;
import com.rexel.common.core.page.TableDataInfo;
import com.rexel.common.enums.BusinessType;
import com.rexel.laocz.domain.LaoczLiquorRuleInfo;
import com.rexel.laocz.domain.vo.LiquorRuleInfoVo;
import com.rexel.laocz.domain.vo.UserInfoVo;
import com.rexel.laocz.service.ILaoczLiquorRuleInfoService;
import com.rexel.laocz.service.ILaoczWineOperationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

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
        return getDataTable(list,"liquorRule");
    }

    /**
     * 获取酒液批次存储报警规则信息详细信息
     */
    @GetMapping(value = "/{liquorRuleId}")
    public AjaxResult getInfo(@PathVariable("liquorRuleId") Long liquorRuleId) {
        return AjaxResult.success(laoczLiquorRuleInfoService.getById(liquorRuleId));
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
     *
     * @param id 酒液批次报警ID
     * @return 用户信息
     */
    @GetMapping("/getByIdWithUserInfo/{id}")
    public TableDataInfo get(@PathVariable Long id) {
        startPage();
        List<UserInfoVo> userInfos = laoczLiquorRuleInfoService.getByIdWithUserInfo(id);
        return getDataTable(userInfos);
    }

    /**
     * 新增报警规则
     *
     * @param laoczLiquorRuleInfo 报警规则
     * @return
     */
    @PostMapping
    public AjaxResult save(@RequestBody LaoczLiquorRuleInfo laoczLiquorRuleInfo) {
        try {
            laoczLiquorRuleInfoService.saveWithRule(laoczLiquorRuleInfo);
            return AjaxResult.success();
        } catch (Exception e) {
            return AjaxResult.error();
        }
    }

    /**
     * 修改报警规则
     * @param laoczLiquorRuleInfo 报警规则
     * @return
     */
    @PutMapping
    public AjaxResult edit(@RequestBody LaoczLiquorRuleInfo laoczLiquorRuleInfo){
        try {
            laoczLiquorRuleInfoService.updateWithRule(laoczLiquorRuleInfo);
            return AjaxResult.success();
        } catch (Exception e) {
            return AjaxResult.error();
        }
    }

    /**
     * 酒液批次下拉
     * @return
     */
    @GetMapping("/dropDown")
    public AjaxResult dropDown(){
        return AjaxResult.success(laoczLiquorRuleInfoService.dropDown());
    }
}
