package com.rexel.earlywarning.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rexel.earlywarning.domain.GrassEarlyWarningJudgeHis;
import org.springframework.stereotype.Repository;

/**
 * 预警规则判断条件历史Mapper接口
 *
 * @author grass-service
 * @date 2023-10-17
 */
@Repository
public interface GrassEarlyWarningJudgeHisMapper extends BaseMapper<GrassEarlyWarningJudgeHis> {
    /**
     * 查询预警规则判断条件历史列表
     *
     * @param grassEarlyWarningJudgeHis 预警规则判断条件历史
     * @return 预警规则判断条件历史集合
     */
    List<GrassEarlyWarningJudgeHis> selectGrassEarlyWarningJudgeHisList(GrassEarlyWarningJudgeHis grassEarlyWarningJudgeHis);

    /**
     * 批量新增预警规则判断条件历史
     *
     * @param grassEarlyWarningJudgeHisList 预警规则判断条件历史列表
     * @return 结果
     */
    int batchGrassEarlyWarningJudgeHis(List<GrassEarlyWarningJudgeHis> grassEarlyWarningJudgeHisList);
}
