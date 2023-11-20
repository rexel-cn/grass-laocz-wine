package com.rexel.earlywarning.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rexel.earlywarning.domain.GrassEarlyWarningTriggerHis;
import org.springframework.stereotype.Repository;

/**
 * 预警规则触发条件历史Mapper接口
 *
 * @author grass-service
 * @date 2023-10-17
 */
@Repository
public interface GrassEarlyWarningTriggerHisMapper extends BaseMapper<GrassEarlyWarningTriggerHis> {
    /**
     * 查询预警规则触发条件历史列表
     *
     * @param grassEarlyWarningTriggerHis 预警规则触发条件历史
     * @return 预警规则触发条件历史集合
     */
    List<GrassEarlyWarningTriggerHis> selectGrassEarlyWarningTriggerHisList(GrassEarlyWarningTriggerHis grassEarlyWarningTriggerHis);

    /**
     * 批量新增预警规则触发条件历史
     *
     * @param grassEarlyWarningTriggerHisList 预警规则触发条件历史列表
     * @return 结果
     */
    int batchGrassEarlyWarningTriggerHis(List<GrassEarlyWarningTriggerHis> grassEarlyWarningTriggerHisList);
}
