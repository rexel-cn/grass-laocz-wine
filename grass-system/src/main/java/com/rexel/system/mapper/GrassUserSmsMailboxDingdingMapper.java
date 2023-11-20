package com.rexel.system.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rexel.system.domain.GrassUserSmsMailboxDingding;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 短信-钉钉-邮件发送记录Mapper接口
 *
 * @author grass-service
 * @date 2022-08-09
 */
@Repository
public interface GrassUserSmsMailboxDingdingMapper extends BaseMapper<GrassUserSmsMailboxDingding> {
    /**
     * 查询短信-钉钉-邮件发送记录列表
     *
     * @param grassUserSmsMailboxDingding 短信-钉钉-邮件发送记录
     * @return 短信-钉钉-邮件发送记录集合
     */
    List<GrassUserSmsMailboxDingding> selectGrassUserSmsMailboxDingdingList(GrassUserSmsMailboxDingding grassUserSmsMailboxDingding);

    @InterceptorIgnore(tenantLine = "on")
    Boolean deleteByTenantId(String tenantId);
}
