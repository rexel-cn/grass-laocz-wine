package com.rexel.laocz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rexel.laocz.domain.LaoczPotteryOperationsLog;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 操作记录汇总（方便查询）Mapper接口
 *
 * @author grass-service
 * @date 2024-03-06
 */
@Repository
public interface LaoczPotteryOperationsLogMapper extends BaseMapper<LaoczPotteryOperationsLog> {
    /**
     * 查询操作记录汇总（方便查询）列表
     *
     * @param laoczPotteryOperationsLog 操作记录汇总（方便查询）
     * @return 操作记录汇总（方便查询）集合
     */
    List<LaoczPotteryOperationsLog> selectLaoczPotteryOperationsLogList(LaoczPotteryOperationsLog laoczPotteryOperationsLog);

    /**
     * 批量新增操作记录汇总（方便查询）
     *
     * @param laoczPotteryOperationsLogList 操作记录汇总（方便查询）列表
     * @return 结果
     */
    int batchLaoczPotteryOperationsLog(List<LaoczPotteryOperationsLog> laoczPotteryOperationsLogList);

}
