package com.rexel.earlywarning.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rexel.earlywarning.domain.GrassEarlyWarningFinishHis;

/**
 * 预警规则结束条件历史Service接口
 *
 * @author grass-service
 * @date 2023-10-17
 */
public interface IGrassEarlyWarningFinishHisService extends IService<GrassEarlyWarningFinishHis> {

    /**
     * 查询预警规则结束条件历史列表
     *
     * @param grassEarlyWarningFinishHis 预警规则结束条件历史
     * @return 预警规则结束条件历史集合
     */
    List<GrassEarlyWarningFinishHis> selectGrassEarlyWarningFinishHisList(GrassEarlyWarningFinishHis grassEarlyWarningFinishHis);
}
