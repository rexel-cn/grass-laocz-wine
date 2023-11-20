package com.rexel.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.common.utils.SecurityUtils;
import com.rexel.system.domain.GrassNoticeSms;
import com.rexel.system.mapper.GrassNoticeSmsMapper;
import com.rexel.system.service.IGrassNoticeSmsService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 通知短信配置Service业务层处理
 *
 * @author grass-service
 * @date 2022-07-29
 */
@Service
public class GrassNoticeSmsServiceImpl extends ServiceImpl<GrassNoticeSmsMapper, GrassNoticeSms> implements IGrassNoticeSmsService {


    /**
     * 查询通知短信配置列表
     *
     * @param grassNoticeSms 通知短信配置
     * @return 通知短信配置
     */
    @Override
    public List<GrassNoticeSms> selectGrassNoticeSmsList(GrassNoticeSms grassNoticeSms) {
        return baseMapper.selectGrassNoticeSmsList(grassNoticeSms);
    }

    @Override
    public GrassNoticeSms selectOneByTenantId() {
        GrassNoticeSms one = lambdaQuery()
                .eq(GrassNoticeSms::getTenantId, SecurityUtils.getTenantId())
                .one();
        if (one == null) {
            return new GrassNoticeSms();
        }

        return one;
    }

    @Override
    public GrassNoticeSms selectOneByTenantId(String TenantId) {
        return baseMapper.selectOneByTenantId(TenantId);
    }

    @Override
    public Boolean deleteByTenantId(String tenantId) {
        return baseMapper.deleteByTenantId(tenantId);
    }

}
