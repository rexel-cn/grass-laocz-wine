package com.rexel.laocz.history;

import com.rexel.common.core.controller.BaseController;
import com.rexel.common.core.page.TableDataInfo;
import com.rexel.laocz.service.ILaoczSamplingHistorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 取样记录
 *
 * @author Yiyang.Hu
 * @version 1.0
 * @date 2024-03-20 4:54 PM
 */
@RestController
@RequestMapping("/rexel-api/LaoczSamplingHistority")
public class LaoczSamplingHistorityController extends BaseController {
    @Autowired
    public ILaoczSamplingHistorityService laoczSamplingHistorityService;

    /**
     * 查询取样
     *
     * @param potteryAltarId 陶坛ID
     * @param fromTime       开始时间
     * @param endTime        结束时间
     * @param liquorBatchId  批次ID
     * @return
     */
    @GetMapping("/list")
    public TableDataInfo getListTableDataList(Long potteryAltarId, String fromTime, String endTime, String liquorBatchId) {
        startPage();
        return getDataTable(laoczSamplingHistorityService.selectLaoczSamplingHist(potteryAltarId, fromTime, endTime, liquorBatchId), "SamplingHistory");
    }

    /**
     * 取样管理
     *
     * @param fromTime           开始时间
     * @param endTime            结束时间
     * @param areaId             场区编号
     * @param fireZoneId         防火区编号
     * @param potteryAltarNumber 陶坛编号
     * @param liquorBatchId      酒品批次
     * @return
     */
    @GetMapping("/samplingList")
    public TableDataInfo getSamplingListTableData(String fromTime,
                                                  String endTime,
                                                  Long areaId,
                                                  Long fireZoneId,
                                                  String potteryAltarNumber,
                                                  String liquorBatchId) {
        startPage();
        return getDataTable(laoczSamplingHistorityService.selectLaoczSamplingList(fromTime, endTime, areaId, fireZoneId, potteryAltarNumber, liquorBatchId), "SamplingHistory");
    }
}
