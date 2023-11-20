package com.rexel.earlywarning.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rexel.earlywarning.domain.GrassEarlyWarningFinishHis;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 预警规则结束条件历史Mapper接口
 *
 * @author grass-service
 * @date 2023-10-17
 */
@Repository
public interface GrassEarlyWarningFinishHisMapper extends BaseMapper<GrassEarlyWarningFinishHis> {
    /**
     * 查询预警规则结束条件历史列表
     *
     * @param grassEarlyWarningFinishHis 预警规则结束条件历史
     * @return 预警规则结束条件历史集合
     */
    List<GrassEarlyWarningFinishHis> selectGrassEarlyWarningFinishHisList(GrassEarlyWarningFinishHis grassEarlyWarningFinishHis);

    /**
     * 批量新增预警规则结束条件历史
     *
     * @param grassEarlyWarningFinishHisList 预警规则结束条件历史列表
     * @return 结果
     */
    int batchGrassEarlyWarningFinishHis(List<GrassEarlyWarningFinishHis> grassEarlyWarningFinishHisList);
}
