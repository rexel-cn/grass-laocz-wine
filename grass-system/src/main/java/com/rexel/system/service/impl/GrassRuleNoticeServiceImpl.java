package com.rexel.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.system.domain.GrassRuleNotice;
import com.rexel.system.domain.vo.GrassRuleNoticeVO;
import com.rexel.system.mapper.GrassRuleNoticeMapper;
import com.rexel.system.service.IGrassRuleNoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 报警规则关联通知模板Service业务层处理
 *
 * @author grass-service
 * @date 2022-08-05
 */
@Service
public class GrassRuleNoticeServiceImpl extends ServiceImpl<GrassRuleNoticeMapper, GrassRuleNotice> implements IGrassRuleNoticeService {


    @Autowired
    private GrassRuleNoticeMapper grassRuleNoticeMapper;

    /**
     * 查询报警规则关联通知模板列表
     *
     * @param grassRuleNotice 报警规则关联通知模板
     * @return 报警规则关联通知模板
     */
    @Override
    public List<GrassRuleNotice> selectGrassRuleNoticeList(GrassRuleNotice grassRuleNotice) {
        return baseMapper.selectGrassRuleNoticeList(grassRuleNotice);
    }

    @Override
    public List<GrassRuleNoticeVO> selectGrassRuleNoticeVOByIds(List<String> ids) {
        return baseMapper.selectGrassRuleNoticeVOByIds(ids);
    }

    @Override
    public Boolean deleteByTenantId(String tenantId) {
        return baseMapper.deleteByTenantId(tenantId);
    }


}
