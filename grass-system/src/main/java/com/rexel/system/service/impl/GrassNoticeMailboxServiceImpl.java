package com.rexel.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.common.utils.SecurityUtils;
import com.rexel.system.domain.GrassNoticeMailbox;
import com.rexel.system.mapper.GrassNoticeMailboxMapper;
import com.rexel.system.service.IGrassNoticeMailboxService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 通知邮箱配置Service业务层处理
 *
 * @author grass-service
 * @date 2022-07-29
 */
@Service
public class GrassNoticeMailboxServiceImpl extends ServiceImpl<GrassNoticeMailboxMapper, GrassNoticeMailbox> implements IGrassNoticeMailboxService {


    /**
     * 查询通知邮箱配置列表
     *
     * @param grassNoticeMailbox 通知邮箱配置
     * @return 通知邮箱配置
     */
    @Override
    public List<GrassNoticeMailbox> selectGrassNoticeMailboxList(GrassNoticeMailbox grassNoticeMailbox) {
        return baseMapper.selectGrassNoticeMailboxList(grassNoticeMailbox);
    }

    @Override
    public GrassNoticeMailbox selectOneByTenantId() {
        GrassNoticeMailbox one = lambdaQuery()
                .eq(GrassNoticeMailbox::getTenantId, SecurityUtils.getTenantId())
                .one();
        if (one == null) {
            return new GrassNoticeMailbox();
        }
        return one;
    }

    @Override
    public GrassNoticeMailbox selectOneByTenantId(String TenantId) {
        return baseMapper.selectOneByTenantId(TenantId);
    }

    @Override
    public Boolean deleteByTenantId(String tenantId) {
        return baseMapper.deleteByTenantId(tenantId);
    }

}
