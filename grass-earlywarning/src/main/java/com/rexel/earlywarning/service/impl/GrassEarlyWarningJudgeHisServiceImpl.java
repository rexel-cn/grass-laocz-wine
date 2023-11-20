package com.rexel.earlywarning.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.earlywarning.domain.GrassEarlyWarningJudgeHis;
import com.rexel.earlywarning.mapper.GrassEarlyWarningJudgeHisMapper;
import com.rexel.earlywarning.service.IGrassEarlyWarningJudgeHisService;
import org.springframework.stereotype.Service;

/**
 * 预警规则判断条件历史Service业务层处理
 *
 * @author grass-service
 * @date 2023-10-17
 */
@Service
public class GrassEarlyWarningJudgeHisServiceImpl extends ServiceImpl<GrassEarlyWarningJudgeHisMapper, GrassEarlyWarningJudgeHis>
        implements IGrassEarlyWarningJudgeHisService {

    /**
     * 查询预警规则判断条件历史列表
     *
     * @param grassEarlyWarningJudgeHis 预警规则判断条件历史
     * @return 预警规则判断条件历史
     */
    @Override
    public List<GrassEarlyWarningJudgeHis> selectGrassEarlyWarningJudgeHisList(GrassEarlyWarningJudgeHis grassEarlyWarningJudgeHis) {
        return baseMapper.selectGrassEarlyWarningJudgeHisList(grassEarlyWarningJudgeHis);
    }
}
