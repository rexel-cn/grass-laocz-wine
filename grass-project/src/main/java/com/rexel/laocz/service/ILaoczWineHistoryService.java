package com.rexel.laocz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rexel.laocz.domain.LaoczWineHistory;
import com.rexel.laocz.domain.vo.LaoczWineHistoryInfoVO;
import com.rexel.laocz.domain.vo.LaoczWineHistoryVO;
import com.rexel.laocz.domain.vo.TableDataInfoDataReportLossVO;
import com.rexel.laocz.domain.vo.TableDataInfoDataReportVO;

import java.util.List;

/**
 * 酒历史Service接口
 *
 * @author grass-service
 * @date 2024-03-12
 */
public interface ILaoczWineHistoryService extends IService<LaoczWineHistory> {

    /**
     * 查询酒历史列表
     *
     * @param laoczWineHistory 酒历史
     * @return 酒历史集合
     */
    List<LaoczWineHistory> selectLaoczWineHistoryList(LaoczWineHistory laoczWineHistory);

    /**
     * 查询历史信息
     *
     * @param potteryAltarId 陶坛ID
     * @param fromTime       开始时间
     * @param endTime        结束时间
     * @param detailType  操作类型
     * @param workOrderId    工单Id
     * @return
     */
    List<LaoczWineHistoryVO> selectLaoczWineHistory(Long potteryAltarId, String fromTime, String endTime, String detailType, String workOrderId);

    /**
     * 数据报表-淘坛操作记录查询1
     *
     * @param potteryAltarNumber 陶坛编号
     * @param fromTime           开始时间
     * @param endTime            结束时间
     * @param liquorBatchId      批次ID
     * @param fireZoneId         防火区ID
     * @param areaId             场区ID
     * @return
     */
    TableDataInfoDataReportVO selectTableDataInfo(String potteryAltarNumber,
                                                  String fromTime,
                                                  String endTime,
                                                  String liquorBatchId,
                                                  Long fireZoneId,
                                                  Long areaId);

    /**
     * 数据报表-淘坛操作记录查询2
     *
     * @param potteryAltarNumber 陶坛编号
     * @param fireZoneId         防火区ID
     * @param areaId             场区ID
     * @return
     */
    List<LaoczWineHistoryVO> getLaoczWineHistoryTableList(String potteryAltarNumber, Long fireZoneId, Long areaId);

    /**
     * 数据报表-导出
     *
     * @param fromTime      开始时间
     * @param endTime       结束时间
     * @param liquorBatchId 批次ID
     * @return
     */
    List<LaoczWineHistoryVO> getLaoczWineHistoryTable(String fromTime, String endTime, String liquorBatchId);


    /**
     * 数据报表-陶坛操作记录详情
     *
     * @param winHisId 历史ID
     * @return
     */
    LaoczWineHistoryInfoVO selectLaoczWineHistoryInfo(Long winHisId);

    /**
     * 批次亏损查询一
     *
     * @param liquorBatchId 批次ID
     * @return
     */
    TableDataInfoDataReportLossVO selectLaoczWineHistoryInfoOne(String liquorBatchId);

    /**
     * 批次亏损查询二
     *
     * @param potteryAltarNumber 陶坛编号
     * @param fireZoneId         防火区编号
     * @param areaId             区域编号
     * @return
     */
    List<LaoczWineHistoryVO> selectLaoczWineHistoryInfoTwo(Long potteryAltarNumber, Long fireZoneId, Long areaId);

    /**
     * 批次报表导出
     *
     * @param liquorBatchId 批次ID
     * @return
     */
    List<LaoczWineHistoryVO> batchLossReportExport(String liquorBatchId);

    List<LaoczWineHistoryVO> selectOperation(String fromTime, String endTime, String detailType, String workOrderId);

    List<LaoczWineHistory> selectDetailByWorkId(LaoczWineHistory laoczWineHistory);
}
