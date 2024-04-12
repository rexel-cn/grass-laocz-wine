package com.rexel.laocz.wine;

import com.rexel.common.core.controller.BaseController;
import com.rexel.common.core.domain.AjaxResult;
import com.rexel.laocz.domain.dto.MatterDetailDTO;
import com.rexel.laocz.domain.dto.WineEntryApplyParamDTO;
import com.rexel.laocz.domain.dto.WineOperationDTO;
import com.rexel.laocz.service.ILaoczWineOperationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName WineController
 * @Description 酒操作共用，控制器
 * @Author 孟开通
 * @Date 2024/3/13 13:55
 **/
@RestController
@RequestMapping("/rexel-api/wine")
public class WineController extends BaseController {

    @Autowired
    private ILaoczWineOperationsService iLaoczWineOperationsService;

    /**
     * 获取我的事项
     *
     * @return 我的事项列表
     */
    @PostMapping("/getMatterVOList")
    public AjaxResult getMatterVOList(@RequestBody WineOperationDTO wineOperationDTO) {
        startPage();
        return AjaxResult.success(iLaoczWineOperationsService.getMatterVOList(wineOperationDTO));
    }

    /**
     * 获取我的事项详情
     *
     * @param matterDetailDTO 酒操作业务表 主键 和 区域id 和 防火区Id
     * @return
     */
    @PostMapping("/getMatterDetailVOList")
    public AjaxResult getMatterDetailVOList(@RequestBody MatterDetailDTO matterDetailDTO) {
        return AjaxResult.success(iLaoczWineOperationsService.getMatterDetailVOList(matterDetailDTO));
    }

    /**
     * 获取入酒详情
     *
     * @param wineDetailsId 酒业务操作详情id
     * @return
     */
    @GetMapping("/getWineDetailVO/{wineDetailsId}")
    public AjaxResult getWineDetailVO(@PathVariable("wineDetailsId") Long wineDetailsId) {
        return AjaxResult.success(iLaoczWineOperationsService.getWineDetailVO(wineDetailsId));
    }

    /**
     * 设置称重罐
     *
     * @param weighingTank 称重罐
     * @return
     */
    @PostMapping("/setWeighingTank")
    public AjaxResult setWeighingTank(@RequestBody WineEntryApplyParamDTO weighingTank) {
        return AjaxResult.success(iLaoczWineOperationsService.setWeighingTank(weighingTank));
    }
}
