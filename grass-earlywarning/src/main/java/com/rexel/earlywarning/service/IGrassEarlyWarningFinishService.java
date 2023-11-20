package com.rexel.earlywarning.service;

import com.rexel.earlywarning.domain.GrassEarlyWarningFinish;

import java.util.List;

/**
 * 预警规则结束条件Service接口
 *
 * @author admin
 * @date 2022-01-14
 */
public interface IGrassEarlyWarningFinishService {
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
     * 修改预警规则结束条件
     *
     * @param finish 预警规则结束条件
     * @return 结果
     */
    int updateGrassEarlyWarningFinish(GrassEarlyWarningFinish finish);

    /**
     * 删除预警规则结束条件信息
     *
     * @param rulesId 预警规则结束条件ID
     * @return 结果
     */
    int deleteGrassEarlyWarningFinishById(Long rulesId);
}
