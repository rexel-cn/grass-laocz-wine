package com.rexel.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rexel.system.domain.GrassNoticeMailbox;

import java.util.List;

/**
 * 通知邮箱配置Service接口
 *
 * @author grass-service
 * @date 2022-07-29
 */
public interface IGrassNoticeMailboxService extends IService<GrassNoticeMailbox> {

    /**
     * 查询通知邮箱配置列表
     *
     * @param grassNoticeMailbox 通知邮箱配置
     * @return 通知邮箱配置集合
     */
    List<GrassNoticeMailbox> selectGrassNoticeMailboxList(GrassNoticeMailbox grassNoticeMailbox);

    GrassNoticeMailbox selectOneByTenantId();

    GrassNoticeMailbox selectOneByTenantId(String TenantId);

    Boolean deleteByTenantId(String tenantId);
}
