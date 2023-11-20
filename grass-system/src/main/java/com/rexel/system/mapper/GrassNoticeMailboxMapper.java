package com.rexel.system.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rexel.system.domain.GrassNoticeMailbox;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 通知邮箱配置Mapper接口
 *
 * @author grass-service
 * @date 2022-07-29
 */
@Repository
public interface GrassNoticeMailboxMapper extends BaseMapper<GrassNoticeMailbox> {
    /**
     * 查询通知邮箱配置列表
     *
     * @param grassNoticeMailbox 通知邮箱配置
     * @return 通知邮箱配置集合
     */
    List<GrassNoticeMailbox> selectGrassNoticeMailboxList(GrassNoticeMailbox grassNoticeMailbox);

    @InterceptorIgnore(tenantLine = "on")
    GrassNoticeMailbox selectOneByTenantId(String tenantId);

    @InterceptorIgnore(tenantLine = "on")
    Boolean deleteByTenantId(String tenantId);
}
