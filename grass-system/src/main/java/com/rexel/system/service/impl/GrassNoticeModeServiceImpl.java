package com.rexel.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.system.domain.GrassNoticeMode;
import com.rexel.system.mapper.GrassNoticeModeMapper;
import com.rexel.system.service.IGrassNoticeModeService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 通知模板对应方式Service业务层处理
 *
 * @author grass-service
 * @date 2022-08-01
 */
@Service
public class GrassNoticeModeServiceImpl extends ServiceImpl<GrassNoticeModeMapper, GrassNoticeMode> implements IGrassNoticeModeService {


    /**
     * 查询通知模板对应方式列表
     *
     * @param grassNoticeMode 通知模板对应方式
     * @return 通知模板对应方式
     */
    @Override
    public List<GrassNoticeMode> selectGrassNoticeModeList(GrassNoticeMode grassNoticeMode) {
        return baseMapper.selectGrassNoticeModeList(grassNoticeMode);
    }

    @Override
    public Boolean deleteByTenantId(String tenantId) {
        return baseMapper.deleteByTenantId(tenantId);
    }

}
