package com.rexel.laocz.history;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson2.JSONObject;
import com.rexel.common.core.controller.BaseController;
import com.rexel.common.core.domain.AjaxResult;
import com.rexel.common.core.page.TableDataInfo;
import com.rexel.common.utils.StringUtils;
import com.rexel.common.utils.poi.ExcelUtil;
import com.rexel.laocz.domain.LaoczWineHistory;
import com.rexel.laocz.domain.vo.*;
import com.rexel.laocz.service.ILaoczWineHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
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
     * @param detailType     操作类型
     * @param workOrderId    工单Id
     * @return
     */
    @GetMapping("/list")
    public TableDataInfo getList(Long potteryAltarId, String fromTime, String endTime, String detailType, String workOrderId) {
        startPage();
        return getDataTable(laoczWineHistoryService.selectLaoczWineHistory(potteryAltarId, fromTime, endTime, detailType, workOrderId), "historyInfo");
    }

    /**
     * 数据报表-淘坛操作记录
     *
     * @param potteryAltarNumber 陶坛编号
     * @param fromTime           开始时间
     * @param endTime            结束时间
     * @param liquorBatchId      批次ID
     * @param fireZoneId         防火区ID
     * @param areaId             场区ID
     * @return
     */
    @GetMapping("/getPotteryJarOperationList")
    public TableDataInfoDataReportVO getList(String potteryAltarNumber, String fromTime, String endTime, String liquorBatchId, Long fireZoneId, Long areaId) {
        return laoczWineHistoryService.selectTableDataInfo(potteryAltarNumber, fromTime, endTime, liquorBatchId, fireZoneId, areaId);
    }

    /**
     * 陶坛操作记录表导出
     *
     * @param response
     * @param requestParams
     * @throws IOException
     */
    @PostMapping("/exportThePotteryJarOperation")
    public AjaxResult exportThePotteryJarOperation(HttpServletResponse response, @RequestBody JSONObject requestParams) throws IOException {
        String liquorBatchId = requestParams.getString("liquorBatchId");
        String endTime = requestParams.getString("endTime");
        String fromTime = requestParams.getString("fromTime");
        // 检查所有参数是否均为空
        if (StringUtils.isEmpty(fromTime) && StringUtils.isEmpty(endTime) && StringUtils.isEmpty(liquorBatchId)) {
            return AjaxResult.error("开始时间、结束时间和批次编号都不能为空，请至少选择一项！");
        }
        // 将字符串转换为LocalDate对象
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); // 假设日期格式是"yyyy-MM-dd"

        if (StringUtils.isNotEmpty(fromTime) && StringUtils.isNotEmpty(endTime)) {
            LocalDate startDate = LocalDate.parse(fromTime, formatter);
            LocalDate endDate = LocalDate.parse(endTime, formatter);
            // 判断时间跨度是否超过三个月
            long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);
            if (daysBetween > 90) { // 如果天数大于90天（即超过三个月）
                return AjaxResult.error("所选时间范围超过三个月，请重新选择！");
            }
        }
        ExcelUtil<LaoczPotteryAltarOperationRecordExportVO> util = new ExcelUtil<>(LaoczPotteryAltarOperationRecordExportVO.class);
        List<LaoczWineHistoryVO> laoczWineHistories = laoczWineHistoryService.getLaoczWineHistoryTable(fromTime, endTime, liquorBatchId);
        util.exportExcel(response, BeanUtil.copyToList(laoczWineHistories, LaoczPotteryAltarOperationRecordExportVO.class), "陶坛操作报表");
        return AjaxResult.success("陶坛操作报表导出成功！");
    }

    /**
     * 数据报表-陶坛操作记录详情
     *
     * @param winHisId 历史ID
     * @return
     */
    @GetMapping("/laoczWineHistorieInfo")
    public AjaxResult getLaoczWineHistorieInfo(Long winHisId) {
        return AjaxResult.success(laoczWineHistoryService.selectLaoczWineHistoryInfo(winHisId));
    }

    /**
     * 批次亏损查询
     *
     * @param liquorBatchId      批次ID
     * @param potteryAltarNumber 陶坛编号
     * @param fireZoneId         防火区编号
     * @param areaId             区域编号
     * @return
     */
    @GetMapping("/selectLaoczWineHistoryInfoOne")
    public TableDataInfoDataReportLossVO selectLaoczWineHistoryInfoOne(String liquorBatchId, String potteryAltarNumber, Long fireZoneId, Long areaId) {
        return laoczWineHistoryService.selectLaoczWineHistoryInfoOne(liquorBatchId, potteryAltarNumber, fireZoneId, areaId);
    }

    /**
     * 批次亏损报表导出
     *
     * @param response
     * @param requestParams 批次编号
     * @throws IOException
     */
    @PostMapping("/batchLossReportExport")
    public AjaxResult batchLossReportExport(HttpServletResponse response, @RequestBody JSONObject requestParams) throws IOException {
        String liquorBatchId = requestParams.getString("liquorBatchId");
        // 检查所有参数是否均为空
        if (StringUtils.isEmpty(liquorBatchId)) {
            return AjaxResult.error("批次编号都不能为空，请至少选择一项！");
        }
        ExcelUtil<LaoczBatchLossReportExportVO> util = new ExcelUtil<>(LaoczBatchLossReportExportVO.class);
        List<LaoczWineHistoryVO> laoczWineHistories = laoczWineHistoryService.batchLossReportExport(liquorBatchId);
        util.exportExcel(response, BeanUtil.copyToList(laoczWineHistories, LaoczBatchLossReportExportVO.class), "批次亏损报表");
        return AjaxResult.success("批次亏损报表导出成功！");
    }

    /**
     * 获取操作记录详情
     *
     * @param winHisId 酒历史表id
     * @return 酒历史详情数据
     */
    @GetMapping("/getOperationdetails")
    public AjaxResult getOperationdetails(Long winHisId) {
        LaoczWineHistory laoczWineHistory = laoczWineHistoryService.getById(winHisId);
        return AjaxResult.success(laoczWineHistory);
    }

    /**
     * 操作记录
     *
     * @param fromTime    开始时间
     * @param endTime     结束时间
     * @param detailType  操作类型
     * @param workOrderId 工单Id
     * @return 分页数据
     */
    @GetMapping("/listOperation")
    public TableDataInfo selectOperation(String fromTime, String endTime, String detailType, String workOrderId) {
        startPage();
        List<LaoczWineHistoryVO> list = laoczWineHistoryService.selectOperation(fromTime, endTime, detailType, workOrderId);
        return getDataTable(list, "historyInfo");
    }

    /**
     * 根据工单Id查询所有陶坛信息
     *
     * @param laoczWineHistory
     * @return
     */
    @GetMapping("getDetailByWorkId")
    public AjaxResult getDetailByWorkId(LaoczWineHistory laoczWineHistory) {
        List<LaoczWineHistory> laoczWineHistories = laoczWineHistoryService.selectDetailByWorkId(laoczWineHistory);
        return AjaxResult.success(laoczWineHistories);
    }

    /**
     * 根据工单Id,陶坛Id,操作类型查询陶坛信息
     *
     * @param laoczWineHistory
     * @return
     */
    @GetMapping("getOneByWorkId")
    public AjaxResult getOneByWorkId(LaoczWineHistory laoczWineHistory) {
        List<LaoczWineHistory> laoczWineHistories = laoczWineHistoryService.selectDetailByWorkId(laoczWineHistory);
        if (!CollectionUtil.isEmpty(laoczWineHistories)) {
            return AjaxResult.success(laoczWineHistories.get(0));
        }
        return AjaxResult.success();
    }
}
