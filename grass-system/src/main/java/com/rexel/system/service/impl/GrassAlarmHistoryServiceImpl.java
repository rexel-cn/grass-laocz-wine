package com.rexel.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.common.config.PulseUrlConfig;
import com.rexel.system.domain.GrassAlarmHistory;
import com.rexel.system.domain.dto.GrassAlarmHistoryDTO;
import com.rexel.system.domain.vo.GrassAlarmGroupVO;
import com.rexel.system.domain.vo.GrassAlarmHistoryVO;
import com.rexel.system.domain.vo.tenant.AlarmVO;
import com.rexel.system.mapper.GrassAlarmHistoryMapper;
import com.rexel.system.service.IGrassAlarmHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 告警历史Service业务层处理
 *
 * @author grass-service
 * @date 2022-08-09
 */
@Service
public class GrassAlarmHistoryServiceImpl extends ServiceImpl<GrassAlarmHistoryMapper, GrassAlarmHistory> implements IGrassAlarmHistoryService {
    @Autowired
    private PulseUrlConfig pulseUrlConfig;

    /**
     * 查询告警历史列表
     *
     * @param grassAlarmHistory 告警历史
     * @return 告警历史
     */
    @Override
    public List<GrassAlarmHistory> selectGrassAlarmHistoryList(GrassAlarmHistory grassAlarmHistory) {
        return baseMapper.selectGrassAlarmHistoryList(grassAlarmHistory);
    }

    /**
     * 查询告警历史表
     *
     * @param iotAlarmHistoryQuery
     * @return
     */
    @Override
    public List<GrassAlarmHistoryVO> selectIotAlarmHistoryList(GrassAlarmHistoryDTO iotAlarmHistoryQuery) {
        List<GrassAlarmHistoryVO> grassAlarmHistories = baseMapper.selectIotAlarmHistoryList(iotAlarmHistoryQuery);
        return grassAlarmHistories;
    }

    /**
     * 告警历史数据聚合（时间，数量）
     *
     * @param iotAlarmHistoryQuery
     * @return
     */
    @Override
    public List<GrassAlarmGroupVO> selectIotAlarmHistoryGroup(GrassAlarmHistoryDTO iotAlarmHistoryQuery) {
        return baseMapper.selectIotAlarmHistoryGroup(iotAlarmHistoryQuery);
    }

    @Override
    public Boolean deleteByTenantId(String tenantId) {
        return baseMapper.deleteByTenantId(tenantId);
    }

    @Override
    public List<AlarmVO> selectAlarmCountList(int type) {

        return baseMapper.selectAlarmCountList(type);
    }

}
