package com.rexel.laocz.history;

import cn.hutool.core.bean.BeanUtil;
import com.rexel.common.core.controller.BaseController;
import com.rexel.common.core.domain.AjaxResult;
import com.rexel.common.core.page.TableDataInfo;
import com.rexel.common.utils.poi.ExcelUtil;
import com.rexel.laocz.domain.LaoczWineHistory;
import com.rexel.laocz.domain.vo.LaoczWineHistoryVO;
import com.rexel.laocz.domain.vo.TableDataInfoDataReportLossVO;
import com.rexel.laocz.domain.vo.TableDataInfoDataReportVO;
import com.rexel.laocz.service.ILaoczWineHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
    public TableDataInfo getList(Long potteryAltarId, String fromTime, String endTime, String operationType,String potteryAltarNumber) {
        startPage();
        return getDataTable(laoczWineHistoryService.selectLaoczWineHistory(potteryAltarId, fromTime, endTime, operationType,potteryAltarNumber), "historyInfo");
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
     * 数据报表-淘坛操作记录查询2
     *
     * @param potteryAltarNumber 陶坛编号
     * @param fireZoneId         防火区ID
     * @param areaId             场区ID
     * @return
     */
    @GetMapping("/getPotteryJarOperationTableList")
    public TableDataInfo getPotteryJarOperationTableList(String potteryAltarNumber, Long fireZoneId, Long areaId) {
        return getDataTable(laoczWineHistoryService.getLaoczWineHistoryTableList(potteryAltarNumber, fireZoneId, areaId), "PotteryReport");
    }

    /**
     * 陶坛操作记录表导出
     *
     * @param response
     * @throws IOException
     */
    @PostMapping("/exportThePotteryJarOperation")
    public void exportThePotteryJarOperation(HttpServletResponse response, String fromTime, String endTime, String liquorBatchId) throws IOException {
        ExcelUtil<LaoczWineHistoryVO> util = new ExcelUtil<>(LaoczWineHistoryVO.class);
        List<LaoczWineHistoryVO> laoczWineHistories = laoczWineHistoryService.getLaoczWineHistoryTable(fromTime, endTime, liquorBatchId);
        util.exportExcel(response, BeanUtil.copyToList(laoczWineHistories, LaoczWineHistoryVO.class), "陶坛操作报表");
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
     * 批次亏损查询一
     *
     * @param liquorBatchId 批次ID
     * @return
     */
    @GetMapping("/selectLaoczWineHistoryInfoOne")
    public TableDataInfoDataReportLossVO selectLaoczWineHistoryInfoOne(String liquorBatchId) {
        return laoczWineHistoryService.selectLaoczWineHistoryInfoOne(liquorBatchId);
    }

    /**
     * 批次亏损查询二
     *
     * @param potteryAltarId 陶坛编号
     * @param fireZoneId     防火区编号
     * @param areaId         区域编号
     * @return
     */
    @GetMapping("/selectLaoczWineHistoryInfoTwo")
    public TableDataInfo selectLaoczWineHistoryInfoTwo(Long potteryAltarId, Long fireZoneId, Long areaId) {
        startPage();
        return getDataTable(laoczWineHistoryService.selectLaoczWineHistoryInfoTwo(potteryAltarId, fireZoneId, areaId), "lossStatement");
    }

    /**
     * 陶坛操作记录表导出
     *
     * @param response
     * @throws IOException
     */
    @PostMapping("/batchLossReportExport")
    public void batchLossReportExport(HttpServletResponse response, String liquorBatchId) throws IOException {
        ExcelUtil<LaoczWineHistoryVO> util = new ExcelUtil<>(LaoczWineHistoryVO.class);
        List<LaoczWineHistoryVO> laoczWineHistories = laoczWineHistoryService.batchLossReportExport(liquorBatchId);
        util.exportExcel(response, BeanUtil.copyToList(laoczWineHistories, LaoczWineHistoryVO.class), "批次亏损报表");
    }

    /**
     * 获取操作记录详情
     * @param winHisId 酒历史表id
     * @return 酒历史详情数据
     */
    @GetMapping("/getOperationdetails")
    public AjaxResult getOperationdetails(Long winHisId){
        LaoczWineHistory laoczWineHistory = laoczWineHistoryService.getById(winHisId);
        return AjaxResult.success(laoczWineHistory);
    }
}
