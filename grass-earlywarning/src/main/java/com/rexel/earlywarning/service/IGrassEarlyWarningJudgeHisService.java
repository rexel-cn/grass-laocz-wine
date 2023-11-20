package com.rexel.earlywarning.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rexel.earlywarning.domain.GrassEarlyWarningJudgeHis;

/**
 * 预警规则判断条件历史Service接口
 *
 * @author grass-service
 * @date 2023-10-17
 */
public interface IGrassEarlyWarningJudgeHisService extends IService<GrassEarlyWarningJudgeHis> {

    /**
     * 查询预警规则判断条件历史列表
     *
     * @param grassEarlyWarningJudgeHis 预警规则判断条件历史
     * @return 预警规则判断条件历史集合
     */
    List<GrassEarlyWarningJudgeHis> selectGrassEarlyWarningJudgeHisList(GrassEarlyWarningJudgeHis grassEarlyWarningJudgeHis);
}
