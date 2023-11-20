package com.rexel.web.controller.rule;

import com.rexel.common.core.controller.BaseController;
import com.rexel.common.core.domain.AjaxResult;
import com.rexel.system.domain.GrassNoticeSms;
import com.rexel.system.service.IGrassNoticeSmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 通知短信配置Controller
 *
 * @author grass-service
 * @date 2022-07-29
 */
@RestController
@RequestMapping("/rexel-api/noticeSms")
public class GrassNoticeSmsController extends BaseController {
    @Autowired
    private IGrassNoticeSmsService grassNoticeSmsService;

    /**
     * 查询通知短信配置
     */
    @GetMapping("/getInfo")
    public AjaxResult getInfo() {
        return AjaxResult.success(grassNoticeSmsService.selectOneByTenantId());
    }

    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody GrassNoticeSms grassNoticeSms) {
        return AjaxResult.success(grassNoticeSmsService.saveOrUpdate(grassNoticeSms));
    }
}
