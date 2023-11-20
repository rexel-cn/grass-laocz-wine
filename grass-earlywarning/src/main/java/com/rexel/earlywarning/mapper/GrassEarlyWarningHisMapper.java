package com.rexel.earlywarning.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rexel.earlywarning.domain.GrassEarlyWarningHis;
import org.springframework.stereotype.Repository;

/**
 * 预警规则历史Mapper接口
 *
 * @author grass-service
 * @date 2023-10-17
 */
@Repository
public interface GrassEarlyWarningHisMapper extends BaseMapper<GrassEarlyWarningHis> {
    /**
     * 查询预警规则历史列表
     *
     * @param grassEarlyWarningHis 预警规则历史
     * @return 预警规则历史集合
     */
    List<GrassEarlyWarningHis> selectGrassEarlyWarningHisList(GrassEarlyWarningHis grassEarlyWarningHis);

    /**
     * 批量新增预警规则历史
     *
     * @param grassEarlyWarningHisList 预警规则历史列表
     * @return 结果
     */
    int batchGrassEarlyWarningHis(List<GrassEarlyWarningHis> grassEarlyWarningHisList);
}
