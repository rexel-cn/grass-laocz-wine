package com.rexel.system.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rexel.system.domain.GrassPointUserStar;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 *
 * @author grass-service
 * @date 2022-10-21
 */
@Repository
public interface GrassPointUserStarMapper extends BaseMapper<GrassPointUserStar> {
    /**
     * 查询【请填写功能名称】列表
     *
     * @param grassPointUserStar 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    List<GrassPointUserStar> selectGrassPointUserStarList(GrassPointUserStar grassPointUserStar);

    @InterceptorIgnore(tenantLine = "on")
    Boolean deleteByTenantId(String tenantId);
}
