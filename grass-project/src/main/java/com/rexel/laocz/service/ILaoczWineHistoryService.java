package com.rexel.laocz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rexel.laocz.domain.LaoczWineHistory;
import com.rexel.laocz.domain.vo.LaoczWineHistoryInfoVO;
import com.rexel.laocz.domain.vo.LaoczWineHistoryVO;
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
     * @param operationType  操作类型
     * @return
     */
    List<LaoczWineHistoryVO> selectLaoczWineHistory(Long potteryAltarId, String fromTime, String endTime, String operationType);

    /**
     * 数据报表-淘坛操作记录查询1
     *
     * @param potteryAltarId 陶坛ID
     * @param fromTime       开始时间
     * @param endTime        结束时间
     * @param liquorBatchId  批次ID
     * @param fireZoneId     防火区ID
     * @param areaId         场区ID
     * @return
     */
    TableDataInfoDataReportVO selectTableDataInfo(Long potteryAltarId, String fromTime, String endTime, String liquorBatchId, Long fireZoneId, Long areaId);

    /**
     * 数据报表-淘坛操作记录查询2
     *
     * @param potteryAltarId 陶坛ID
     * @param fireZoneId     防火区ID
     * @param areaId         场区ID
     * @return
     */
    List<LaoczWineHistoryVO> getLaoczWineHistoryTableList(Long potteryAltarId, Long fireZoneId, Long areaId);

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
}
