package com.rexel.system.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rexel.system.domain.GrassAlarmHistory;
import com.rexel.system.domain.dto.GrassAlarmHistoryDTO;
import com.rexel.system.domain.vo.GrassAlarmGroupVO;
import com.rexel.system.domain.vo.GrassAlarmHistoryVO;
import com.rexel.system.domain.vo.tenant.AlarmVO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 告警历史Mapper接口
 *
 * @author grass-service
 * @date 2022-08-09
 */
@Repository
public interface GrassAlarmHistoryMapper extends BaseMapper<GrassAlarmHistory> {
    /**
     * 查询告警历史列表
     *
     * @param grassAlarmHistory 告警历史
     * @return 告警历史集合
     */
    List<GrassAlarmHistory> selectGrassAlarmHistoryList(GrassAlarmHistory grassAlarmHistory);

    List<GrassAlarmGroupVO> selectIotAlarmHistoryGroup(GrassAlarmHistoryDTO iotAlarmHistoryQuery);

    List<GrassAlarmHistoryVO> selectIotAlarmHistoryList(GrassAlarmHistoryDTO iotAlarmHistoryQuery);

    @InterceptorIgnore(tenantLine = "on")
    Boolean deleteByTenantId(String tenantId);

    List<AlarmVO> selectAlarmCountList(int type);
}
