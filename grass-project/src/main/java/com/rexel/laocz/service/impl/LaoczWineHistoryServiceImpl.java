package com.rexel.laocz.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.rexel.common.core.domain.SysHeaderMetadata;
import com.rexel.common.core.page.PageHeader;
import com.rexel.common.core.service.ISysHeaderMetadataService;
import com.rexel.common.exception.ServiceException;
import com.rexel.common.utils.PageUtils;
import com.rexel.common.utils.StringUtils;
import com.rexel.laocz.domain.LaoczBatchPotteryMapping;
import com.rexel.laocz.domain.LaoczLiquorManagement;
import com.rexel.laocz.domain.LaoczWineHistory;
import com.rexel.laocz.domain.vo.*;
import com.rexel.laocz.mapper.LaoczLiquorManagementMapper;
import com.rexel.laocz.mapper.LaoczWineHistoryMapper;
import com.rexel.laocz.service.ILaoczBatchPotteryMappingService;
import com.rexel.laocz.service.ILaoczWineHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 酒历史Service业务层处理
 *
 * @author grass-service
 * @date 2024-03-12
 */
@Service
public class LaoczWineHistoryServiceImpl extends ServiceImpl<LaoczWineHistoryMapper, LaoczWineHistory> implements ILaoczWineHistoryService {
    @Autowired
    private ISysHeaderMetadataService headerMetadataService;
    @Autowired
    private ILaoczBatchPotteryMappingService iLaoczBatchPotteryMappingService;
    @Autowired
    private LaoczLiquorManagementMapper laoczLiquorManagementMapper;


    /**
     * 查询酒历史列表
     *
     * @param laoczWineHistory 酒历史
     * @return 酒历史
     */
    @Override
    public List<LaoczWineHistory> selectLaoczWineHistoryList(LaoczWineHistory laoczWineHistory) {
        return baseMapper.selectLaoczWineHistoryList(laoczWineHistory);
    }

    /**
     * 查询历史信息
     *
     * @param potteryAltarId 陶坛ID
     * @param fromTime       开始时间
     * @param endTime        结束时间
     * @param detailType     操作类型
     * @param workOrderId    工单Id
     * @return
     */

    @Override
    public List<LaoczWineHistoryVO> selectLaoczWineHistory(Long potteryAltarId, String fromTime, String endTime, String detailType, String workOrderId) {
        return baseMapper.selectLaoczWineHistory(potteryAltarId, fromTime, endTime, detailType, workOrderId);
    }

    /**
     * 数据报表-淘坛操作记录
     *
     * @param potteryAltarNumber 陶坛ID
     * @param fromTime           开始时间
     * @param endTime            结束时间
     * @param liquorBatchId      批次ID
     * @param fireZoneId         防火区ID
     * @param areaId             场区ID
     * @return
     */
    @Override
    public TableDataInfoDataReportVO selectTableDataInfo(String potteryAltarNumber, String fromTime, String endTime, String liquorBatchId, Long fireZoneId, Long areaId) {
        try {
            List<LaoczWineHistoryVO> laoczWineHistoryVOS = null;
            if (StringUtils.isEmpty(potteryAltarNumber)
                    && StringUtils.isEmpty(fromTime)
                    && StringUtils.isEmpty(endTime)
                    && StringUtils.isEmpty(liquorBatchId)
                    && (fireZoneId == null || fireZoneId == 0L)
                    && (areaId == null || areaId == 0L)) {
                return getDataTable(-1L, -1L, -1L, -1L, -1L, -1L, laoczWineHistoryVOS, "PotteryReport");
            }

            try {
                PageUtils.startPage();
                laoczWineHistoryVOS = baseMapper.selectLaoczWineHistoryStatement(potteryAltarNumber, fromTime, endTime, liquorBatchId, fireZoneId, areaId);
            } finally {
                PageUtils.clearPage();
            }
            List<LaoczWineHistoryVO> laoczWineHistoryVOSList = baseMapper.selectLaoczWineHistoryStatement(potteryAltarNumber, fromTime, endTime, liquorBatchId, fireZoneId, areaId);
            Long totalOperand = (long) laoczWineHistoryVOSList.size();
            long entryOperation = laoczWineHistoryVOSList.stream()
                    .filter(history -> "入酒".equals(history.getDetailType()))
                    .count();
            long distillingOperation = laoczWineHistoryVOSList.stream()
                    .filter(history -> "出酒".equals(history.getDetailType()))
                    .count();
            long invertedJarOperationIn = laoczWineHistoryVOSList.stream()
                    .filter(history -> "倒坛入".equals(history.getDetailType()))
                    .count();
            long invertedJarOperationOut = laoczWineHistoryVOSList.stream()
                    .filter(history -> "倒坛出".equals(history.getDetailType()))
                    .count();
            long samplingOperation = laoczWineHistoryVOSList.stream()
                    .filter(history -> "取样".equals(history.getDetailType()))
                    .count();

            return getDataTable(totalOperand, entryOperation, distillingOperation, invertedJarOperationIn, invertedJarOperationOut, samplingOperation, laoczWineHistoryVOS, "PotteryReport");
        } catch (Exception e) {
            log.error("查询失败", e);
            throw new ServiceException("查询失败");
        }
    }

