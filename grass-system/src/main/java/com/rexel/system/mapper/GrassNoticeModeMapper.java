package com.rexel.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rexel.system.domain.GrassNoticeMode;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 通知模板对应方式Mapper接口
 *
 * @author grass-service
 * @date 2022-08-01
 */
@Repository
public interface GrassNoticeModeMapper extends BaseMapper<GrassNoticeMode> {
    /**
     * 查询通知模板对应方式列表
     *
     * @param grassNoticeMode 通知模板对应方式
     * @return 通知模板对应方式集合
     */
    List<GrassNoticeMode> selectGrassNoticeModeList(GrassNoticeMode grassNoticeMode);

    Boolean deleteByTenantId(String tenantId);
}
