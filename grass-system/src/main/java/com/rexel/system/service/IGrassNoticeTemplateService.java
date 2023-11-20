package com.rexel.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rexel.system.domain.GrassNoticeTemplate;
import com.rexel.system.domain.dto.GrassNoticeTemplateDTO;
import com.rexel.system.domain.vo.GrassNoticeTemplateVO;

import java.util.List;

/**
 * 通知配置模板Service接口
 *
 * @author grass-service
 * @date 2022-08-01
 */
public interface IGrassNoticeTemplateService extends IService<GrassNoticeTemplate> {

    /**
     * 查询通知配置模板列表
     *
     * @param grassNoticeTemplate 通知配置模板
     * @return 通知配置模板集合
     */
    List<GrassNoticeTemplateVO> selectGrassNoticeTemplateList(GrassNoticeTemplate grassNoticeTemplate);


    /**
     * 查询通知配置模板列表
     *
     * @param grassNoticeTemplate 通知配置模板
     * @return 通知配置模板集合
     */
    GrassNoticeTemplateVO selectGrassNoticeTemplateById(GrassNoticeTemplate grassNoticeTemplate);


    /**
     * 创建通知模板
     *
     * @param grassNoticeScopeDTOS 添加对象
     * @return
     */
    Boolean createGrassNoticeTemplate(GrassNoticeTemplateDTO grassNoticeScopeDTOS);

    /**
     * 修改通知模板
     *
     * @param grassNoticeScopeDTOS 修改对象
     * @return
     */
    Boolean updateGrassNoticeTemplate(GrassNoticeTemplateDTO grassNoticeScopeDTOS);

    /**
     * 删除通知模板
     *
     * @param grassNoticeScopeDTOS 删除对象
     * @return
     */
    Boolean deleteGrassNoticeTemplate(GrassNoticeTemplateDTO grassNoticeScopeDTOS);

    Boolean deleteByTenantId(String tenantId);
}
