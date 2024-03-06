package com.rexel.laocz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.laocz.domain.LaoczPotteryOperationsLog;
import com.rexel.laocz.mapper.LaoczPotteryOperationsLogMapper;
import com.rexel.laocz.service.ILaoczPotteryOperationsLogService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 操作记录汇总（方便查询）Service业务层处理
 *
 * @author grass-service
 * @date 2024-03-06
 */
@Service
public class LaoczPotteryOperationsLogServiceImpl extends ServiceImpl<LaoczPotteryOperationsLogMapper, LaoczPotteryOperationsLog> implements ILaoczPotteryOperationsLogService {


    /**
     * 查询操作记录汇总（方便查询）列表
     *
     * @param laoczPotteryOperationsLog 操作记录汇总（方便查询）
     * @return 操作记录汇总（方便查询）
     */
    @Override
    public List<LaoczPotteryOperationsLog> selectLaoczPotteryOperationsLogList(LaoczPotteryOperationsLog laoczPotteryOperationsLog) {
        return baseMapper.selectLaoczPotteryOperationsLogList(laoczPotteryOperationsLog);
    }

}
