package com.rexel.earlywarning.service.impl;

import java.util.List;

import com.rexel.common.utils.DateUtils;
import com.rexel.common.utils.SecurityUtils;
import com.rexel.earlywarning.domain.GrassEarlyWarningJudge;
import com.rexel.earlywarning.mapper.GrassEarlyWarningJudgeMapper;
import com.rexel.earlywarning.service.IGrassEarlyWarningJudgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 预警规则判断条件Service业务层处理
 *
 * @author admin
 * @date 2022-01-14
 */
@Service
public class GrassEarlyWarningJudgeServiceImpl implements IGrassEarlyWarningJudgeService {
    @Autowired
    private GrassEarlyWarningJudgeMapper grassEarlyWarningJudgeMapper;

    /**
     * 查询预警规则判断条件列表
     *
     * @param judge 预警规则判断条件
     * @return 预警规则判断条件
     */
    @Override
    public List<GrassEarlyWarningJudge> selectGrassEarlyWarningJudgeList(GrassEarlyWarningJudge judge) {
        return grassEarlyWarningJudgeMapper.selectGrassEarlyWarningJudgeList(judge);
    }

    /**
     * 新增预警规则判断条件
     *
     * @param judge 预警规则判断条件
     * @return 结果
     */
    @Override
    public int insertGrassEarlyWarningJudge(GrassEarlyWarningJudge judge) {
        judge.setTenantId(SecurityUtils.getTenantId());
        judge.setCreateTime(DateUtils.getNowDate());
        judge.setCreateBy(SecurityUtils.getUsername());
        judge.setUpdateTime(DateUtils.getNowDate());
        judge.setUpdateBy(SecurityUtils.getUsername());
        return grassEarlyWarningJudgeMapper.insertGrassEarlyWarningJudge(judge);
    }

    /**
     * 修改预警规则判断条件
     *
     * @param judge 预警规则判断条件
     * @return 结果
     */
    @Override
    public int updateGrassEarlyWarningJudge(GrassEarlyWarningJudge judge) {
        judge.setUpdateTime(DateUtils.getNowDate());
        judge.setUpdateBy(SecurityUtils.getUsername());
        return grassEarlyWarningJudgeMapper.updateGrassEarlyWarningJudge(judge);
    }

    /**
     * 删除预警规则判断条件信息
     *
     * @param rulesId 预警规则判断条件ID
     * @return 结果
     */
    @Override
    public int deleteGrassEarlyWarningJudgeById(Long rulesId) {
        return grassEarlyWarningJudgeMapper.deleteGrassEarlyWarningJudgeByRulesId(rulesId);
    }
}
