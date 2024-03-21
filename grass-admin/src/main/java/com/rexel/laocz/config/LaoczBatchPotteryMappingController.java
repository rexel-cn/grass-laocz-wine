package com.rexel.laocz.config;

import cn.hutool.core.bean.BeanUtil;
import com.rexel.common.core.controller.BaseController;
import com.rexel.common.core.page.TableDataInfo;
import com.rexel.common.utils.poi.ExcelUtil;
import com.rexel.laocz.domain.vo.LaoczBatchPotteryMappingVO;
import com.rexel.laocz.domain.vo.TableDataInfoDataReportActualVO;
import com.rexel.laocz.service.ILaoczBatchPotteryMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 陶坛与批次实时关系对象
 *
 * @author Yiyang.Hu
 * @version 1.0
 * @date 2024-03-21 10:10 AM
 */
@RestController
@RequestMapping("/rexel-api/batchPotteryMapping")
public class LaoczBatchPotteryMappingController extends BaseController {
    @Autowired
    private ILaoczBatchPotteryMappingService laoczBatchPotteryMappingService;

    /**
     * 酒液存储报表查询
     *
     * @param fireZoneId         防火区编号
     * @param liquorBatchId      批次ID
     * @param potteryAltarNumber 陶坛ID
     * @param liquorName         酒品名称
     * @param areaId             区域编号
     * @return
     */
    @GetMapping("/list")
    private TableDataInfo getListTableDataInfo(Long fireZoneId, String liquorBatchId, String potteryAltarNumber, String liquorName, Long areaId) {
        startPage();
        return getDataTable(laoczBatchPotteryMappingService.selectTableDataInfoReportActual(fireZoneId, liquorBatchId, potteryAltarNumber, liquorName, areaId), "LiquorStorage");
    }

    /**
     * 酒液存储报表查询所有
     *
     * @param areaId        区域编号
     * @param fireZoneId    防火区编号
     * @param liquorBatchId 批次ID
     * @return
     */
    @GetMapping("/listAndNumber")
    private TableDataInfoDataReportActualVO getActual(Long areaId, Long fireZoneId, String liquorBatchId) {
        return laoczBatchPotteryMappingService.selectTableDataInfoDataReportActualVO(areaId, fireZoneId, liquorBatchId);
    }

    /**
     * 酒液存储报表导出
     *
     * @param response
     * @throws IOException
     */
    @PostMapping("/liquorStorageReportExport")
    public void liquorStorageReportExport(HttpServletResponse response, Long areaId, Long fireZoneId, String liquorBatchId) throws IOException {
        ExcelUtil<LaoczBatchPotteryMappingVO> util = new ExcelUtil<>(LaoczBatchPotteryMappingVO.class);
        List<LaoczBatchPotteryMappingVO> laoczWineHistories = laoczBatchPotteryMappingService.selectTableDataInfoReportActualList(fireZoneId, liquorBatchId, areaId);
        util.exportExcel(response, BeanUtil.copyToList(laoczWineHistories, LaoczBatchPotteryMappingVO.class), "酒液存储报表");
    }
}
