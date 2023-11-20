package com.rexel.common.core.service;

import com.rexel.common.core.domain.SysHeaderMetadata;
import com.rexel.common.core.page.PageHeader;

import java.util.List;

/**
 * 列头元数据Service接口
 *
 * @author grass-service
 * @date 2022-07-07
 */
public interface ISysHeaderMetadataService {

    /**
     * 查询列头元数据列表
     *
     * @param sysHeaderMetadata 列头元数据
     * @return 列头元数据集合
     */
    List<PageHeader> selectSysHeaderMetadataList(SysHeaderMetadata sysHeaderMetadata);

}
