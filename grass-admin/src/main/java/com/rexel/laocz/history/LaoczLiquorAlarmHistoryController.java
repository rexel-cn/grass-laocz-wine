package com.rexel.laocz.history;

import com.rexel.common.core.controller.BaseController;
import com.rexel.common.core.page.TableDataInfo;
import com.rexel.laocz.service.ILaoczLiquorAlarmHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 老村长酒存储时间报警历史
 *
 * @author Yiyang.Hu
 * @version 1.0
 * @date 2024-03-20 10:04 AM
 */
@RestController
@RequestMapping("/rexel-api/LiquorAlarmHistory")
public class LaoczLiquorAlarmHistoryController extends BaseController {
    @Autowired
    private ILaoczLiquorAlarmHistoryService laoczLiquorAlarmHistoryService;

    /**
     * 查询报警历史信息
     *
     * @param potteryAltarId 陶坛ID
     * @param fromTime       开始时间
     * @param endTime        结束时间
     * @param liquorBatchId   批次ID
     * @param liquorRuleName 规则名称
     * @return
     */
    @GetMapping("/list")
    public TableDataInfo getTableDataList(Long potteryAltarId, String fromTime, String endTime, String liquorBatchId, String liquorRuleName) {
        startPage();
        return getDataTable(laoczLiquorAlarmHistoryService.selectLaoczLiquorAlarmHistory(potteryAltarId, fromTime, endTime, liquorBatchId, liquorRuleName), "alarmRecord");
    }

    /**
     * 报警查询
     *
     * @param fromTime       开始时间
     * @param endTime        结束时间
     * @param liquorBatchId   批次ID
     * @param liquorRuleName 规则名称
     * @return
     */
    @GetMapping("/alarmHistoryList")
    public TableDataInfo getAlarmHistoryList(String fromTime, String endTime, String liquorBatchId, String liquorRuleName) {
        startPage();
        return getDataTable(laoczLiquorAlarmHistoryService.selectLaoczLiquorAlarmHistoryList(fromTime, endTime, liquorBatchId, liquorRuleName), "alarmHistoryList");
    }
}
