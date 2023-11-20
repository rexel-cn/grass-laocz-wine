package com.rexel.earlywarning.service.impl;

import com.rexel.common.utils.DateUtils;
import com.rexel.common.utils.SecurityUtils;
import com.rexel.earlywarning.domain.GrassEarlyWarningTrigger;
import com.rexel.earlywarning.mapper.GrassEarlyWarningTriggerMapper;
import com.rexel.earlywarning.service.IGrassEarlyWarningTriggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 预警规则触发条件Service业务层处理
 *
 * @author admin
 * @date 2022-01-14
 */
@Service
public class GrassEarlyWarningTriggerServiceImpl implements IGrassEarlyWarningTriggerService {
    @Autowired
    private GrassEarlyWarningTriggerMapper busyEarlyWarningTriggerMapper;

    /**
     * 查询预警规则触发条件列表
     *
     * @param trigger 预警规则触发条件
     * @return 预警规则触发条件
     */
    @Override
    public List<GrassEarlyWarningTrigger> selectGrassEarlyWarningTriggerList(GrassEarlyWarningTrigger trigger) {
        return busyEarlyWarningTriggerMapper.selectGrassEarlyWarningTriggerList(trigger);
    }

    /**
     * 新增预警规则触发条件
     *
     * @param trigger 预警规则触发条件
     * @return 结果
     */
    @Override
    public int insertGrassEarlyWarningTrigger(GrassEarlyWarningTrigger trigger) {
        trigger.setTenantId(SecurityUtils.getTenantId());
        trigger.setCreateTime(DateUtils.getNowDate());
        trigger.setCreateBy(SecurityUtils.getUsername());
        trigger.setUpdateTime(DateUtils.getNowDate());
        trigger.setUpdateBy(SecurityUtils.getUsername());
        return busyEarlyWarningTriggerMapper.insertGrassEarlyWarningTrigger(trigger);
    }

    /**
     * 修改预警规则触发条件
     *
     * @param trigger 预警规则触发条件
     * @return 结果
     */
    @Override
    public int updateGrassEarlyWarningTrigger(GrassEarlyWarningTrigger trigger) {
        trigger.setUpdateTime(DateUtils.getNowDate());
        trigger.setUpdateBy(SecurityUtils.getUsername());
        return busyEarlyWarningTriggerMapper.updateGrassEarlyWarningTrigger(trigger);
    }

    /**
     * 删除预警规则触发条件信息
     *
     * @param rulesId 预警规则触发条件ID
     * @return 结果
     */
    @Override
    public int deleteGrassEarlyWarningTriggerById(Long rulesId) {
        return busyEarlyWarningTriggerMapper.deleteGrassEarlyWarningTriggerByRulesId(rulesId);
    }
}
