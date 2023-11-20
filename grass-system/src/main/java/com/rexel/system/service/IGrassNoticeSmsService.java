package com.rexel.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rexel.system.domain.GrassNoticeSms;

import java.util.List;

/**
 * 通知短信配置Service接口
 *
 * @author grass-service
 * @date 2022-07-29
 */
public interface IGrassNoticeSmsService extends IService<GrassNoticeSms> {

    /**
     * 查询通知短信配置列表
     *
     * @param grassNoticeSms 通知短信配置
     * @return 通知短信配置集合
     */
    List<GrassNoticeSms> selectGrassNoticeSmsList(GrassNoticeSms grassNoticeSms);

    GrassNoticeSms selectOneByTenantId();

    GrassNoticeSms selectOneByTenantId(String TenantId);

    Boolean deleteByTenantId(String tenantId);
}
