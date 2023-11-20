package com.rexel.web.controller.pulse;

import com.rexel.common.config.PulseUrlConfig;
import com.rexel.common.core.controller.BaseController;
import com.rexel.common.core.domain.AjaxResult;
import com.rexel.common.utils.pulse.PulseHttpRequestUtil;
import com.rexel.common.utils.pulse.PulseHttpResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName PulseBucketController
 * @Description
 * @Author 孟开通
 * @Date 2022/7/19 15:53
 **/
@RestController
@RequestMapping("/pulse/bucket")
public class PulseBucketController extends BaseController {
    @Autowired
    private PulseUrlConfig pulseUrlConfig;
    @GetMapping("/list")
    public AjaxResult list(){
        return AjaxResult.success(PulseHttpResponseUtil
                .pretreatmentResultsArray(PulseHttpRequestUtil
                        .sendPostJson(pulseUrlConfig.getBucketList(), null)));
    }
}
