package com.rexel.web.controller.rule;

import com.rexel.common.core.controller.BaseController;
import com.rexel.common.core.domain.AjaxResult;
import com.rexel.system.domain.dto.GrassNoticeDingdingDTO;
import com.rexel.system.service.IGrassNoticeDingdingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 通知钉钉配置Controller
 *
 * @author grass-service
 * @date 2022-07-29
 */
@RestController
@RequestMapping("/rexel-api/noticeDingding")
public class GrassNoticeDingdingController extends BaseController {
    @Autowired
    private IGrassNoticeDingdingService grassNoticeDingdingService;

    /**
     * 获取通知钉钉配置详细信息
     */
    @GetMapping("/getInfo")
    public AjaxResult getInfo() {
        return AjaxResult.success(grassNoticeDingdingService.selectOneByTenantId());
    }

    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody GrassNoticeDingdingDTO grassNoticeDingdingDTO) {
        return AjaxResult.success(grassNoticeDingdingService.updateOrInsert(grassNoticeDingdingDTO));
    }
}
