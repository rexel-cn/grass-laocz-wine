package com.rexel.earlywarning.mapper;

import com.rexel.earlywarning.domain.GrassEarlyWarningAlarmHis;
import com.rexel.earlywarning.vo.GrassEarlyWarningAlarmHisGroupByRulesName;
import com.rexel.earlywarning.vo.GrassEarlyWarningAlarmHisGroupResult;
import com.rexel.earlywarning.vo.GrassEarlyWarningAlarmHisQuery;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * 预警规则报警历史Mapper接口
 *
 * @author admin
 * @date 2022-01-14
 */
@Repository
public interface GrassEarlyWarningAlarmHisMapper {
    /**
     * 查询预警规则报警历史
     *
     * @param hisId 预警规则报警历史ID
     * @return 预警规则报警历史
     */
    GrassEarlyWarningAlarmHis selectGrassEarlyWarningAlarmHisById(Long hisId);

    /**
     * 查询预警规则报警历史列表
     *
     * @param alarmHisQuery 预警规则报警历史
     * @return 预警规则报警历史集合
     */
    List<GrassEarlyWarningAlarmHis> selectGrassEarlyWarningAlarmHisList(GrassEarlyWarningAlarmHisQuery alarmHisQuery);

    /**
     * 查询预警规则报警历史聚合结果
     *
     * @param alarmHisQuery 预警规则报警历史
     * @return 预警规则报警历史集合
     */
    List<GrassEarlyWarningAlarmHisGroupResult> selectGrassEarlyWarningAlarmHisGroup(GrassEarlyWarningAlarmHisQuery alarmHisQuery);

    /**
     * 聚合预警统计饼图数据
     *
     * @param alarmHisQuery tenantId
     * @return 结果
     */
    List<GrassEarlyWarningAlarmHisGroupByRulesName> selectGroupByRulesNameByTenantId(GrassEarlyWarningAlarmHisQuery alarmHisQuery);

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
     * 删除预警规则报警历史
     *
     * @param hisId 预警规则报警历史ID
     * @return 结果
     */
    int deleteGrassEarlyWarningAlarmHisById(Long hisId);

    /**
     * 删除预警规则报警历史
     *
     * @param rulesId rulesId
     * @return 结果
     */
    int deleteGrassEarlyWarningAlarmHisByRulesId(Long rulesId);

    /**
     * 批量删除预警规则报警历史
     *
     * @param hisIds 需要删除的数据ID
     * @return 结果
     */
    int deleteGrassEarlyWarningAlarmHisByIds(Long[] hisIds);
}
