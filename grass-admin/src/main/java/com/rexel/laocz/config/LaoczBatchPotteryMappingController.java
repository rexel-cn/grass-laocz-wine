package com.rexel.laocz.config;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson2.JSONObject;
import com.rexel.common.core.controller.BaseController;
import com.rexel.common.core.domain.AjaxResult;
import com.rexel.common.core.page.TableDataInfo;
import com.rexel.common.utils.StringUtils;
import com.rexel.common.utils.poi.ExcelUtil;
import com.rexel.laocz.domain.vo.BatchInfoVo;
import com.rexel.laocz.domain.vo.LaoczBatchPotteryMappingVO;
import com.rexel.laocz.domain.vo.TableDataInfoDataReportActualVO;
import com.rexel.laocz.service.ILaoczBatchPotteryMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

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
     * 酒液存储报表查询所有
     *
     * @param areaId             区域编号
     * @param fireZoneId         防火区编号
     * @param liquorBatchId      批次ID
     * @param potteryAltarNumber 陶坛编号
     * @param liquorName         酒品名称
     * @return
     */
    @GetMapping("/listAndNumber")
    private TableDataInfoDataReportActualVO getActual(Long areaId, Long fireZoneId, String liquorBatchId, String potteryAltarNumber, String liquorName) {
        return laoczBatchPotteryMappingService.selectTableDataInfoDataReportActualVO(areaId, fireZoneId, liquorBatchId, potteryAltarNumber, liquorName);
    }

    /**
     * 酒液存储报表导出
     *
     * @param response
     * @param requestParams
     * @throws IOException
     */
    @PostMapping("/liquorStorageReportExport")
    public AjaxResult liquorStorageReportExport(HttpServletResponse response, @RequestBody JSONObject requestParams) throws IOException {
        Long areaId = requestParams.getLong("areaId");
        Long fireZoneId = requestParams.getLong("fireZoneId");
        String liquorBatchId = requestParams.getString("liquorBatchId");
        // 检查三个参数是否同时为空
        if (Objects.isNull(areaId) && Objects.isNull(fireZoneId) && StringUtils.isEmpty(liquorBatchId)) {
            return AjaxResult.error("场区编号、防火区编号和批次编号不能同时为空，请至少选择一项！");
        }
        ExcelUtil<LaoczBatchPotteryMappingVO> util = new ExcelUtil<>(LaoczBatchPotteryMappingVO.class);
        List<LaoczBatchPotteryMappingVO> laoczWineHistories = laoczBatchPotteryMappingService.selectTableDataInfoReportActualList(fireZoneId, liquorBatchId, areaId);
        util.exportExcel(response, BeanUtil.copyToList(laoczWineHistories, LaoczBatchPotteryMappingVO.class), "酒液存储报表");
        return AjaxResult.success("酒液存储报表导出成功！");
    }

    /**
     * 看板场区概览
     *
     * @param areaId     场区编号
     * @param fireZoneId 防火区编号
     * @return
     */
    @GetMapping("/getBoardData")
    private AjaxResult getBoardData(Long areaId, Long fireZoneId) {
        return AjaxResult.success(laoczBatchPotteryMappingService.selectBoardData(areaId, fireZoneId));
    }

    /**
     * 移动端场区概览
     *
     * @param potteryAltarNumber 陶坛编号
     * @return 详情数据
     */
    @GetMapping("/getOverview")
    public AjaxResult getOverview(String potteryAltarNumber) {
        return AjaxResult.success(laoczBatchPotteryMappingService.getOverview(potteryAltarNumber));
    }

    /**
     * 批次查询
     *
     * @param liquorBatchId 酒批次Id
     * @return 分页数据
     */
    @GetMapping("/getBatchInfo")
    public TableDataInfo getBatchInfo(String liquorBatchId) {
        startPage();
        List<BatchInfoVo> batchInfoVos = laoczBatchPotteryMappingService.getBatchInfo(liquorBatchId);
        return getDataTable(batchInfoVos, "batchInfo");
    }

}
