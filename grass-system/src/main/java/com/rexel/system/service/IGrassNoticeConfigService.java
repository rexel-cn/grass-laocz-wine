package com.rexel.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rexel.system.domain.GrassNoticeConfig;
import com.rexel.system.domain.dto.GrassNoticeConfigDTO;
import com.rexel.system.domain.vo.GrassNoticeConfigVO;

import java.util.List;

/**
 * 通知配置主Service接口
 *
 * @author grass-service
 * @date 2022-07-29
 */
public interface IGrassNoticeConfigService extends IService<GrassNoticeConfig> {

    /**
     * 查询通知配置主列表
     *
     * @param grassNoticeConfig 通知配置主
     * @return 通知配置主集合
     */
    List<GrassNoticeConfigVO> selectGrassNoticeConfigList(GrassNoticeConfig grassNoticeConfig);


    boolean updateOpenStatusById(GrassNoticeConfigDTO grassNoticeConfigDTO);

    GrassNoticeConfig selectOneByPushType(GrassNoticeConfig grassNoticeConfig);

    Boolean deleteByTenantId(String tenantId);
}
