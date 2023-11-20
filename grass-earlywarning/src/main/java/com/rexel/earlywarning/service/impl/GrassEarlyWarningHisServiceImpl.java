package com.rexel.earlywarning.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.earlywarning.domain.GrassEarlyWarningHis;
import com.rexel.earlywarning.mapper.GrassEarlyWarningHisMapper;
import com.rexel.earlywarning.service.IGrassEarlyWarningHisService;
import org.springframework.stereotype.Service;

/**
 * 预警规则历史Service业务层处理
 *
 * @author grass-service
 * @date 2023-10-17
 */
@Service
public class GrassEarlyWarningHisServiceImpl extends ServiceImpl<GrassEarlyWarningHisMapper, GrassEarlyWarningHis>
        implements IGrassEarlyWarningHisService {

    /**
     * 查询预警规则历史列表
     *
     * @param grassEarlyWarningHis 预警规则历史
     * @return 预警规则历史
     */
    @Override
    public List<GrassEarlyWarningHis> selectGrassEarlyWarningHisList(GrassEarlyWarningHis grassEarlyWarningHis) {
        return baseMapper.selectGrassEarlyWarningHisList(grassEarlyWarningHis);
    }
}
