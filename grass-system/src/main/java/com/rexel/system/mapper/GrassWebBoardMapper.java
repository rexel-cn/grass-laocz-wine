package com.rexel.system.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rexel.system.domain.GrassWebBoard;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 看板地址Mapper接口
 *
 * @author grass-service
 * @date 2022-08-01
 */
@Repository
public interface GrassWebBoardMapper extends BaseMapper<GrassWebBoard> {
    /**
     * 查询看板地址列表
     *
     * @param grassWebBoard 看板地址
     * @return 看板地址集合
     */
    List<GrassWebBoard> selectGrassWebBoardList(GrassWebBoard grassWebBoard);

    @InterceptorIgnore(tenantLine = "on")
    Boolean deleteByTenantId(String tenantId);
}
