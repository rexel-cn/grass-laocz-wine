package com.rexel.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.system.domain.GrassWebConf;
import com.rexel.system.mapper.GrassWebConfMapper;
import com.rexel.system.service.IGrassWebConfService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 工艺组态地址信息Service业务层处理
 *
 * @author grass-service
 * @date 2022-07-18
 */
@Service
public class GrassWebConfServiceImpl extends ServiceImpl<GrassWebConfMapper, GrassWebConf> implements IGrassWebConfService {


    /**
     * 查询工艺组态地址信息列表
     *
     * @param grassWebConf 工艺组态地址信息
     * @return 工艺组态地址信息
     */
    @Override
    public List<GrassWebConf> selectGrassWebConfList(GrassWebConf grassWebConf) {
        return baseMapper.selectGrassWebConfList(grassWebConf);
    }

    @Override
    public Boolean deleteByTenantId(String tenantId) {
        return baseMapper.deleteByTenantId(tenantId);
    }

}
