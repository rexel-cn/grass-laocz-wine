package com.rexel.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rexel.system.domain.GrassNoticeScope;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 通知模板对应范围Mapper接口
 *
 * @author grass-service
 * @date 2022-08-01
 */
@Repository
public interface GrassNoticeScopeMapper extends BaseMapper<GrassNoticeScope> {
    /**
     * 查询通知模板对应范围列表
     *
     * @param grassNoticeScope 通知模板对应范围
     * @return 通知模板对应范围集合
     */
    List<GrassNoticeScope> selectGrassNoticeScopeList(GrassNoticeScope grassNoticeScope);

    Boolean deleteByTenantId(String tenantId);
}
