package com.rexel.laocz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rexel.laocz.domain.LaoczWineHistory;
import com.rexel.laocz.domain.vo.LaoczWineHistoryVO;
import com.rexel.laocz.domain.vo.PotteryAltarInfomationDInfoVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 酒历史Mapper接口
 *
 * @author grass-service
 * @date 2024-03-12
 */
@Repository
public interface LaoczWineHistoryMapper extends BaseMapper<LaoczWineHistory> {
    /**
     * 查询酒历史列表
     *
     * @param laoczWineHistory 酒历史
     * @return 酒历史集合
     */
    List<LaoczWineHistory> selectLaoczWineHistoryList(LaoczWineHistory laoczWineHistory);

    /**
     * 批量新增酒历史
     *
     * @param laoczWineHistoryList 酒历史列表
     * @return 结果
     */
    int batchLaoczWineHistory(List<LaoczWineHistory> laoczWineHistoryList);

    /**
     * 查询历史信息
     *
     * @param potteryAltarId 陶坛ID
     * @param fromTime       开始时间
     * @param endTime        结束时间
     * @param operationType  操作类型
     * @return
     */
    List<LaoczWineHistoryVO> selectLaoczWineHistory(@Param("potteryAltarId") Long potteryAltarId, @Param("fromTime") String fromTime, @Param("endTime") String endTime, @Param("operationType") String operationType);

    /**
     * 数据报表-淘坛操作记录
     *
     * @param potteryAltarNumber 陶坛编号
     * @param fromTime       开始时间
     * @param endTime        结束时间
     * @param liquorBatchId  批次ID
     * @return
     */
    List<LaoczWineHistoryVO> selectLaoczWineHistoryStatement(@Param("potteryAltarNumber") String potteryAltarNumber,
                                                             @Param("fromTime") String fromTime,
                                                             @Param("endTime") String endTime,
                                                             @Param("liquorBatchId") String liquorBatchId,
                                                             @Param("fireZoneId") Long fireZoneId,
                                                             @Param("areaId") Long areaId);

    PotteryAltarInfomationDInfoVO selectPotteryAltarFullAltarWeight(Long winHisId);

    /**
     * 查询酒历史列表
     *
     * @param liquorBatchId 批次ID
     * @return 酒历史集合
     */
    List<LaoczWineHistoryVO> selectLaoczWineHistoryLossList(@Param("liquorBatchId") String liquorBatchId,
                                                            @Param("potteryAltarId") Long potteryAltarId,
                                                            @Param("fireZoneId") Long fireZoneId,
                                                            @Param("areaId") Long areaId);
}
