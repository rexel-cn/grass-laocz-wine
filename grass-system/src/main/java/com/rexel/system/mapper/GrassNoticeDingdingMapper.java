package com.rexel.system.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rexel.system.domain.GrassNoticeDingding;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 通知钉钉配置Mapper接口
 *
 * @author grass-service
 * @date 2022-07-29
 */
@Repository
public interface GrassNoticeDingdingMapper extends BaseMapper<GrassNoticeDingding> {
    /**
     * 查询通知钉钉配置列表
     *
     * @param grassNoticeDingding 通知钉钉配置
     * @return 通知钉钉配置集合
     */
    List<GrassNoticeDingding> selectGrassNoticeDingdingList(GrassNoticeDingding grassNoticeDingding);

    @InterceptorIgnore(tenantLine = "on")
    List<GrassNoticeDingding> selectByTenantId(String tenantId);

    @InterceptorIgnore(tenantLine = "on")
    Boolean deleteByTenantId(String tenantId);
}
