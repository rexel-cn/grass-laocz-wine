package com.rexel.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rexel.system.domain.GrassPointUserStar;
import com.rexel.system.domain.dto.GrassPointUserStarDTO;

import java.util.List;

/**
 * 【请填写功能名称】Service接口
 *
 * @author grass-service
 * @date 2022-10-21
 */
public interface IGrassPointUserStarService extends IService<GrassPointUserStar> {

    /**
     * 查询【请填写功能名称】列表
     *
     * @param grassPointUserStar 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    List<GrassPointUserStar> selectGrassPointUserStarList(GrassPointUserStar grassPointUserStar);


    Boolean top(GrassPointUserStarDTO grassPointUserStarDTO);

    Boolean deleteByTenantId(String tenantId);
}
