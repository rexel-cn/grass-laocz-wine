package com.rexel.earlywarning.service;

import java.util.List;
import com.rexel.earlywarning.domain.GrassEarlyWarningJudge;

/**
 * 预警规则判断条件Service接口
 *
 * @author admin
 * @date 2022-01-14
 */
public interface IGrassEarlyWarningJudgeService {
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
     * 修改预警规则判断条件
     *
     * @param judge 预警规则判断条件
     * @return 结果
     */
    int updateGrassEarlyWarningJudge(GrassEarlyWarningJudge judge);

    /**
     * 删除预警规则判断条件信息
     *
     * @param rulesId 预警规则判断条件ID
     * @return 结果
     */
    int deleteGrassEarlyWarningJudgeById(Long rulesId);
}
