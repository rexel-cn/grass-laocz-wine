package com.rexel.laocz.wine;

import com.rexel.common.annotation.RateLimiter;
import com.rexel.common.core.controller.BaseController;
import com.rexel.common.core.domain.AjaxResult;
import com.rexel.common.core.page.TableDataInfo;
import com.rexel.laocz.domain.dto.MatterDetailDTO;
import com.rexel.laocz.domain.dto.QrCodeScanMatterDetailDTO;
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
    public TableDataInfo getMatterVOList(@RequestBody WineOperationDTO wineOperationDTO) {
        startPage();
        return getDataTable(iLaoczWineOperationsService.getMatterVOList(wineOperationDTO));
    }

    /**
     * 获取我的事项详情
     *
     * @param matterDetailDTO 酒操作业务表 主键 和 区域id 和 防火区Id
     * @return
     */
    @PostMapping("/getMatterDetailVOList")
    public TableDataInfo getMatterDetailVOList(@RequestBody MatterDetailDTO matterDetailDTO) {
        return getDataTable(iLaoczWineOperationsService.getMatterDetailVOList(matterDetailDTO));
    }

    /**
     * 二维码扫描根据事项id和陶坛编号进行查询事项进而快速开始任务
     *
     * @param qrCodeScanMatterDetailDTO 事项id 陶坛编号
     * @return 事项详情id
     */
    @PostMapping("qrCodeScanMatterDetail")
    public AjaxResult qrCodeScanMatterDetail(@RequestBody QrCodeScanMatterDetailDTO qrCodeScanMatterDetailDTO) {
        return AjaxResult.success(iLaoczWineOperationsService.qrCodeScanMatterDetail(qrCodeScanMatterDetailDTO.getWineOperationsId(), qrCodeScanMatterDetailDTO.getPotteryAltarNumber()));
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
    @RateLimiter(time = 1, count = 1)
    public AjaxResult setWeighingTank(@RequestBody WineEntryApplyParamDTO weighingTank) {
        return AjaxResult.success(iLaoczWineOperationsService.setWeighingTank(weighingTank));
    }


    /**
     * 确认审批失败
     *
     * @param matterDetailDTO 酒操作业务表 主键
     * @return
     */
    @PostMapping("/confirmApprovalFailed")
    @RateLimiter(time = 2, count = 1)
    public AjaxResult confirmApprovalFailed(@RequestBody MatterDetailDTO matterDetailDTO) {
        return AjaxResult.success(iLaoczWineOperationsService.confirmApprovalFailed(matterDetailDTO.getWineOperationsId()));
    }
}
