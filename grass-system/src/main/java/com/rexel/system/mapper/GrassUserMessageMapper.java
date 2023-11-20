package com.rexel.system.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rexel.system.domain.GrassUserMessage;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户站内信Mapper接口
 *
 * @author grass-service
 * @date 2022-08-09
 */
@Repository
public interface GrassUserMessageMapper extends BaseMapper<GrassUserMessage> {
    /**
     * 查询用户站内信列表
     *
     * @param grassUserMessage 用户站内信
     * @return 用户站内信集合
     */

    List<GrassUserMessage> selectGrassUserMessageList(GrassUserMessage grassUserMessage);

    @InterceptorIgnore(tenantLine = "on")
    Integer selectCountByGrassUserMessage(GrassUserMessage grassUserMessage);

    @InterceptorIgnore(tenantLine = "on")
    Boolean deleteByTenantId(String tenantId);
}
