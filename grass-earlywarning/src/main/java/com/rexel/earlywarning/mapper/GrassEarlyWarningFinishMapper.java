package com.rexel.earlywarning.mapper;

import java.util.List;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.rexel.earlywarning.domain.GrassEarlyWarningFinish;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * 预警规则结束条件Mapper接口
 *
 * @author admin
 * @date 2022-01-14
 */
@Repository
public interface GrassEarlyWarningFinishMapper {
    /**
     * 查询预警规则结束条件
     *
     * @param rulesId 预警规则结束条件ID
     * @return 预警规则结束条件
     */
    @InterceptorIgnore(tenantLine = "on")
    List<GrassEarlyWarningFinish> selectGrassEarlyWarningFinishByRulesId(Long rulesId);

    /**
     * 查询预警规则结束条件列表
     *
     * @param finish 预警规则结束条件
     * @return 预警规则结束条件集合
     */
    List<GrassEarlyWarningFinish> selectGrassEarlyWarningFinishList(GrassEarlyWarningFinish finish);

    /**
     * 新增预警规则结束条件
     *
     * @param finish 预警规则结束条件
     * @return 结果
     */
    int insertGrassEarlyWarningFinish(GrassEarlyWarningFinish finish);

    /**
     * 新增预警规则结束条条件
     *
     * @param list list
     */
    void batchInsertGrassEarlyWarningFinish(@Param("list") List<GrassEarlyWarningFinish> list);

    /**
     * 修改预警规则结束条件
     *
     * @param finish 预警规则结束条件
     * @return 结果
     */
    int updateGrassEarlyWarningFinish(GrassEarlyWarningFinish finish);

    /**
     * 删除预警规则结束条件
     *
     * @param rulesId 预警规则结束条件ID
     * @return 结果
     */
    int deleteGrassEarlyWarningFinishByRulesId(Long rulesId);

    /**
     * 删除预警规则结束条件
     *
     * @param tenantId tenantId
     * @return 结果
     */
    int deleteGrassEarlyWarningFinishByTenantId(String tenantId);

    /**
     * 批量删除预警规则结束条件
     *
     * @param rulesIds 需要删除的数据ID
     * @return 结果
     */
    int deleteGrassEarlyWarningFinishByIds(Long[] rulesIds);
}
