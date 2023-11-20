package com.rexel.earlywarning.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rexel.earlywarning.domain.GrassEarlyWarningTriggerHis;

/**
 * 预警规则触发条件历史Service接口
 *
 * @author grass-service
 * @date 2023-10-17
 */
public interface IGrassEarlyWarningTriggerHisService extends IService<GrassEarlyWarningTriggerHis> {

    /**
     * 查询预警规则触发条件历史列表
     *
     * @param grassEarlyWarningTriggerHis 预警规则触发条件历史
     * @return 预警规则触发条件历史集合
     */
    List<GrassEarlyWarningTriggerHis> selectGrassEarlyWarningTriggerHisList(GrassEarlyWarningTriggerHis grassEarlyWarningTriggerHis);
}
