package com.rexel.laocz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rexel.laocz.domain.LaoczLiquorAlarmHistory;
import com.rexel.laocz.domain.vo.LaoczLiquorAlarmHistoryVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 老村长酒存储时间报警历史Mapper接口
 *
 * @author grass-service
 * @date 2024-03-06
 */
@Repository
public interface LaoczLiquorAlarmHistoryMapper extends BaseMapper<LaoczLiquorAlarmHistory> {
    /**
     * 查询老村长酒存储时间报警历史列表
     *
     * @param laoczLiquorAlarmHistory 老村长酒存储时间报警历史
     * @return 老村长酒存储时间报警历史集合
     */
    List<LaoczLiquorAlarmHistory> selectLaoczLiquorAlarmHistoryList(LaoczLiquorAlarmHistory laoczLiquorAlarmHistory);

    /**
     * 批量新增老村长酒存储时间报警历史
     *
     * @param laoczLiquorAlarmHistoryList 老村长酒存储时间报警历史列表
     * @return 结果
     */
    int batchLaoczLiquorAlarmHistory(List<LaoczLiquorAlarmHistory> laoczLiquorAlarmHistoryList);

    /**
     * 查询报警历史信息
     *
     * @param potteryAltarId 陶坛ID
     * @param fromTime       开始时间
     * @param endTime        结束时间
     * @param liquorBatchId   批次ID
     * @param liquorRuleName 规则名称
     * @return
     */
    List<LaoczLiquorAlarmHistory> selectLaoczLiquorAlarmHistory(@Param("potteryAltarId") Long potteryAltarId, @Param("fromTime") String fromTime, @Param("endTime") String endTime, @Param("liquorBatchId") String liquorBatchId, @Param("liquorRuleName") String liquorRuleName);

    /**
     * 报警查询
     *
     * @param fromTime       开始时间
     * @param endTime        结束时间
     * @param liquorBatchId   批次ID
     * @param liquorRuleName 规则名称
     * @return
     */
    List<LaoczLiquorAlarmHistoryVO> selectLaoczLiquorAlarmHistoryVOList(@Param("fromTime") String fromTime, @Param("endTime") String endTime, @Param("liquorBatchId") String liquorBatchId, @Param("liquorRuleName") String liquorRuleName);
    /**
     * 获取报警详情
     *
     * @return
     */
    LaoczLiquorAlarmHistoryVO selectAlarmInfo(Long liquorAlarmHistoryId);
}
