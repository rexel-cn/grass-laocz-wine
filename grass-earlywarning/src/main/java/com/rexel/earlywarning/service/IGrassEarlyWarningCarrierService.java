package com.rexel.earlywarning.service;

import java.util.List;
import com.rexel.earlywarning.domain.GrassEarlyWarningCarrier;

/**
 * 预警规则运行载体Service接口
 *
 * @author admin
 * @date 2022-02-23
 */
public interface IGrassEarlyWarningCarrierService {
    /**
     * 查询预警规则运行载体列表
     *
     * @param carrier 预警规则运行载体
     * @return 预警规则运行载体集合
     */
    List<GrassEarlyWarningCarrier> selectGrassEarlyWarningCarrierList(GrassEarlyWarningCarrier carrier);

    /**
     * 新增预警规则运行载体
     *
     * @param carrier 预警规则运行载体
     * @return 结果
     */
    int insertGrassEarlyWarningCarrier(GrassEarlyWarningCarrier carrier);

    /**
     * 修改预警规则运行载体
     *
     * @param carrier 预警规则运行载体
     * @return 结果
     */
    int updateGrassEarlyWarningCarrier(GrassEarlyWarningCarrier carrier);

    /**
     * 删除预警规则运行载体信息
     *
     * @param id 预警规则运行载体ID
     * @return 结果
     */
    int deleteGrassEarlyWarningCarrierById(Long id);
}
