package com.rexel.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rexel.system.domain.GrassNoticeMode;

import java.util.List;

/**
 * 通知模板对应方式Service接口
 *
 * @author grass-service
 * @date 2022-08-01
 */
public interface IGrassNoticeModeService extends IService<GrassNoticeMode> {

    /**
     * 查询通知模板对应方式列表
     *
     * @param grassNoticeMode 通知模板对应方式
     * @return 通知模板对应方式集合
     */
    List<GrassNoticeMode> selectGrassNoticeModeList(GrassNoticeMode grassNoticeMode);

    Boolean deleteByTenantId(String tenantId);
}
