package com.rexel.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.system.domain.GrassWebBoard;
import com.rexel.system.mapper.GrassWebBoardMapper;
import com.rexel.system.service.IGrassWebBoardService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 看板地址Service业务层处理
 *
 * @author grass-service
 * @date 2022-08-01
 */
@Service
public class GrassWebBoardServiceImpl extends ServiceImpl<GrassWebBoardMapper, GrassWebBoard> implements IGrassWebBoardService {

    
    /**
     * 查询看板地址列表
     *
     *
     * @param grassWebBoard 看板地址
     * @return 看板地址
     */
    @Override
    public List<GrassWebBoard> selectGrassWebBoardList(GrassWebBoard grassWebBoard) {
        return baseMapper.selectGrassWebBoardList(grassWebBoard);
    }

    @Override
    public Boolean deleteByTenantId(String tenantId) {
        return baseMapper.deleteByTenantId(tenantId);
    }

}
