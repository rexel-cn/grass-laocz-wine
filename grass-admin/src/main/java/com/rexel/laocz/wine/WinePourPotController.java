package com.rexel.laocz.wine;

import com.rexel.common.core.domain.AjaxResult;
import com.rexel.laocz.domain.dto.QrInCodeScanDTO;
import com.rexel.laocz.domain.dto.WinBaseDTO;
import com.rexel.laocz.domain.dto.WineEntryDTO;
import com.rexel.laocz.domain.dto.WinePourPotApplyDTO;
import com.rexel.laocz.service.WinePourPotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName WinePourPotController
 * @Description 倒坛 控制器
 * @Author 孟开通
 * @Date 2024/4/2 16:18
 **/
@RestController
@RequestMapping("/rexel-api/winePourPot")
public class WinePourPotController {
    @Autowired
    private WinePourPotService winePourPotService;

    /**
     * 倒坛申请
     *
     * @param winePourPotApplyDTO 倒坛申请参数：申请重量，出酒陶坛罐ID，入酒陶坛罐ID
     */
    @PostMapping("/apply")
    public AjaxResult winePourPotApply(@RequestBody WinePourPotApplyDTO winePourPotApplyDTO) {
        winePourPotService.winePourPotApply(winePourPotApplyDTO);
        return AjaxResult.success();
    }

    /**
     * 倒坛出酒开始获取重量
     *
     * @param wineDetailsId 酒操作业务详情id
     * @return 重量
     */
    @GetMapping("/outStart/{wineDetailsId}")
    public AjaxResult winePourPotOutStart(@PathVariable("wineDetailsId") Long wineDetailsId) {
        Object outWeight = winePourPotService.winePourPotOutStart(wineDetailsId);
        return AjaxResult.success(outWeight);
    }

    /**
     * 倒坛出酒
     *
     * @param winBaseDTO 酒操作业务详情id
     * @return 出酒重量
     */
    @PostMapping("/out")
    public AjaxResult winePourPotOut(@RequestBody WinBaseDTO winBaseDTO) {
        Long outPotteryAltarId = winePourPotService.winePourPotOut(winBaseDTO.getWineDetailsId());
        return AjaxResult.success(outPotteryAltarId);
    }


    /**
     * 倒坛入酒开始
     *
     * @param wineEntryDTO 酒入坛参数
     * @return 入酒的id
     */
    @PostMapping("/inStart")
    public AjaxResult winePourPotInStart(@RequestBody WineEntryDTO wineEntryDTO) {
        winePourPotService.winePourPotInStart(wineEntryDTO);
        return AjaxResult.success();
    }

    /**
     * 倒坛入酒
     *
     * @param winBaseDTO 酒操作业务详情id
     * @return 入酒重量
     */

    @PostMapping("/in")
    public AjaxResult winePourPotIn(@RequestBody WinBaseDTO winBaseDTO) {
        winePourPotService.winePourPotIn(winBaseDTO.getWineDetailsId());
        return AjaxResult.success();
    }


    /**
     * 二维码扫描获取倒坛出酒陶坛信息
     *
     * @param potteryAltarNumber 陶坛编号
     * @return 陶坛信息
     */
    @GetMapping("/qrOutCodeScan/{potteryAltarNumber}")
    public AjaxResult qrOutCodeScan(@PathVariable("potteryAltarNumber") String potteryAltarNumber) {
        return AjaxResult.success(winePourPotService.qrOutCodeScan(potteryAltarNumber));
    }

    /**
     * 二维码扫描获取倒坛入酒陶坛信息
     *
     * @param qrInCodeScanDTO
     * @return 陶坛信息
     */
    @PostMapping("/qrInCodeScan")
    public AjaxResult qrInCodeScan(@RequestBody QrInCodeScanDTO qrInCodeScanDTO) {
        return AjaxResult.success(winePourPotService.qrInCodeScan(qrInCodeScanDTO));
    }

}
