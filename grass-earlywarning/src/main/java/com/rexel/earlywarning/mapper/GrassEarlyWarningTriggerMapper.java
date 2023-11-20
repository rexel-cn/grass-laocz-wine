package com.rexel.earlywarning.mapper;

import java.util.List;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.rexel.earlywarning.domain.GrassEarlyWarningTrigger;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * 预警规则触发条件Mapper接口
 *
 * @author admin
 * @date 2022-01-14
 */
@Repository
public interface GrassEarlyWarningTriggerMapper {
    /**
     * 查询预警规则触发条件
     *
     * @param rulesId 预警规则触发条件ID
     * @return 预警规则触发条件
     */
    @InterceptorIgnore(tenantLine = "on")
    List<GrassEarlyWarningTrigger> selectGrassEarlyWarningTriggerByRulesId(Long rulesId);

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
     * 新增预警规则触发条件
     *
     * @param list list
     */
    void batchInsertGrassEarlyWarningTrigger(@Param("list") List<GrassEarlyWarningTrigger> list);

    /**
     * 修改预警规则触发条件
     *
     * @param trigger 预警规则触发条件
     * @return 结果
     */
    int updateGrassEarlyWarningTrigger(GrassEarlyWarningTrigger trigger);

    /**
     * 删除预警规则触发条件
     *
     * @param rulesId 预警规则触发条件ID
     * @return 结果
     */
    int deleteGrassEarlyWarningTriggerByRulesId(Long rulesId);

    /**
     * 删除预警规则触发条件
     *
     * @param tenantId tenantId
     * @return 结果
     */
    int deleteGrassEarlyWarningTriggerByTenantId(String tenantId);

    /**
     * 批量删除预警规则触发条件
     *
     * @param rulesIds 需要删除的数据ID
     * @return 结果
     */
    int deleteGrassEarlyWarningTriggerByIds(Long[] rulesIds);
}
