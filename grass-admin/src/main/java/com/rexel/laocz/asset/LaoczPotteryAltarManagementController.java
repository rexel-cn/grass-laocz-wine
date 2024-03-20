package com.rexel.laocz.asset;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rexel.common.core.controller.BaseController;
import com.rexel.common.core.domain.AjaxResult;
import com.rexel.common.core.page.TableDataInfo;
import com.rexel.laocz.domain.LaoczPotteryAltarManagement;
import com.rexel.laocz.domain.vo.PotteryAltarVo;
import com.rexel.laocz.service.ILaoczFireZoneInfoService;
import com.rexel.laocz.service.ILaoczPotteryAltarManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName LaoczPotteryAltarManagementController
 * @Description 陶坛管理
 * @Author 孟开通
 * @Date 2024/3/12 17:50
 **/
@RestController
@RequestMapping("/rexel-api/potteryAltar")
public class LaoczPotteryAltarManagementController extends BaseController {

    @Autowired
    private ILaoczPotteryAltarManagementService iLaoczPotteryAltarManagementService;


    /**
     * 查询陶坛管理列表
     */
    @GetMapping("/list")
    public TableDataInfo list(LaoczPotteryAltarManagement laoczPotteryAltarManagement) {
        startPage();
        List<LaoczPotteryAltarManagement> list = iLaoczPotteryAltarManagementService.selectLaoczPotteryAltarManagementList(laoczPotteryAltarManagement);
        return getDataTable(list);
    }

    /**
     * 查询陶坛管理列表详细信息
     *
     * @param laoczPotteryAltarManagement
     * @return
     */
    @GetMapping("listDetail")
    public TableDataInfo listDetail(LaoczPotteryAltarManagement laoczPotteryAltarManagement) {
        startPage();
        List<PotteryAltarVo> list = iLaoczPotteryAltarManagementService.selectLaoczPotteryAltarManagementListDetail(laoczPotteryAltarManagement);
        return getDataTable(list, "potteryAltar");
    }

    /**
     * 通过Id删除陶坛管理
     */
    @DeleteMapping("/{potteryAltarId}")
    public AjaxResult remove(@PathVariable Long potteryAltarId) {

        return toAjax(iLaoczPotteryAltarManagementService.removeById(potteryAltarId));
    }

    /**
     * 编辑回显,通过Id查询陶坛管理详情
     */
    @GetMapping("/{potteryAltarId}")
    public AjaxResult byIdWithPottery(@PathVariable Long potteryAltarId) {
        PotteryAltarVo potteryAltarVo = iLaoczPotteryAltarManagementService.selectLaoczPotteryAltarManagement(potteryAltarId);
        return AjaxResult.success(potteryAltarVo);
    }

    /**
     * 修改陶坛
     *
     * @param laoczPotteryAltarManagement
     * @return
     */
    @PutMapping
    public AjaxResult edit(@RequestBody LaoczPotteryAltarManagement laoczPotteryAltarManagement) {
        return toAjax(iLaoczPotteryAltarManagementService.updateByIdWithPotteryAltar(laoczPotteryAltarManagement));
    }

    /**
     * 新增陶坛
     *
     * @param laoczPotteryAltarManagement
     * @return
     */
    @PostMapping
    public AjaxResult add(@RequestBody LaoczPotteryAltarManagement laoczPotteryAltarManagement) {

            return toAjax(iLaoczPotteryAltarManagementService.addPotteryAltar(laoczPotteryAltarManagement));
    }
}
