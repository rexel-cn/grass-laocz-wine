package com.rexel.web.controller.alarm;

import com.rexel.common.core.controller.BaseController;
import com.rexel.common.core.domain.AjaxResult;
import com.rexel.common.core.page.TableDataInfo;
import com.rexel.system.domain.dto.GrassAlarmHistoryDTO;
import com.rexel.system.domain.vo.GrassAlarmHistoryVO;
import com.rexel.system.service.IGrassAlarmHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName GrassAlarmHistoryController
 * @Description
 * @Author 孟开通
 * @Date 2022/8/10 16:10
 **/
@RestController
@RequestMapping("/rexel-api/alarmHistory")
public class GrassAlarmHistoryController extends BaseController {
    @Autowired
    private IGrassAlarmHistoryService iGrassAlarmHistoryService;

    /**
     * 查询告警历史表DruidConfig
     *
     * @param iotAlarmHistoryQuery iotAlarmHistoryQuery
     * @return 结果
     */
    @GetMapping("/list")
    public TableDataInfo list(GrassAlarmHistoryDTO iotAlarmHistoryQuery) {
        startPage();
        List<GrassAlarmHistoryVO> list = iGrassAlarmHistoryService.selectIotAlarmHistoryList(iotAlarmHistoryQuery);
        return getDataTable(list, "alarmHistory");
    }

    /**
     * 告警历史数据聚合（时间，数量）
     *
     * @param iotAlarmHistoryQuery iotAlarmHistoryQuery
     * @return 结果
     */
    @GetMapping("/group")
    public AjaxResult group(GrassAlarmHistoryDTO iotAlarmHistoryQuery) {
        return AjaxResult.success(iGrassAlarmHistoryService.selectIotAlarmHistoryGroup(iotAlarmHistoryQuery));
    }
}
