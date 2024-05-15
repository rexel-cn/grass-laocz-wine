package com.rexel.laocz.wine;

import com.rexel.common.annotation.RateLimiter;
import com.rexel.common.core.controller.BaseController;
import com.rexel.common.core.domain.AjaxResult;
import com.rexel.laocz.domain.dto.WinBaseDTO;
import com.rexel.laocz.domain.dto.WineEntryApplyDTO;
import com.rexel.laocz.domain.dto.WineEntryDTO;
import com.rexel.laocz.service.WineEntryApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName WineEntryController
 * @Description 入酒 控制器
 * @Author 孟开通
 * @Date 2024/3/11 16:55
 **/
@RestController
@RequestMapping("/rexel-api/wineEntry")
public class WineEntryController extends BaseController {

    @Autowired
    private WineEntryApplyService wineEntryApplyService;

    /**
     * 获取酒品批次号
     *
     * @return
     */
    @GetMapping("/getLiquorBatchId")
    public AjaxResult getLiquorBatchId() {
        Object liquorBatchId = wineEntryApplyService.getLiquorBatchId();
        return AjaxResult.success(liquorBatchId);
    }

    /**
     * 入酒申请
     *
     * @param wineEntryApplyDTO 入酒申请参数：申请重量，陶坛罐ID，酒品管理ID，酒品批次号
     * @return 申请结果
     */
    @PostMapping("/wineEntryApply")
    @RateLimiter(time = 2, count = 1)
    public AjaxResult wineEntryApply(@RequestBody @Validated WineEntryApplyDTO wineEntryApplyDTO) {
        wineEntryApplyService.wineEntryApply(wineEntryApplyDTO);
        return AjaxResult.success();
    }

    /**
     * 入酒操作
     *
     * @param wineEntryDTO 入酒参数：操作类型 1:开始 2:急停 3:继续
     * @return 操作结果
     */
    @PostMapping("/wineEntry")
    @RateLimiter(time = 1, count = 1)
    public AjaxResult wineEntry(@RequestBody WineEntryDTO wineEntryDTO) {
        wineEntryApplyService.wineEntry(wineEntryDTO);
        return AjaxResult.success();
    }


    /**
     * 查询入酒出酒当前实时数据
     *
     * @param wineDetailsId 酒操作业务详情id
     * @return 入酒出酒实时数据
     */
    @GetMapping("/getWineRealData/{wineDetailsId}")
    public AjaxResult getWineRealData(@PathVariable("wineDetailsId") Long wineDetailsId) {
        return AjaxResult.success(wineEntryApplyService.getWineRealData(wineDetailsId));
    }


    /**
     * 入酒结束
     *
     * @param winBaseDTO 酒操作业务详情id
     * @return 结束结果
     */
    @PostMapping("/wineEntryFinish")
    @RateLimiter(time = 2, count = 1)
    public AjaxResult wineEntryFinish(@RequestBody WinBaseDTO winBaseDTO) {
        wineEntryApplyService.wineEntryFinish(winBaseDTO.getWineDetailsId());
        return AjaxResult.success();
    }


    /**
     * 二维码扫描获取入酒陶坛信息
     *
     * @param potteryAltarNumber 陶坛编号
     * @return 陶坛信息
     */
    @GetMapping("/qrCodeScan/{potteryAltarNumber}")
    public AjaxResult qrCodeScan(@PathVariable("potteryAltarNumber") String potteryAltarNumber) {
        return AjaxResult.success(wineEntryApplyService.qrCodeScan(potteryAltarNumber));
    }


    /**
     * 自动选择陶坛
     *
     * @param applyWeight 申请重量
     * @return 陶坛列表
     */
    @GetMapping("/automaticChoosePotteryAltar/{applyWeight}")
    public AjaxResult automaticChoosePotteryAltar(@PathVariable("applyWeight") Double applyWeight) {
        return AjaxResult.success(wineEntryApplyService.automaticChoosePotteryAltar(applyWeight));
    }

}
