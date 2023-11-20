package com.rexel.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rexel.system.domain.GrassWebConf;

import java.util.List;

/**
 * 工艺组态地址信息Service接口
 *
 * @author grass-service
 * @date 2022-07-18
 */
public interface IGrassWebConfService extends IService<GrassWebConf> {

    /**
     * 查询工艺组态地址信息列表
     *
     * @param grassWebConf 工艺组态地址信息
     * @return 工艺组态地址信息集合
     */
    public List<GrassWebConf> selectGrassWebConfList(GrassWebConf grassWebConf);

    Boolean deleteByTenantId(String tenantId);
}
