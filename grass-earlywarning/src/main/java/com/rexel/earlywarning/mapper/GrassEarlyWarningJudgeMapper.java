package com.rexel.earlywarning.mapper;

import java.util.List;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.rexel.earlywarning.domain.GrassEarlyWarningJudge;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * 预警规则判断条件Mapper接口
 *
 * @author admin
 * @date 2022-01-14
 */
@Repository
public interface GrassEarlyWarningJudgeMapper {
    /**
     * 查询预警规则判断条件
     *
     * @param rulesId 预警规则判断条件ID
     * @return 预警规则判断条件
     */
    @InterceptorIgnore(tenantLine = "on")
    List<GrassEarlyWarningJudge> selectGrassEarlyWarningJudgeByRulesId(Long rulesId);

    /**
     * 查询预警规则判断条件列表
     *
     * @param judge 预警规则判断条件
     * @return 预警规则判断条件集合
     */
    List<GrassEarlyWarningJudge> selectGrassEarlyWarningJudgeList(GrassEarlyWarningJudge judge);

    /**
     * 新增预警规则判断条件
     *
     * @param judge 预警规则判断条件
     * @return 结果
     */
    int insertGrassEarlyWarningJudge(GrassEarlyWarningJudge judge);

    /**
     * 新增预警规则判断条件
     *
     * @param list list
     */
    void batchInsertGrassEarlyWarningJudge(@Param("list") List<GrassEarlyWarningJudge> list);

    /**
     * 修改预警规则判断条件
     *
     * @param judge 预警规则判断条件
     * @return 结果
     */
    int updateGrassEarlyWarningJudge(GrassEarlyWarningJudge judge);

    /**
     * 删除预警规则判断条件
     *
     * @param rulesId 预警规则判断条件ID
     * @return 结果
     */
    int deleteGrassEarlyWarningJudgeByRulesId(Long rulesId);

    /**
     * 删除预警规则判断条件
     *
     * @param tenantId tenantId
     * @return 结果
     */
    int deleteGrassEarlyWarningJudgeByTenantId(String tenantId);

    /**
     * 批量删除预警规则判断条件
     *
     * @param rulesIds 需要删除的数据ID
     * @return 结果
     */
    int deleteGrassEarlyWarningJudgeByIds(Long[] rulesIds);
}
