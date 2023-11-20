package com.rexel.earlywarning.service.impl;

import java.util.List;

import com.rexel.common.utils.DateUtils;
import com.rexel.common.utils.SecurityUtils;
import com.rexel.earlywarning.domain.GrassEarlyWarningFinish;
import com.rexel.earlywarning.mapper.GrassEarlyWarningFinishMapper;
import com.rexel.earlywarning.service.IGrassEarlyWarningFinishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 预警规则结束条件Service业务层处理
 *
 * @author admin
 * @date 2022-01-14
 */
@Service
public class GrassEarlyWarningFinishServiceImpl implements IGrassEarlyWarningFinishService {
    @Autowired
    private GrassEarlyWarningFinishMapper grassEarlyWarningFinishMapper;

    /**
     * 查询预警规则结束条件列表
     *
     * @param finish 预警规则结束条件
     * @return 预警规则结束条件
     */
    @Override
    public List<GrassEarlyWarningFinish> selectGrassEarlyWarningFinishList(GrassEarlyWarningFinish finish) {
        return grassEarlyWarningFinishMapper.selectGrassEarlyWarningFinishList(finish);
    }

    /**
     * 新增预警规则结束条件
     *
     * @param finish 预警规则结束条件
     * @return 结果
     */
    @Override
    public int insertGrassEarlyWarningFinish(GrassEarlyWarningFinish finish) {
        finish.setTenantId(SecurityUtils.getTenantId());
        finish.setCreateTime(DateUtils.getNowDate());
        finish.setCreateBy(SecurityUtils.getUsername());
        finish.setUpdateTime(DateUtils.getNowDate());
        finish.setUpdateBy(SecurityUtils.getUsername());
        return grassEarlyWarningFinishMapper.insertGrassEarlyWarningFinish(finish);
    }

    /**
     * 修改预警规则结束条件
     *
     * @param finish 预警规则结束条件
     * @return 结果
     */
    @Override
    public int updateGrassEarlyWarningFinish(GrassEarlyWarningFinish finish) {
        finish.setUpdateTime(DateUtils.getNowDate());
        finish.setUpdateBy(SecurityUtils.getUsername());
        return grassEarlyWarningFinishMapper.updateGrassEarlyWarningFinish(finish);
    }

    /**
     * 删除预警规则结束条件信息
     *
     * @param rulesId 预警规则结束条件ID
     * @return 结果
     */
    @Override
    public int deleteGrassEarlyWarningFinishById(Long rulesId) {
        return grassEarlyWarningFinishMapper.deleteGrassEarlyWarningFinishByRulesId(rulesId);
    }
}
