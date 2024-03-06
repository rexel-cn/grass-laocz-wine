package com.rexel.laocz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rexel.laocz.domain.LaoczLiquorAlarmHistory;

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

}
