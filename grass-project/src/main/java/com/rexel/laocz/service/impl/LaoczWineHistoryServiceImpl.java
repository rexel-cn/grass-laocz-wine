package com.rexel.laocz.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.rexel.common.core.domain.SysHeaderMetadata;
import com.rexel.common.core.page.PageHeader;
import com.rexel.common.core.service.ISysHeaderMetadataService;
import com.rexel.common.exception.ServiceException;
import com.rexel.common.utils.PageUtils;
import com.rexel.common.utils.StringUtils;
import com.rexel.laocz.domain.LaoczWineHistory;
import com.rexel.laocz.domain.vo.*;
import com.rexel.laocz.mapper.LaoczWineHistoryMapper;
import com.rexel.laocz.service.ILaoczWineHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
     * @param operationType  操作类型
     * @return
     */
    @Override
    public List<LaoczWineHistoryVO> selectLaoczWineHistory(Long potteryAltarId, String fromTime, String endTime, String operationType) {
        return baseMapper.selectLaoczWineHistory(potteryAltarId, fromTime, endTime, operationType);
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
    @Override
    public TableDataInfoDataReportVO selectTableDataInfo(Long potteryAltarId, String fromTime, String endTime, String liquorBatchId, Long fireZoneId, Long areaId) {
        try {
            List<LaoczWineHistoryVO> laoczWineHistoryVOS;
            try {
                PageUtils.startPage();
                laoczWineHistoryVOS = baseMapper.selectLaoczWineHistoryStatement(potteryAltarId, fromTime, endTime, liquorBatchId, fireZoneId, areaId);
            } finally {
                PageUtils.clearPage();
            }
            List<LaoczWineHistoryVO> laoczWineHistoryVOSList = baseMapper.selectLaoczWineHistoryStatement(potteryAltarId, fromTime, endTime, liquorBatchId, fireZoneId, areaId);
            Long totalOperand = (long) laoczWineHistoryVOSList.size();
            long entryOperation = laoczWineHistoryVOSList.stream()
                    .filter(history -> "1".equals(history.getOperationType()))
                    .count();
            long distillingOperation = laoczWineHistoryVOSList.stream()
                    .filter(history -> "2".equals(history.getOperationType()))
                    .count();
            long invertedJarOperation = laoczWineHistoryVOSList.stream()
                    .filter(history -> "3".equals(history.getOperationType()))
                    .count();
            long samplingOperation = laoczWineHistoryVOSList.stream()
                    .filter(history -> "4".equals(history.getOperationType()))
                    .count();

            return getDataTable(totalOperand, entryOperation, distillingOperation, invertedJarOperation, samplingOperation, laoczWineHistoryVOS, "PotteryReport");
        } catch (Exception e) {
            log.error("查询失败", e);
            throw new ServiceException("查询失败");
        }
    }

    /**
     * 数据报表-淘坛操作记录查询2
     *
     * @param potteryAltarId 陶坛ID
     * @param fireZoneId     防火区ID
     * @param areaId         场区ID
     * @return
     */
    @Override
    public List<LaoczWineHistoryVO> getLaoczWineHistoryTableList(Long potteryAltarId, Long fireZoneId, Long areaId) {
        return baseMapper.selectLaoczWineHistoryStatement(potteryAltarId, null, null, null, fireZoneId, areaId);
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
            laoczWineHistoryInfoVO.setCurrentWineIndustryVO(BeanUtil.copyProperties(potteryAltarInfomationDInfoVO, CurrentWineIndustryInfoVO.class));
            laoczWineHistoryInfoVO.setPotteryAltarInformationInfoVO(BeanUtil.copyProperties(potteryAltarInfomationDInfoVO, PotteryAltarInformationInfoVO.class));
            return laoczWineHistoryInfoVO;
        } catch (Exception e) {
            log.error("查询失败", e);
            throw new SecurityException("查询失败");
        }
    }

    /**
     * 响应请求分页数据
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private TableDataInfoDataReportVO getDataTable(long totalOperand, long entryOperation, long distillingOperation, long invertedJarOperation, long samplingOperation, List<?> list, String headerName) {
        TableDataInfoDataReportVO rspData = new TableDataInfoDataReportVO();
        rspData.setTotalOperand(totalOperand);
        rspData.setEntryOperation(entryOperation);
        rspData.setDistillingOperation(distillingOperation);
        rspData.setInvertedJarOperation(invertedJarOperation);
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
}
