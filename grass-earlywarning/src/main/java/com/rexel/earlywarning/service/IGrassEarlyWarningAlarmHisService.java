package com.rexel.earlywarning.service;

import com.rexel.common.core.domain.AjaxResult;
import com.rexel.earlywarning.domain.GrassEarlyWarningAlarmHis;
import com.rexel.earlywarning.vo.GrassEarlyWarningAlarmHisGroupResult;
import com.rexel.earlywarning.vo.GrassEarlyWarningAlarmHisQuery;

import java.util.List;

/**
 * 预警规则报警历史Service接口
 *
 * @author admin
 * @date 2022-01-14
 */
public interface IGrassEarlyWarningAlarmHisService {
    /**
     * 查询预警规则报警历史
     *
     * @param id 预警规则报警历史ID
     * @return 预警规则报警历史
     */
    GrassEarlyWarningAlarmHis selectGrassEarlyWarningAlarmHisById(Long id);

    /**
     * 查询预警规则报警历史列表
     *
     * @param alarmHisQuery alarmHisQuery
     * @return 预警规则报警历史集合
     */
    List<GrassEarlyWarningAlarmHis> selectGrassEarlyWarningAlarmHisList(GrassEarlyWarningAlarmHisQuery alarmHisQuery);

    /**
     * 查询预警规则报警历史聚合结果
     *
     * @param alarmHisQuery alarmHisQuery
     * @return 结果
     */
    List<GrassEarlyWarningAlarmHisGroupResult> selectGrassEarlyWarningAlarmHisGroup(GrassEarlyWarningAlarmHisQuery alarmHisQuery);

    /**
     * 新增预警规则报警历史
     *
     * @param his 预警规则报警历史
     * @return 结果
     */
    int insertGrassEarlyWarningAlarmHis(GrassEarlyWarningAlarmHis his);

    /**
     * 修改预警规则报警历史
     *
     * @param his 预警规则报警历史
     * @return 结果
     */
    int updateGrassEarlyWarningAlarmHis(GrassEarlyWarningAlarmHis his);

    /**
     * 删除预警规则报警历史信息
     *
     * @param id 预警规则报警历史ID
     * @return 结果
     */
    int deleteGrassEarlyWarningAlarmHisById(Long id);

    /**
     * 聚合预警统计饼图数据
     *
     * @param tenantId tenantId
     * @return 结果
     */
    AjaxResult getPieChartData(String tenantId);
}
