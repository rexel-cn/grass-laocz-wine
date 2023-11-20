package com.rexel.earlywarning.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.earlywarning.domain.GrassEarlyWarningSuggestHis;
import com.rexel.earlywarning.mapper.GrassEarlyWarningSuggestHisMapper;
import com.rexel.earlywarning.service.IGrassEarlyWarningSuggestHisService;
import org.springframework.stereotype.Service;

/**
 * 处置建议历史Service业务层处理
 *
 * @author grass-service
 * @date 2023-10-17
 */
@Service
public class GrassEarlyWarningSuggestHisServiceImpl extends ServiceImpl<GrassEarlyWarningSuggestHisMapper, GrassEarlyWarningSuggestHis>
        implements IGrassEarlyWarningSuggestHisService {

    /**
     * 查询处置建议历史列表
     *
     * @param grassEarlyWarningSuggestHis 处置建议历史
     * @return 处置建议历史
     */
    @Override
    public List<GrassEarlyWarningSuggestHis> selectGrassEarlyWarningSuggestHisList(GrassEarlyWarningSuggestHis grassEarlyWarningSuggestHis) {
        return baseMapper.selectGrassEarlyWarningSuggestHisList(grassEarlyWarningSuggestHis);
    }
}
