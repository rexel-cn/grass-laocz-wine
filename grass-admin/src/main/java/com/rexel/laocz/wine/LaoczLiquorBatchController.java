package com.rexel.laocz.wine;

import com.rexel.common.core.domain.AjaxResult;
import com.rexel.laocz.service.ILaoczLiquorBatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName LaoczLiquorBatchController
 * @Description 酒液批次相关信息Controller
 * @Author 孟开通
 * @Date 2024/3/29 11:09
 **/
@RestController
@RequestMapping("/rexel-api/liquorBatch")
public class LaoczLiquorBatchController {
    @Autowired
    private ILaoczLiquorBatchService iLaoczLiquorBatchService;

    @GetMapping("/wineOutLaoczLiquorBatchList")
    public AjaxResult wineOutLaoczLiquorBatchList() {
        return AjaxResult.success(iLaoczLiquorBatchService.wineOutLaoczLiquorBatchList());
    }
}