    @Override
    public List<LaoczWineHistoryVO> getLaoczWineHistoryTable(String fromTime, String endTime, String liquorBatchId) {
        return baseMapper.selectLaoczWineHistoryStatement(null, fromTime, endTime, liquorBatchId, null, null);
    }

    /**
     * 数据报表-陶坛操作记录详情
     *
     * @param winHisId 历史ID
     * @return
     */
    @Override
    public LaoczWineHistoryInfoVO selectLaoczWineHistoryInfo(Long winHisId) {
        try {
            PotteryAltarInfomationDInfoVO potteryAltarInfomationDInfoVO = baseMapper.selectPotteryAltarFullAltarWeight(winHisId);
            LaoczWineHistoryInfoVO laoczWineHistoryInfoVO = new LaoczWineHistoryInfoVO();
            laoczWineHistoryInfoVO.setWorkOrderId(potteryAltarInfomationDInfoVO.getWorkOrderId());
            laoczWineHistoryInfoVO.setHeadline(potteryAltarInfomationDInfoVO.getOperationType());
            laoczWineHistoryInfoVO.setCurrentWineIndustryVO(BeanUtil.copyProperties(potteryAltarInfomationDInfoVO, CurrentWineIndustryInfoVO.class));
            laoczWineHistoryInfoVO.setPotteryAltarInformationInfoVO(BeanUtil.copyProperties(potteryAltarInfomationDInfoVO, PotteryAltarInformationInfoVO.class));
            return laoczWineHistoryInfoVO;
        } catch (Exception e) {
            log.error("查询失败", e);
            throw new SecurityException("查询失败");
        }
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
    @Override
    public TableDataInfoDataReportLossVO selectLaoczWineHistoryInfoOne(String liquorBatchId, String potteryAltarNumber, Long fireZoneId, Long areaId) {

        try {
            List<LaoczWineHistoryVO> laoczWineHistoryListVO = null;
            if (StringUtils.isEmpty(liquorBatchId)
                    && (fireZoneId == null || fireZoneId == 0L)
                    && (areaId == null || areaId == 0L)
                    && StringUtils.isEmpty(potteryAltarNumber)) {
                return getDataTableLoss(-1.0, -1.0, -1.0, -1.0, -1.0, laoczWineHistoryListVO, "lossStatement");
            }
            Double totalApplications;
            Double inventoryQuantity;
            Double totalLiquorOutput;
            Double totalSampling;
            Double totalLoss;
            List<LaoczWineHistoryVO> laoczWineHistoryList = baseMapper.selectLaoczWineHistoryLossList(liquorBatchId, potteryAltarNumber, fireZoneId, areaId);
            try {
                PageUtils.startPage();
                laoczWineHistoryListVO = baseMapper.selectLaoczWineHistoryLossList(liquorBatchId, potteryAltarNumber, fireZoneId, areaId);
            } finally {
                PageUtils.clearPage();
            }
            if (CollectionUtil.isEmpty(laoczWineHistoryList)) {
                TableDataInfoDataReportLossVO tableDataInfoDataReportLossVO = new TableDataInfoDataReportLossVO();
                return Optional.ofNullable(tableDataInfoDataReportLossVO).orElseGet(TableDataInfoDataReportLossVO::new);
            }
            //申请总重量
            totalApplications = laoczWineHistoryList.stream()
                    .filter(history -> history.getPotteryAltarApplyWeight() != null)
                    .mapToDouble(LaoczWineHistoryVO::getPotteryAltarApplyWeight)
                    .sum();
            LaoczBatchPotteryMapping laoczBatchPotteryMapping = new LaoczBatchPotteryMapping();
            laoczBatchPotteryMapping.setLiquorBatchId(liquorBatchId);
            //实际陶坛状态
            List<LaoczBatchPotteryMapping> laoczBatchPotteryMappings = iLaoczBatchPotteryMappingService.selectLaoczBatchPotteryMappingList(laoczBatchPotteryMapping);
            //库存总量
            inventoryQuantity = laoczBatchPotteryMappings.stream()
                    .filter(batchPottery -> batchPottery.getActualWeight() != null)
                    .mapToDouble(LaoczBatchPotteryMapping::getActualWeight)
                    .sum();
            //出酒总量
            totalLiquorOutput = laoczWineHistoryList.stream()
                    .filter(history -> history.getDetailType().equals("出酒"))
                    .filter(history -> history.getWeighingTankWeight() != null)
                    .mapToDouble(LaoczWineHistoryVO::getWeighingTankWeight)
                    .sum();
            //取样总量
            totalSampling = laoczWineHistoryList.stream()
                    .filter(history -> history.getDetailType().equals("取样"))
                    .filter(history -> history.getSamplingWeight() != null)
                    .mapToDouble(LaoczWineHistoryVO::getSamplingWeight)
                    .sum();
            //亏损总量
            totalLoss = laoczWineHistoryList.stream()
                    .filter(history -> history.getLossWeight() != null)
                    .mapToDouble(LaoczWineHistoryVO::getLossWeight)
                    .sum();
            return getDataTableLoss(totalApplications, inventoryQuantity, totalLiquorOutput, totalSampling, totalLoss, laoczWineHistoryListVO, "lossStatement");
        } catch (Exception e) {
            log.error("查询失败", e);
            throw new ServiceException("查询失败");
        }
    }


    /**
     * 批次报表导出
     *
     * @param liquorBatchId 批次ID
     * @return
     */
    @Override
    public List<LaoczWineHistoryVO> batchLossReportExport(String liquorBatchId) {
        return baseMapper.selectLaoczWineHistoryLossList(liquorBatchId, null, null, null);
    }

    @Override
    public List<LaoczWineHistoryVO> selectOperation(String fromTime, String endTime, String detailType, String workOrderId) {
        List<LaoczWineHistoryVO> list = baseMapper.selectOperation(fromTime, endTime, detailType, workOrderId);
        return list;
    }

    @Override
    public List<LaoczWineHistory> selectDetailByWorkId(LaoczWineHistory laoczWineHistory) {
        List<LaoczWineHistory> list = baseMapper.selectLaoczWineHistoryList(laoczWineHistory);

        //赋值
        if (!CollectionUtil.isEmpty(list)) {
            //查询酒品Id
            LambdaQueryWrapper<LaoczLiquorManagement> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(LaoczLiquorManagement::getLiquorName, list.get(0).getLiquorName());
            LaoczLiquorManagement laoczLiquorManagement = laoczLiquorManagementMapper.selectOne(queryWrapper);
            list.get(0).setLiquorManagementId(laoczLiquorManagement.getLiquorManagementId());
        }
        return list;
    }


    /**
     * 响应请求分页数据
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private TableDataInfoDataReportVO getDataTable(long totalOperand,
                                                   long entryOperation,
                                                   long distillingOperation,
                                                   long invertedJarOperationIn,
                                                   long invertedJarOperationOut,
                                                   long samplingOperation,
                                                   List<?> list,
                                                   String headerName) {
        TableDataInfoDataReportVO rspData = new TableDataInfoDataReportVO();
        rspData.setTotalOperand(totalOperand);
        rspData.setEntryOperation(entryOperation);
        rspData.setDistillingOperation(distillingOperation);
        rspData.setInvertedJarOperationIn(invertedJarOperationIn);
        rspData.setInvertedJarOperationOut(invertedJarOperationOut);
        rspData.setSamplingOperation(samplingOperation);
        rspData.setCode(org.springframework.http.HttpStatus.OK.value());
        rspData.setMsg(org.springframework.http.HttpStatus.OK.getReasonPhrase());
        //列表信息组装

        PageInfo<?> pageInfo = new PageInfo<>(list == null ? new ArrayList<>() : list);
        //表头信息
        if (StringUtils.isNotBlank(headerName)) {
            SysHeaderMetadata headerMetadataPo = new SysHeaderMetadata();
            headerMetadataPo.setHeaderName(headerName);
            headerMetadataPo.setIsDelete(0L);
            List<PageHeader> pageHeaders = headerMetadataService.selectSysHeaderMetadataList(headerMetadataPo);
            if (null != pageHeaders) {
                rspData.setTableColumnList(pageHeaders);
            }
        }
        rspData.setTotal(pageInfo.getTotal());
        rspData.setRows(list == null ? new ArrayList<>() : list);
        return rspData;
    }

    /**
     * 亏损请求分页数据
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private TableDataInfoDataReportLossVO getDataTableLoss(Double totalApplications,
                                                           Double inventoryQuantity,
                                                           Double totalLiquorOutput,
                                                           Double totalSampling,
                                                           Double totalLoss,
                                                           List<?> list,
                                                           String headerName) {
        TableDataInfoDataReportLossVO rspData = new TableDataInfoDataReportLossVO();
        rspData.setTotalApplications(totalApplications);
        rspData.setInventoryQuantity(inventoryQuantity);
        rspData.setTotalLiquorOutput(totalLiquorOutput);
        rspData.setTotalLoss(totalLoss);
        rspData.setTotalSampling(totalSampling);
        rspData.setCode(org.springframework.http.HttpStatus.OK.value());
        rspData.setMsg(org.springframework.http.HttpStatus.OK.getReasonPhrase());
        //列表信息组装

        PageInfo<?> pageInfo = new PageInfo<>(list == null ? new ArrayList<>() : list);
        //表头信息
        if (StringUtils.isNotBlank(headerName)) {
            SysHeaderMetadata headerMetadataPo = new SysHeaderMetadata();
            headerMetadataPo.setHeaderName(headerName);
            headerMetadataPo.setIsDelete(0L);
            List<PageHeader> pageHeaders = headerMetadataService.selectSysHeaderMetadataList(headerMetadataPo);
            if (null != pageHeaders) {
                rspData.setTableColumnList(pageHeaders);
            }
        }
        rspData.setTotal(pageInfo.getTotal());
        rspData.setRows(list == null ? new ArrayList<>() : list);
        return rspData;
    }
}
