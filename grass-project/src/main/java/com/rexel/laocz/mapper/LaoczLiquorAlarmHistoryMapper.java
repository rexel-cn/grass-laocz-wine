package com.rexel.laocz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rexel.laocz.domain.LaoczLiquorAlarmHistory;
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

}
