package com.rexel.laocz.wine;

import com.rexel.common.annotation.RateLimiter;
import com.rexel.common.core.domain.AjaxResult;
import com.rexel.laocz.domain.dto.WinBaseDTO;
import com.rexel.laocz.domain.dto.WineSampApplyDTO;
import com.rexel.laocz.service.WineSampService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName WineSampController
 * @Description 酒取样控制器
 * @Author 孟开通
 * @Date 2024/3/26 17:24
 **/
@RestController
@RequestMapping("/rexel-api/wineSamp")
public class WineSampController {
    @Autowired
    private WineSampService wineSampService;

    /**
     * 酒取样申请
     *
     * @param wineSampApplyDTO 酒取样申请
     */
    @PostMapping("/apply")
    @RateLimiter(time = 2, count = 1)
    public AjaxResult wineSampApply(@RequestBody WineSampApplyDTO wineSampApplyDTO) {
        wineSampService.wineSampApply(wineSampApplyDTO);
        return AjaxResult.success();
    }

    /**
     * 酒取样完成
     *
     * @param winBaseDTO 酒操作业务详情id
     */
    @PostMapping("/finish")
    @RateLimiter(time = 2, count = 1)
    public AjaxResult wineSampFinish(@RequestBody WinBaseDTO winBaseDTO) {
        wineSampService.wineSampFinish(winBaseDTO.getWineDetailsId());
        return AjaxResult.success();
    }


    /**
     * 二维码扫描获取取样陶坛信息
     *
     * @param potteryAltarNumber 陶坛编号
     * @return 陶坛信息
     */
    @GetMapping("/qrCodeScan/{potteryAltarNumber}")
    public AjaxResult qrCodeScan(@PathVariable("potteryAltarNumber") String potteryAltarNumber) {
        return AjaxResult.success(wineSampService.qrCodeScan(potteryAltarNumber));
    }

}
