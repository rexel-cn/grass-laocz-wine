package com.rexel.web.controller.rule;


import com.rexel.common.annotation.Log;
import com.rexel.common.core.controller.BaseController;
import com.rexel.common.core.domain.AjaxResult;
import com.rexel.common.enums.BusinessType;
import com.rexel.system.domain.GrassNoticeConfig;
import com.rexel.system.domain.dto.GrassNoticeConfigDTO;
import com.rexel.system.service.IGrassNoticeConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 通知配置主Controller
 *
 * @author grass-service
 * @date 2022-07-29
 */
@RestController
@RequestMapping("/rexel-api/noticeConfig")
public class GrassNoticeConfigController extends BaseController {
    @Autowired
    private IGrassNoticeConfigService grassNoticeConfigService;

    /**
     * 查询通知配置主列表
     */
    @GetMapping("/list")
    public AjaxResult list(GrassNoticeConfig grassNoticeConfig) {
        return AjaxResult.success(grassNoticeConfigService.selectGrassNoticeConfigList(grassNoticeConfig));
    }

    /**
     * 修改通知配置主
     */
    @Log(title = "修改通知配置是否开通状态", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody GrassNoticeConfigDTO GrassNoticeConfigDto) {
        return toAjax(grassNoticeConfigService.updateOpenStatusById(GrassNoticeConfigDto));
    }


}
