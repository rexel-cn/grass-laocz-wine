package com.rexel.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rexel.system.domain.GrassNoticeDingding;
import com.rexel.system.domain.dto.GrassNoticeDingdingDTO;

import java.util.List;

/**
 * 通知钉钉配置Service接口
 *
 * @author grass-service
 * @date 2022-07-29
 */
public interface IGrassNoticeDingdingService extends IService<GrassNoticeDingding> {

    /**
     * 查询通知钉钉配置列表
     *
     * @param grassNoticeDingding 通知钉钉配置
     * @return 通知钉钉配置集合
     */
    List<GrassNoticeDingding> selectGrassNoticeDingdingList(GrassNoticeDingding grassNoticeDingding);

    List<GrassNoticeDingding> selectOneByTenantId();

    Boolean updateOrInsert(GrassNoticeDingdingDTO grassNoticeDingdingDTO);

    List<GrassNoticeDingding> selectByTenantId(String tenantId);

    Boolean deleteByTenantId(String tenantId);
}
