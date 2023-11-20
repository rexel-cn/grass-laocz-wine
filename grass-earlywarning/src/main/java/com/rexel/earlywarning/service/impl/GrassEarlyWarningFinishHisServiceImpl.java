package com.rexel.earlywarning.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.earlywarning.domain.GrassEarlyWarningFinishHis;
import com.rexel.earlywarning.mapper.GrassEarlyWarningFinishHisMapper;
import com.rexel.earlywarning.service.IGrassEarlyWarningFinishHisService;
import org.springframework.stereotype.Service;

/**
 * 预警规则结束条件历史Service业务层处理
 *
 * @author grass-service
 * @date 2023-10-17
 */
@Service
public class GrassEarlyWarningFinishHisServiceImpl extends ServiceImpl<GrassEarlyWarningFinishHisMapper, GrassEarlyWarningFinishHis>
        implements IGrassEarlyWarningFinishHisService {

    /**
     * 查询预警规则结束条件历史列表
     *
     * @param grassEarlyWarningFinishHis 预警规则结束条件历史
     * @return 预警规则结束条件历史
     */
    @Override
    public List<GrassEarlyWarningFinishHis> selectGrassEarlyWarningFinishHisList(GrassEarlyWarningFinishHis grassEarlyWarningFinishHis) {
        return baseMapper.selectGrassEarlyWarningFinishHisList(grassEarlyWarningFinishHis);
    }
}
