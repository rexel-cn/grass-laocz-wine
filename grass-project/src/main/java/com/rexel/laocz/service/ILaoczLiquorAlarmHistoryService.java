package com.rexel.laocz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rexel.laocz.domain.LaoczLiquorAlarmHistory;
import com.rexel.laocz.domain.vo.LaoczLiquorAlarmHistoryVO;

import java.util.List;

/**
 * 老村长酒存储时间报警历史Service接口
 *
 * @author grass-service
 * @date 2024-03-06
 */
public interface ILaoczLiquorAlarmHistoryService extends IService<LaoczLiquorAlarmHistory> {

    /**
     * 查询老村长酒存储时间报警历史列表
     *
     * @param laoczLiquorAlarmHistory 老村长酒存储时间报警历史
     * @return 老村长酒存储时间报警历史集合
     */
    List<LaoczLiquorAlarmHistory> selectLaoczLiquorAlarmHistoryList(LaoczLiquorAlarmHistory laoczLiquorAlarmHistory);

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
    List<LaoczLiquorAlarmHistory> selectLaoczLiquorAlarmHistory(Long potteryAltarId, String fromTime, String endTime, String liquorBatchId, String liquorRuleName);

    /**
     * 报警查询
     *
     * @param fromTime       开始时间
     * @param endTime        结束时间
     * @param liquorBatchId   批次ID
     * @param liquorRuleName 规则名称
     * @return
     */
    List<LaoczLiquorAlarmHistoryVO> selectLaoczLiquorAlarmHistoryList(String fromTime, String endTime, String liquorBatchId, String liquorRuleName);
}
