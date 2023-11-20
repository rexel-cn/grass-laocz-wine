package com.rexel.web.controller.rule;

import com.rexel.common.annotation.Log;
import com.rexel.common.core.controller.BaseController;
import com.rexel.common.core.domain.AjaxResult;
import com.rexel.common.core.page.TableDataInfo;
import com.rexel.common.enums.BusinessType;
import com.rexel.common.utils.SecurityUtils;
import com.rexel.system.domain.GrassUserMessage;
import com.rexel.system.service.IGrassUserMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName GrassUserMessageController
 * @Description
 * @Author 孟开通
 * @Date 2022/8/30 09:42
 **/
@RestController
@RequestMapping("/rexel-api/grass/userMessage")
public class GrassUserMessageController extends BaseController {
    @Autowired
    private IGrassUserMessageService userMessageService;

    /**
     * 站内信列表
     *
     * @param userMessage userMessage
     * @return TableDataInfo
     */
    @GetMapping("/messageList")
    public TableDataInfo messageList(GrassUserMessage userMessage) {
        startPage();
        userMessage.setUserId(SecurityUtils.getUserId().toString());
        userMessage.setIsRead(0L);
        return getDataTable(userMessageService.selectGrassUserMessageList(userMessage), null);
    }

    /**
     * 站内信列表
     *
     * @param userMessage userMessage
     * @return AjaxResult
     */
    @PostMapping("/messageCount")
    public AjaxResult messageCount(@RequestBody GrassUserMessage userMessage) {
        return userMessageService.selectUserMessageCount(userMessage);
    }

    /**
     * 标记为已读状态
     *
     * @param userMessage userMessage
     * @return AjaxResult
     */
    @Log(title = "站内信-更改状态", businessType = BusinessType.UPDATE)
    @PostMapping("/confirmRead")
    public AjaxResult confirmRead(@RequestBody GrassUserMessage userMessage) {
        return toAjax(userMessageService.confirmRead(userMessage));
    }

    /**
     * 全部已读
     *
     * @return 结果
     */
    @PostMapping("/allConfirmRead")
    public AjaxResult allConfirmRead(@RequestBody GrassUserMessage userMessage) {
        return toAjax(userMessageService.allConfirmRead(userMessage));
    }
}
