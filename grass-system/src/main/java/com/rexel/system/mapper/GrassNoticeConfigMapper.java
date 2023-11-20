package com.rexel.system.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rexel.system.domain.GrassNoticeConfig;
import com.rexel.system.domain.vo.GrassNoticeConfigVO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 通知配置主Mapper接口
 *
 * @author grass-service
 * @date 2022-07-29
 */
@Repository
public interface GrassNoticeConfigMapper extends BaseMapper<GrassNoticeConfig> {
    /**
     * 查询通知配置主列表
     *
     * @param grassNoticeConfig 通知配置主
     * @return 通知配置主集合
     */
    List<GrassNoticeConfig> selectGrassNoticeConfigList(GrassNoticeConfig grassNoticeConfig);


    List<GrassNoticeConfigVO> selectList();

    @InterceptorIgnore(tenantLine = "on")
    GrassNoticeConfig selectOneByPushType(GrassNoticeConfig grassNoticeConfig);

    @InterceptorIgnore(tenantLine = "on")
    Boolean deleteByTenantId(String tenantId);
}
