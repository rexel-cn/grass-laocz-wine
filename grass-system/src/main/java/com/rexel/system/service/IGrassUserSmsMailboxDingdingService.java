package com.rexel.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rexel.system.domain.GrassUserSmsMailboxDingding;

import java.util.List;

/**
 * 短信-钉钉-邮件发送记录Service接口
 *
 * @author grass-service
 * @date 2022-08-09
 */
public interface IGrassUserSmsMailboxDingdingService extends IService<GrassUserSmsMailboxDingding> {

    /**
     * 查询短信-钉钉-邮件发送记录列表
     *
     * @param grassUserSmsMailboxDingding 短信-钉钉-邮件发送记录
     * @return 短信-钉钉-邮件发送记录集合
     */
    List<GrassUserSmsMailboxDingding> selectGrassUserSmsMailboxDingdingList(GrassUserSmsMailboxDingding grassUserSmsMailboxDingding);

    Boolean deleteByTenantId(String tenantId);
}
