package com.rexel.laocz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.laocz.domain.LaoczLiquorAlarmHistory;
import com.rexel.laocz.mapper.LaoczLiquorAlarmHistoryMapper;
import com.rexel.laocz.service.ILaoczLiquorAlarmHistoryService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 老村长酒存储时间报警历史Service业务层处理
 *
 * @author grass-service
 * @date 2024-03-06
 */
@Service
public class LaoczLiquorAlarmHistoryServiceImpl extends ServiceImpl<LaoczLiquorAlarmHistoryMapper, LaoczLiquorAlarmHistory> implements ILaoczLiquorAlarmHistoryService {


    /**
     * 查询老村长酒存储时间报警历史列表
     *
     * @param laoczLiquorAlarmHistory 老村长酒存储时间报警历史
     * @return 老村长酒存储时间报警历史
     */
    @Override
    public List<LaoczLiquorAlarmHistory> selectLaoczLiquorAlarmHistoryList(LaoczLiquorAlarmHistory laoczLiquorAlarmHistory) {
        return baseMapper.selectLaoczLiquorAlarmHistoryList(laoczLiquorAlarmHistory);
    }

}
