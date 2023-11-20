package com.rexel.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rexel.system.domain.GrassAlarmHistory;
import com.rexel.system.domain.dto.GrassAlarmHistoryDTO;
import com.rexel.system.domain.vo.GrassAlarmGroupVO;
import com.rexel.system.domain.vo.GrassAlarmHistoryVO;
import com.rexel.system.domain.vo.tenant.AlarmVO;

import java.util.List;

/**
 * 告警历史Service接口
 *
 * @author grass-service
 * @date 2022-08-09
 */
public interface IGrassAlarmHistoryService extends IService<GrassAlarmHistory> {

    /**
     * 查询告警历史列表
     *
     * @param grassAlarmHistory 告警历史
     * @return 告警历史集合
     */
    List<GrassAlarmHistory> selectGrassAlarmHistoryList(GrassAlarmHistory grassAlarmHistory);

    /**
     * 查询告警历史表
     *
     * @param iotAlarmHistoryQuery
     * @return
     */
    List<GrassAlarmHistoryVO> selectIotAlarmHistoryList(GrassAlarmHistoryDTO iotAlarmHistoryQuery);

    /**
     * 告警历史数据聚合（时间，数量）
     *
     * @param iotAlarmHistoryQuery
     * @return
     */
    List<GrassAlarmGroupVO> selectIotAlarmHistoryGroup(GrassAlarmHistoryDTO iotAlarmHistoryQuery);


    Boolean deleteByTenantId(String tenantId);

    List<AlarmVO> selectAlarmCountList(int type);
}
