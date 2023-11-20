package com.rexel.earlywarning.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.rexel.earlywarning.domain.GrassEarlyWarningCarrier;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 预警规则运行载体Mapper接口
 *
 * @author admin
 * @date 2022-02-23
 */
@Repository
public interface GrassEarlyWarningCarrierMapper {
    /**
     * 查询预警规则运行载体
     *
     * @param rulesId rulesId
     * @return 预警规则运行载体
     */
    @InterceptorIgnore(tenantLine = "on")
    List<GrassEarlyWarningCarrier> selectGrassEarlyWarningCarrierListByRulesId(Long rulesId);

    /**
     * 查询预警规则运行载体列表
     *
     * @param carrier 预警规则运行载体
     * @return 预警规则运行载体集合
     */
    @InterceptorIgnore(tenantLine = "on")
    List<GrassEarlyWarningCarrier> selectGrassEarlyWarningCarrierList(GrassEarlyWarningCarrier carrier);

    /**
     * 新增预警规则运行载体
     *
     * @param carrier 预警规则运行载体
     * @return 结果
     */
    int insertGrassEarlyWarningCarrier(GrassEarlyWarningCarrier carrier);

    /**
     * 新增预警规则运行载体
     *
     * @param list list
     */
    void batchInsertGrassEarlyWarningCarrier(@Param("list") List<GrassEarlyWarningCarrier> list);

    /**
     * 修改预警规则运行载体
     *
     * @param carrier 预警规则运行载体
     * @return 结果
     */
    int updateGrassEarlyWarningCarrier(GrassEarlyWarningCarrier carrier);

    /**
     * 删除预警规则运行载体
     *
     * @param id 预警规则运行载体ID
     * @return 结果
     */
    int deleteGrassEarlyWarningCarrierById(Long id);

    /**
     * 删除预警规则运行载体
     *
     * @param rulesId rulesId
     * @return 结果
     */
    int deleteGrassEarlyWarningCarrierByRulesId(Long rulesId);

    /**
     * 批量删除预警规则运行载体
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteGrassEarlyWarningCarrierByIds(Long[] ids);
}
