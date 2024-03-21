package com.rexel.laocz.history;

import cn.hutool.core.bean.BeanUtil;
import com.rexel.common.core.controller.BaseController;
import com.rexel.common.core.domain.AjaxResult;
import com.rexel.common.core.page.TableDataInfo;
import com.rexel.common.utils.poi.ExcelUtil;
import com.rexel.laocz.domain.LaoczWineHistory;
import com.rexel.laocz.domain.vo.LaoczWineHistoryExportVO;
import com.rexel.laocz.domain.vo.TableDataInfoDataReportVO;
import com.rexel.laocz.service.ILaoczWineHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * 酒历史查询
 *
 * @author Yiyang.Hu
 * @version 1.0
 * @date 2024-03-19 5:36 PM
 */
@RestController
@RequestMapping("/rexel-api/wineHistory")
public class LaoczWineHistoryController extends BaseController {
    @Autowired
    private ILaoczWineHistoryService laoczWineHistoryService;

    /**
     * 查询
     *
     * @param potteryAltarId 陶坛ID
     * @param fromTime       开始时间
     * @param endTime        结束时间
     * @param operationType  操作类型
     * @return
     */
    @GetMapping("/list")
    public TableDataInfo getList(Long potteryAltarId, String fromTime, String endTime, Long operationType) {
        startPage();
        return getDataTable(laoczWineHistoryService.selectLaoczWineHistory(potteryAltarId, fromTime, endTime, operationType), "historyInfo");
    }

    /**
     * 数据报表-淘坛操作记录
     *
     * @param potteryAltarId 陶坛ID
     * @param fromTime       开始时间
     * @param endTime        结束时间
     * @param liquorBatchId  批次ID
     * @param fireZoneId     防火区ID
     * @param areaId         场区ID
     * @return
     */
    @GetMapping("/getPotteryJarOperationList")
    public TableDataInfoDataReportVO getList(Long potteryAltarId, String fromTime, String endTime, String liquorBatchId, Long fireZoneId, Long areaId) {
        return laoczWineHistoryService.selectTableDataInfo(potteryAltarId, fromTime, endTime, liquorBatchId, fireZoneId, areaId);
    }

    /**
     * 陶坛操作记录表导出
     *
     * @param response
     * @throws IOException
     */
    @PostMapping("/exportThePotteryJarOperation")
    public void exportThePotteryJarOperation(HttpServletResponse response) throws IOException {
        ExcelUtil<LaoczWineHistoryExportVO> util = new ExcelUtil<>(LaoczWineHistoryExportVO.class);
        List<LaoczWineHistory> laoczWineHistories = laoczWineHistoryService.selectLaoczWineHistoryList(null);
        util.exportExcel(response, BeanUtil.copyToList(laoczWineHistories, LaoczWineHistoryExportVO.class), "陶坛操作报表");
    }

    /**
     * 数据报表-陶坛操作记录详情
     *
     * @param winHisId 历史ID
     * @return
     */
    @GetMapping("/laoczWineHistorieInfo")
    public AjaxResult getLaoczWineHistorieInfo(Long winHisId) throws ParseException {
        return AjaxResult.success(laoczWineHistoryService.selectLaoczWineHistoryInfo(winHisId));
    }
}
