package com.rexel.laocz.wine;

import com.rexel.common.annotation.RateLimiter;
import com.rexel.common.core.controller.BaseController;
import com.rexel.common.core.domain.AjaxResult;
import com.rexel.laocz.domain.dto.WinBaseDTO;
import com.rexel.laocz.domain.dto.WineOutApplyDTO;
import com.rexel.laocz.domain.dto.WineOutStartDTO;
import com.rexel.laocz.service.WineOutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName WineOutController
 * @Description 酒品出库 控制器
 * @Author 孟开通
 * @Date 2024/3/19 15:42
 **/
@RestController
@RequestMapping("/rexel-api/wineOut")
public class WineOutController extends BaseController {

    @Autowired
    private WineOutService wineOutService;

    @PostMapping("/apply")
    public AjaxResult wineOutApply(@RequestBody List<WineOutApplyDTO> list) {
        wineOutService.wineOutApply(list);
        return AjaxResult.success();
    }

    /**
     * 出酒操作，称重罐称重量
     *
     * @param wineOutStartDTO 酒操作业务详情id
     * @return 称重量
     */
    @PostMapping("/start")
    @RateLimiter(time = 1, count = 1)
    public AjaxResult wineOutStart(@RequestBody WineOutStartDTO wineOutStartDTO) {
        return AjaxResult.success(wineOutService.wineOutStart(wineOutStartDTO));
    }

    /**
     * 出酒操作完成
     *
     * @param winBaseDTO 酒操作业务详情id
     * @return 完成
     */
    @PostMapping("/finish")
    @RateLimiter(time = 2, count = 1)
    public AjaxResult wineOutFinish(@RequestBody WinBaseDTO winBaseDTO) {
        wineOutService.wineOutFinish(winBaseDTO.getWineDetailsId());
        return AjaxResult.success();
    }


}
