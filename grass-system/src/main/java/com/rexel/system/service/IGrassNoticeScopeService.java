package com.rexel.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rexel.system.domain.GrassNoticeScope;
import com.rexel.system.domain.vo.NoticeScopeDropDown;

import java.util.List;

/**
 * 通知模板对应范围Service接口
 *
 * @author grass-service
 * @date 2022-08-01
 */
public interface IGrassNoticeScopeService extends IService<GrassNoticeScope> {

    /**
     * 查询通知模板对应范围列表
     *
     * @param grassNoticeScope 通知模板对应范围
     * @return 通知模板对应范围集合
     */
    List<GrassNoticeScope> selectGrassNoticeScopeList(GrassNoticeScope grassNoticeScope);

    /**
     * 通知范围下拉框
     *
     * @return List 结果
     */
    List<NoticeScopeDropDown> noticeScopeList();

    Boolean deleteByTenantId(String tenantId);
}
