package com.rexel.laocz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.rexel.common.core.domain.SysHeaderMetadata;
import com.rexel.common.core.page.PageHeader;
import com.rexel.common.core.service.ISysHeaderMetadataService;
import com.rexel.common.exception.ServiceException;
import com.rexel.common.utils.PageUtils;
import com.rexel.common.utils.StringUtils;
import com.rexel.laocz.domain.LaoczBatchPotteryMapping;
import com.rexel.laocz.domain.vo.*;
import com.rexel.laocz.mapper.LaoczBatchPotteryMappingMapper;
import com.rexel.laocz.service.ILaoczBatchPotteryMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 陶坛与批次实时关系Service业务层处理
 *
 * @author grass-service
 * @date 2024-03-06
 */
@Service
public class LaoczBatchPotteryMappingServiceImpl extends ServiceImpl<LaoczBatchPotteryMappingMapper, LaoczBatchPotteryMapping> implements ILaoczBatchPotteryMappingService {
    @Autowired
    private ISysHeaderMetadataService headerMetadataService;

    /**
     * 查询陶坛与批次实时关系列表
     *
     * @param laoczBatchPotteryMapping 陶坛与批次实时关系
     * @return 陶坛与批次实时关系
     */
    @Override
    public List<LaoczBatchPotteryMapping> selectLaoczBatchPotteryMappingList(LaoczBatchPotteryMapping laoczBatchPotteryMapping) {
        return baseMapper.selectLaoczBatchPotteryMappingList(laoczBatchPotteryMapping);
    }

    @Override
    public List<LaoczBatchPotteryMappingVO> selectTableDataInfoReportActualList(Long fireZoneId, String liquorBatchId, Long areaId) {
        return baseMapper.selectTableDataInfoReportActual(fireZoneId, liquorBatchId, null, null, areaId);
    }

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
    @Override
    public TableDataInfoDataReportActualVO selectTableDataInfoDataReportActualVO(Long areaId, Long fireZoneId, String liquorBatchId, String potteryAltarNumber, String liquorName) {
        try {
            List<LaoczBatchPotteryMappingVO> laoczBatchPotteryMappingVOS;
            try {
                PageUtils.startPage();
                laoczBatchPotteryMappingVOS = baseMapper.selectTableDataInfoReportActual(fireZoneId, liquorBatchId, potteryAltarNumber, liquorName, areaId);
            } finally {
                PageUtils.clearPage();
            }
            List<LaoczBatchPotteryMappingVO> laoczBatchPotteryMappingVOS1 = baseMapper.selectTableDataInfoReportActual(fireZoneId, liquorBatchId, potteryAltarNumber, liquorName, areaId);
            double totalActualWeight = laoczBatchPotteryMappingVOS.stream()
                    .filter(Objects::nonNull) // 排除VO对象为null的情况
                    .filter(mappingVO -> mappingVO.getActualWeight() != null) // 排除实际重量为null的记录
                    .mapToDouble(LaoczBatchPotteryMappingVO::getActualWeight)
                    .sum();
            return getDataTable(totalActualWeight, laoczBatchPotteryMappingVOS, "LiquorStorage");
        } catch (Exception e) {
            log.error("查询失败", e);
            throw new ServiceException("查询失败");
        }
    }

    /**
     * 看板场区概览
     *
     * @param areaId     场区编号
     * @param fireZoneId 防火区编号
     * @return
     */
    @Override
    public BoardDataListVO selectBoardData(Long areaId, Long fireZoneId) {
        List<BoardDataVO> boardDataVOS;
        try {
            PageUtils.startPage();
            boardDataVOS = baseMapper.selectBoardData(areaId, fireZoneId);
        } finally {
            PageUtils.clearPage();
        }
        BoardDataListVO boardDataListVO = new BoardDataListVO();
        boardDataListVO.setBoardDataVOList(boardDataVOS);
        boardDataListVO.setTableTotal(baseMapper.selectBoardData(areaId, fireZoneId).size());
        return boardDataListVO;
    }

    /**
     * 移动端场区概览
     *
     * @param potteryAltarNumber 陶坛编号
     * @return 详情数据
     */
    @Override
    public OverviewVo getOverview(String potteryAltarNumber) {
        OverviewVo overviewVo = baseMapper.selectOverviewVo(potteryAltarNumber);
        return overviewVo;
    }

    @Override
    public List<BatchInfoVo> getBatchInfo(String liquorBatchId) {
        List<BatchInfoVo> batchInfoVos = baseMapper.selectBatchInfo(liquorBatchId);
        return batchInfoVos;
    }

    /**
     * 响应请求分页数据
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private TableDataInfoDataReportActualVO getDataTable(Double actualWeight, List<?> list, String headerName) {
        TableDataInfoDataReportActualVO rspData = new TableDataInfoDataReportActualVO();
        rspData.setTotalStorage(actualWeight);
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
