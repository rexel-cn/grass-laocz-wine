package com.rexel.earlywarning.service;

import com.rexel.earlywarning.domain.GrassEarlyWarningTrigger;

import java.util.List;

/**
 * 预警规则触发条件Service接口
 *
 * @author admin
 * @date 2022-01-14
 */
public interface IGrassEarlyWarningTriggerService {
    /**
     * 查询预警规则触发条件列表
     *
     * @param trigger 预警规则触发条件
     * @return 预警规则触发条件集合
     */
    List<GrassEarlyWarningTrigger> selectGrassEarlyWarningTriggerList(GrassEarlyWarningTrigger trigger);

    /**
     * 新增预警规则触发条件
     *
     * @param trigger 预警规则触发条件
     * @return 结果
     */
    int insertGrassEarlyWarningTrigger(GrassEarlyWarningTrigger trigger);

    /**
     * 修改预警规则触发条件
     *
     * @param trigger 预警规则触发条件
     * @return 结果
     */
    int updateGrassEarlyWarningTrigger(GrassEarlyWarningTrigger trigger);

    /**
     * 删除预警规则触发条件信息
     *
     * @param rulesId 预警规则触发条件ID
     * @return 结果
     */
    int deleteGrassEarlyWarningTriggerById(Long rulesId);
}
