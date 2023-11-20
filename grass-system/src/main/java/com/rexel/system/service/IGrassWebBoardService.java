package com.rexel.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rexel.system.domain.GrassWebBoard;

import java.util.List;

/**
 * 看板地址Service接口
 *
 * @author grass-service
 * @date 2022-08-01
 */
public interface IGrassWebBoardService extends IService<GrassWebBoard> {

    /**
     * 查询看板地址列表
     *
     * @param grassWebBoard 看板地址
     * @return 看板地址集合
     */
     List<GrassWebBoard> selectGrassWebBoardList(GrassWebBoard grassWebBoard);

    Boolean deleteByTenantId(String tenantId);
}
