package com.rexel.laocz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rexel.laocz.domain.LaoczPotteryOperationsLog;

import java.util.List;

/**
 * 操作记录汇总（方便查询）Service接口
 *
 * @author grass-service
 * @date 2024-03-06
 */
public interface ILaoczPotteryOperationsLogService extends IService<LaoczPotteryOperationsLog> {

    /**
     * 查询操作记录汇总（方便查询）列表
     *
     * @param laoczPotteryOperationsLog 操作记录汇总（方便查询）
     * @return 操作记录汇总（方便查询）集合
     */
    List<LaoczPotteryOperationsLog> selectLaoczPotteryOperationsLogList(LaoczPotteryOperationsLog laoczPotteryOperationsLog);

}
