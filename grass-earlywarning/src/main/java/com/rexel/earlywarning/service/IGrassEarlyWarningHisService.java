package com.rexel.earlywarning.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rexel.earlywarning.domain.GrassEarlyWarningHis;

/**
 * 预警规则历史Service接口
 *
 * @author grass-service
 * @date 2023-10-17
 */
public interface IGrassEarlyWarningHisService extends IService<GrassEarlyWarningHis> {

    /**
     * 查询预警规则历史列表
     *
     * @param grassEarlyWarningHis 预警规则历史
     * @return 预警规则历史集合
     */
    List<GrassEarlyWarningHis> selectGrassEarlyWarningHisList(GrassEarlyWarningHis grassEarlyWarningHis);
}
