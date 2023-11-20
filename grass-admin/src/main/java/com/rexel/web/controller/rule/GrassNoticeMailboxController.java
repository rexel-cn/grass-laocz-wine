package com.rexel.web.controller.rule;

import com.rexel.common.core.controller.BaseController;
import com.rexel.common.core.domain.AjaxResult;
import com.rexel.system.domain.GrassNoticeMailbox;
import com.rexel.system.service.IGrassNoticeMailboxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 通知邮箱配置Controller
 *
 * @author grass-service
 * @date 2022-07-29
 */
@RestController
@RequestMapping("/rexel-api/noticeMail")
public class GrassNoticeMailboxController extends BaseController {
    @Autowired
    private IGrassNoticeMailboxService grassNoticeMailboxService;

    /**
     * 查询通知邮箱配置
     */
    @GetMapping("/getInfo")
    public AjaxResult getInfo() {
        return AjaxResult.success(grassNoticeMailboxService.selectOneByTenantId());
    }

    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody GrassNoticeMailbox grassNoticeMailbox) {
        return AjaxResult.success(grassNoticeMailboxService.saveOrUpdate(grassNoticeMailbox));
    }
}
