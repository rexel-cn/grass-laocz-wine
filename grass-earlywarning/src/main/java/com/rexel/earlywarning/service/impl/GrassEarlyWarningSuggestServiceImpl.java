package com.rexel.earlywarning.service.impl;

import java.util.List;

import com.rexel.common.utils.DateUtils;
import com.rexel.common.utils.SecurityUtils;
import com.rexel.earlywarning.domain.GrassEarlyWarningSuggest;
import com.rexel.earlywarning.mapper.GrassEarlyWarningSuggestMapper;
import com.rexel.earlywarning.service.IGrassEarlyWarningSuggestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 处置建议Service业务层处理
 *
 * @author admin
 * @date 2022-01-14
 */
@Service
public class GrassEarlyWarningSuggestServiceImpl implements IGrassEarlyWarningSuggestService {
    @Autowired
    private GrassEarlyWarningSuggestMapper grassEarlyWarningSuggestMapper;

    /**
     * 查询处置建议
     *
     * @param suggestId 处置建议ID
     * @return 处置建议
     */
    @Override
    public GrassEarlyWarningSuggest selectGrassEarlyWarningSuggestById(Long suggestId) {
        return grassEarlyWarningSuggestMapper.selectGrassEarlyWarningSuggestById(suggestId);
    }

    /**
     * 查询处置建议列表
     *
     * @param suggest 处置建议
     * @return 处置建议
     */
    @Override
    public List<GrassEarlyWarningSuggest> selectGrassEarlyWarningSuggestList(GrassEarlyWarningSuggest suggest) {
        suggest.setTenantId(SecurityUtils.getTenantId());
        return grassEarlyWarningSuggestMapper.selectGrassEarlyWarningSuggestList(suggest);
    }

    /**
     * 新增处置建议
     *
     * @param suggest 处置建议
     * @return 结果
     */
    @Override
    public int insertGrassEarlyWarningSuggest(GrassEarlyWarningSuggest suggest) {
        suggest.setTenantId(SecurityUtils.getTenantId());
        suggest.setCreateTime(DateUtils.getNowDate());
        suggest.setCreateBy(SecurityUtils.getUsername());
        suggest.setUpdateTime(DateUtils.getNowDate());
        suggest.setUpdateBy(SecurityUtils.getUsername());
        return grassEarlyWarningSuggestMapper.insertGrassEarlyWarningSuggest(suggest);
    }

    /**
     * 修改处置建议
     *
     * @param suggest 处置建议
     * @return 结果
     */
    @Override
    public int updateGrassEarlyWarningSuggest(GrassEarlyWarningSuggest suggest) {
        suggest.setUpdateTime(DateUtils.getNowDate());
        suggest.setUpdateBy(SecurityUtils.getUsername());
        return grassEarlyWarningSuggestMapper.updateGrassEarlyWarningSuggest(suggest);
    }

    /**
     * 删除处置建议信息
     *
     * @param suggestId 处置建议ID
     * @return 结果
     */
    @Override
    public int deleteGrassEarlyWarningSuggestById(Long suggestId) {
        return grassEarlyWarningSuggestMapper.deleteGrassEarlyWarningSuggestById(suggestId);
    }
}
