package com.rexel.earlywarning.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rexel.common.core.domain.AjaxResult;
import com.rexel.earlywarning.domain.GrassEarlyWarningAlarmHis;
import com.rexel.earlywarning.mapper.GrassEarlyWarningAlarmHisMapper;
import com.rexel.earlywarning.service.IGrassEarlyWarningAlarmHisService;
import com.rexel.earlywarning.vo.GrassEarlyWarningAlarmHisGroupByRulesName;
import com.rexel.earlywarning.vo.GrassEarlyWarningAlarmHisGroupResult;
import com.rexel.earlywarning.vo.GrassEarlyWarningAlarmHisQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 预警规则报警历史Service业务层处理
 *
 * @author admin
 * @date 2022-01-14
 */
@Service
public class GrassEarlyWarningAlarmHisServiceImpl implements IGrassEarlyWarningAlarmHisService {
    @Autowired
    private GrassEarlyWarningAlarmHisMapper grassEarlyWarningAlarmHisMapper;

    /**
     * 查询预警规则报警历史
     *
     * @param id 预警规则报警历史ID
     * @return 预警规则报警历史
     */
    @Override
    public GrassEarlyWarningAlarmHis selectGrassEarlyWarningAlarmHisById(Long id) {
        return grassEarlyWarningAlarmHisMapper.selectGrassEarlyWarningAlarmHisById(id);
    }

    /**
     * 查询预警规则报警历史列表
     *
     * @param alarmHisQuery alarmHisQuery
     * @return 预警规则报警历史
     */
    @Override
    public List<GrassEarlyWarningAlarmHis> selectGrassEarlyWarningAlarmHisList(GrassEarlyWarningAlarmHisQuery alarmHisQuery) {
        return grassEarlyWarningAlarmHisMapper.selectGrassEarlyWarningAlarmHisList(alarmHisQuery);
    }

    /**
     * 查询预警规则报警历史聚合结果
     *
     * @param alarmHisQuery alarmHisQuery
     * @return 结果
     */
    @Override
    public List<GrassEarlyWarningAlarmHisGroupResult> selectGrassEarlyWarningAlarmHisGroup(GrassEarlyWarningAlarmHisQuery alarmHisQuery) {
        return grassEarlyWarningAlarmHisMapper.selectGrassEarlyWarningAlarmHisGroup(alarmHisQuery);
    }

    /**
     * 新增预警规则报警历史
     *
     * @param his 预警规则报警历史
     * @return 结果
     */
    @Override
    public int insertGrassEarlyWarningAlarmHis(GrassEarlyWarningAlarmHis his) {
        return grassEarlyWarningAlarmHisMapper.insertGrassEarlyWarningAlarmHis(his);
    }

    /**
     * 修改预警规则报警历史
     *
     * @param his 预警规则报警历史
     * @return 结果
     */
    @Override
    public int updateGrassEarlyWarningAlarmHis(GrassEarlyWarningAlarmHis his) {
        return grassEarlyWarningAlarmHisMapper.updateGrassEarlyWarningAlarmHis(his);
    }

    /**
     * 删除预警规则报警历史信息
     *
     * @param id 预警规则报警历史ID
     * @return 结果
     */
    @Override
    public int deleteGrassEarlyWarningAlarmHisById(Long id) {
        return grassEarlyWarningAlarmHisMapper.deleteGrassEarlyWarningAlarmHisById(id);
    }

    /**
     * 聚合预警统计饼图数据
     *
     * @param tenantId tenantId
     * @return 结果
     */
    @Override
    public AjaxResult getPieChartData(String tenantId) {
        GrassEarlyWarningAlarmHisQuery busyEarlyWarningAlarmHisQuery = new GrassEarlyWarningAlarmHisQuery();
        busyEarlyWarningAlarmHisQuery.setTenantId(tenantId);
        // 查询预警记录
        List<GrassEarlyWarningAlarmHisGroupByRulesName> list =
                grassEarlyWarningAlarmHisMapper.selectGroupByRulesNameByTenantId(busyEarlyWarningAlarmHisQuery);

        // 转换数据结构
        JSONArray jsonArray = new JSONArray();
        Long totalCount = 0L;
        for (GrassEarlyWarningAlarmHisGroupByRulesName rules : list) {
            Long alarmCount = rules.getAlarmCount();

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", rules.getRulesName());
            jsonObject.put("value", alarmCount);
            jsonArray.add(jsonObject);

            totalCount += alarmCount;
        }

        // 生成结果数据
        JSONObject result = new JSONObject();
        result.put("total", totalCount);
        result.put("data", jsonArray);
        return AjaxResult.success(result);
    }
}
