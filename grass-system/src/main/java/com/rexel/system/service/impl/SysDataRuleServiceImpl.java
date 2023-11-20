package com.rexel.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.common.core.domain.entity.SysDataRule;
import com.rexel.datarule.handler.DataRuleHandler;
import com.rexel.system.domain.SysRoleDataRule;
import com.rexel.system.mapper.SysDataRuleMapper;
import com.rexel.system.mapper.SysRoleDataRuleMapper;
import com.rexel.system.service.ISysDataRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;


/**
 * @version V1.0
 * @package sys
 * @title: 数据权限表控制器
 * @description: 数据权限表控制器
 * @author: 未知
 * @date: 2019-11-29 06:05:01
 * @copyright: Inc. All rights reserved.
 */
@Transactional
@Service
public class SysDataRuleServiceImpl extends ServiceImpl<SysDataRuleMapper, SysDataRule> implements ISysDataRuleService {

    //所有数据更新都需要刷新数据权限
    @Autowired
    private DataRuleHandler dataRuleHandler;
    @Autowired
    private SysRoleDataRuleMapper roleDataRuleMapper;

    @Override
    public boolean updateById(SysDataRule entity) {
        boolean result = super.updateById(entity);
        dataRuleHandler.refreshDataRule(entity.getId());
        return result;
    }

    @Override
    public boolean save(SysDataRule entity) {
        boolean save = super.save(entity);
        dataRuleHandler.refreshDataRule(entity.getId());
        return save;
    }

    @Override
    public boolean saveOrUpdate(SysDataRule entity) {
        boolean result = super.saveOrUpdate(entity);
        dataRuleHandler.refreshDataRule(entity.getId());
        return result;
    }

    @Override
    public boolean removeById(Serializable id) {
        boolean result = super.removeById(id);
        roleDataRuleMapper.delete(new QueryWrapper<SysRoleDataRule>().eq("role_id", id));
        dataRuleHandler.deleteDataRule(String.valueOf(id));
        return result;
    }

    @Override
    public boolean removeByIds(Collection<? extends Serializable> idList) {
        boolean result = super.removeByIds(idList);
        idList.forEach(id -> {
            roleDataRuleMapper.delete(new QueryWrapper<SysRoleDataRule>().eq("role_id", id));
            dataRuleHandler.deleteDataRule(String.valueOf(id));
        });
        return result;
    }

    @Override
    public List<SysRoleDataRule> roleDataList(QueryWrapper<SysRoleDataRule> roleDataRuleEntityWrapper) {
        return roleDataRuleMapper.selectList(roleDataRuleEntityWrapper);
    }

    @Override
    public void removeRoleDataRule(QueryWrapper<SysRoleDataRule> roleDataRuleEntityWrapper) {
        roleDataRuleMapper.delete(roleDataRuleEntityWrapper);
    }

    @Override
    public void insertBatchRoleDataRule(List<SysRoleDataRule> roleDataRuleList) {
        roleDataRuleList.forEach(item -> {
            roleDataRuleMapper.insert(item);
        });
    }
}
