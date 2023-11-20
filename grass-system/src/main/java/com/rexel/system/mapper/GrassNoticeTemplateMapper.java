package com.rexel.system.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rexel.system.domain.GrassNoticeTemplate;
import com.rexel.system.domain.vo.GrassNoticeTemplateVO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 通知配置模板Mapper接口
 *
 * @author grass-service
 * @date 2022-08-01
 */
@Repository
public interface GrassNoticeTemplateMapper extends BaseMapper<GrassNoticeTemplate> {
    /**
     * 查询通知配置模板列表
     *
     * @param grassNoticeTemplate 通知配置模板
     * @return 通知配置模板集合
     */
    List<GrassNoticeTemplateVO> selectGrassNoticeTemplateList(GrassNoticeTemplate grassNoticeTemplate);


    @InterceptorIgnore(tenantLine = "on")
    Boolean deleteByTenantId(String tenantId);
}
