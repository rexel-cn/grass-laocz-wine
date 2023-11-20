package com.rexel.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.system.domain.GrassUserSmsMailboxDingding;
import com.rexel.system.mapper.GrassUserSmsMailboxDingdingMapper;
import com.rexel.system.service.IGrassUserSmsMailboxDingdingService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 短信-钉钉-邮件发送记录Service业务层处理
 *
 * @author grass-service
 * @date 2022-08-09
 */
@Service
public class GrassUserSmsMailboxDingdingServiceImpl extends ServiceImpl<GrassUserSmsMailboxDingdingMapper, GrassUserSmsMailboxDingding> implements IGrassUserSmsMailboxDingdingService {


    /**
     * 查询短信-钉钉-邮件发送记录列表
     *
     * @param grassUserSmsMailboxDingding 短信-钉钉-邮件发送记录
     * @return 短信-钉钉-邮件发送记录
     */
    @Override
    public List<GrassUserSmsMailboxDingding> selectGrassUserSmsMailboxDingdingList(GrassUserSmsMailboxDingding grassUserSmsMailboxDingding) {
        return baseMapper.selectGrassUserSmsMailboxDingdingList(grassUserSmsMailboxDingding);
    }

    @Override
    public Boolean deleteByTenantId(String tenantId) {
        return baseMapper.deleteByTenantId(tenantId);
    }

}
