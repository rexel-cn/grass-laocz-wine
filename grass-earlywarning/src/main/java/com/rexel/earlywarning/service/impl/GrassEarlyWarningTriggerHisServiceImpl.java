package com.rexel.earlywarning.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.earlywarning.domain.GrassEarlyWarningTriggerHis;
import com.rexel.earlywarning.mapper.GrassEarlyWarningTriggerHisMapper;
import com.rexel.earlywarning.service.IGrassEarlyWarningTriggerHisService;
import org.springframework.stereotype.Service;

/**
 * 预警规则触发条件历史Service业务层处理
 *
 * @author grass-service
 * @date 2023-10-17
 */
@Service
public class GrassEarlyWarningTriggerHisServiceImpl extends ServiceImpl<GrassEarlyWarningTriggerHisMapper, GrassEarlyWarningTriggerHis>
        implements IGrassEarlyWarningTriggerHisService {

    /**
     * 查询预警规则触发条件历史列表
     *
     * @param grassEarlyWarningTriggerHis 预警规则触发条件历史
     * @return 预警规则触发条件历史
     */
    @Override
    public List<GrassEarlyWarningTriggerHis> selectGrassEarlyWarningTriggerHisList(GrassEarlyWarningTriggerHis grassEarlyWarningTriggerHis) {
        return baseMapper.selectGrassEarlyWarningTriggerHisList(grassEarlyWarningTriggerHis);
    }
}
