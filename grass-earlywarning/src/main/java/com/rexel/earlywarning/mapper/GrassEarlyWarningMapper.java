package com.rexel.earlywarning.mapper;

import java.util.List;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.rexel.earlywarning.domain.GrassEarlyWarning;
import org.springframework.stereotype.Repository;

/**
 * 预警规则Mapper接口
 *
 * @author admin
 * @date 2022-01-14
 */
@Repository
public interface GrassEarlyWarningMapper {
    /**
     * 查询预警规则
     *
     * @param rulesId 预警规则ID
     * @return 预警规则
     */
    @InterceptorIgnore(tenantLine = "on")
    GrassEarlyWarning selectGrassEarlyWarningById(Long rulesId);

    /**
     * 查询预警规则列表
     *
     * @param grassEarlyWarning 预警规则
     * @return 预警规则集合
     */
    @InterceptorIgnore(tenantLine = "on")
    List<GrassEarlyWarning> selectGrassEarlyWarningList(GrassEarlyWarning grassEarlyWarning);

    /**
     * 新增预警规则
     *
     * @param grassEarlyWarning 预警规则
     * @return 结果
     */
    int insertGrassEarlyWarning(GrassEarlyWarning grassEarlyWarning);

    /**
     * 修改预警规则
     *
     * @param grassEarlyWarning 预警规则
     * @return 结果
     */
    int updateGrassEarlyWarning(GrassEarlyWarning grassEarlyWarning);

    /**
     * 删除预警规则
     *
     * @param rulesId 预警规则ID
     * @return 结果
     */
    int deleteGrassEarlyWarningByRulesId(Long rulesId);

    /**
     * 删除预警规则
     *
     * @param tenantId tenantId
     * @return 结果
     */
    int deleteGrassEarlyWarningByTenantId(String tenantId);

    /**
     * 批量删除预警规则
     *
     * @param rulesIds 需要删除的数据ID
     * @return 结果
     */
    int deleteGrassEarlyWarningByIds(Long[] rulesIds);
}
