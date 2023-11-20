package com.rexel.system.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rexel.system.domain.GrassWebConf;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 工艺组态地址信息Mapper接口
 *
 * @author grass-service
 * @date 2022-07-18
 */
@Repository
public interface GrassWebConfMapper extends BaseMapper<GrassWebConf> {
    /**
     * 查询工艺组态地址信息列表
     *
     * @param grassWebConf 工艺组态地址信息
     * @return 工艺组态地址信息集合
     */
    public List<GrassWebConf> selectGrassWebConfList(GrassWebConf grassWebConf);


    @InterceptorIgnore(tenantLine = "on")
    Boolean deleteByTenantId(String tenantId);
}
