package com.rexel.laocz.wine;

import com.rexel.common.core.controller.BaseController;
import com.rexel.common.core.domain.AjaxResult;
import com.rexel.laocz.domain.dto.WineOutApplyDTO;
import com.rexel.laocz.service.WineOutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName WineOutController
 * @Description 酒品出库
 * @Author 孟开通
 * @Date 2024/3/19 15:42
 **/
@RestController
@RequestMapping("/wineOut")
public class WineOutController extends BaseController {

    @Autowired
    private WineOutService wineOutService;

    @PostMapping("/apply")
    public AjaxResult wineOutApply(@RequestBody List<WineOutApplyDTO> list) {
        wineOutService.wineOutApply(list);
        return AjaxResult.success();
    }

    /**
     * 出酒操作完成
     *
     * @param wineDetailsId 酒操作业务详情id
     * @return 称重量
     */
    @GetMapping("/start/{wineDetailsId}")
    public AjaxResult wineOutStart(@PathVariable("wineDetailsId") Long wineDetailsId) {
        Object value = wineOutService.wineOutStart(wineDetailsId);
        return AjaxResult.success(value);
    }

    /**
     * 出酒操作完成
     *
     * @param wineDetailsId 酒操作业务详情id
     * @return 完成
     */
    @PostMapping("/finish")
    public AjaxResult wineOutFinish(@RequestParam("wineDetailsId") Long wineDetailsId) {
        wineOutService.wineOutFinish(wineDetailsId);
        return AjaxResult.success();
    }
}
