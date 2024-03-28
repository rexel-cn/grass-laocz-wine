package com.rexel.laocz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rexel.laocz.domain.LaoczBatchPotteryMapping;
import com.rexel.laocz.domain.vo.*;

import java.util.List;

/**
 * 陶坛与批次实时关系Service接口
 *
 * @author grass-service
 * @date 2024-03-06
 */
public interface ILaoczBatchPotteryMappingService extends IService<LaoczBatchPotteryMapping> {

    /**
     * 查询陶坛与批次实时关系列表
     *
     * @param laoczBatchPotteryMapping 陶坛与批次实时关系
     * @return 陶坛与批次实时关系集合
     */
    List<LaoczBatchPotteryMapping> selectLaoczBatchPotteryMappingList(LaoczBatchPotteryMapping laoczBatchPotteryMapping);

    /**
     * 酒液存储报表查询下方
     *
     * @param fireZoneId     防火区编号
     * @param liquorBatchId  批次ID
     * @param potteryAltarId 陶坛ID
     * @param liquorName     酒品名称
     * @param areaId         区域编号
     * @return
     */
    List<LaoczBatchPotteryMappingVO> selectTableDataInfoReportActual(Long fireZoneId, String liquorBatchId, String potteryAltarId, String liquorName, Long areaId);

    /**
     * 酒液存储报表查询
     *
     * @param fireZoneId    防火区编号
     * @param liquorBatchId 批次ID
     * @param areaId        区域编号
     * @return
     */
    List<LaoczBatchPotteryMappingVO> selectTableDataInfoReportActualList(Long fireZoneId, String liquorBatchId, Long areaId);

    /**
     * 酒液存储报表查询所有上方
     *
     * @param areaId        区域编号
     * @param fireZoneId    防火区编号
     * @param liquorBatchId 批次ID
     * @return
     */
    TableDataInfoDataReportActualVO selectTableDataInfoDataReportActualVO(Long areaId, Long fireZoneId, String liquorBatchId);

    /**
     * 看板场区概览
     *
     * @param areaId     场区编号
     * @param fireZoneId 防火区编号
     * @return
     */
    BoardDataListVO selectBoardData(Long areaId, Long fireZoneId);
    /**
     * 移动端场区概览
     *
     * @param potteryAltarNumber 陶坛编号
     * @return 详情数据
     */
    OverviewVo  getOverview(String potteryAltarNumber);

    List<BatchInfoVo> getBatchInfo(String liquorBatchId);
}
