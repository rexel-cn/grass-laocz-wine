package com.rexel.common.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rexel.common.core.domain.SysHeaderMetadata;
import com.rexel.common.core.mapper.SysHeaderMetadataMapper;
import com.rexel.common.core.page.PageHeader;
import com.rexel.common.core.service.ISysHeaderMetadataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * 列头元数据Service业务层处理
 *
 * @author grass-service
 * @date 2022-07-07
 */
@Service
public class SysHeaderMetadataServiceImpl implements ISysHeaderMetadataService {

    @Autowired
    private SysHeaderMetadataMapper metadataMapper;

    /**
     * 查询列头元数据列表
     *
     * @param sysHeaderMetadata 列头元数据
     * @return 列头元数据
     */
    @Override
    public List<PageHeader> selectSysHeaderMetadataList(SysHeaderMetadata sysHeaderMetadata) {
        return metadataMapper.selectSysHeaderMetadataList(sysHeaderMetadata);
    }
}
