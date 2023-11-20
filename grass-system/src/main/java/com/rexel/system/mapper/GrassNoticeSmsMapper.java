package com.rexel.system.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rexel.system.domain.GrassNoticeSms;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 通知短信配置Mapper接口
 *
 * @author grass-service
 * @date 2022-07-29
 */
@Repository
public interface GrassNoticeSmsMapper extends BaseMapper<GrassNoticeSms> {
    /**
     * 查询通知短信配置列表
     *
     * @param grassNoticeSms 通知短信配置
     * @return 通知短信配置集合
     */
    List<GrassNoticeSms> selectGrassNoticeSmsList(GrassNoticeSms grassNoticeSms);

    @InterceptorIgnore(tenantLine = "on")
    GrassNoticeSms selectOneByTenantId(String tenantId);

    @InterceptorIgnore(tenantLine = "on")
    Boolean deleteByTenantId(String tenantId);
}
