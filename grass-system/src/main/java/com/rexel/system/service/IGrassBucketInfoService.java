package com.rexel.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rexel.system.domain.GrassBucketInfo;

import java.util.List;

/**
 * 存储信息Service接口
 *
 * @author grass-service
 * @date 2022-08-15
 */
public interface IGrassBucketInfoService extends IService<GrassBucketInfo> {

    Boolean insertGrassBucketInfo(GrassBucketInfo grassBucketInfo);

    /**
     * 查询存储信息列表
     *
     * @param grassBucketInfo 存储信息
     * @return 存储信息集合
     */
    List<GrassBucketInfo> selectGrassBucketInfoList(GrassBucketInfo grassBucketInfo);

    /**
     * 根据租户Id查询
     *
     * @param tenantIds 租户id
     * @return
     */
    List<GrassBucketInfo> selectGrassBucketInfoByTenantIds(List<String> tenantIds);

    /**
     * 根据租户Id查询
     *
     * @param tenantId 租户id
     * @return
     */
    GrassBucketInfo selectGrassBucketInfoByTenantId(String tenantId);

    Boolean deleteGrassBucketInfoByTenantId(String tenantId);

    /**
     * 根据bucketId批量删除
     *
     * @param bucketIds
     * @return
     */
    Boolean deleteByBucketIds(List<String> bucketIds);


    Boolean updateGrassBucketInfo(Long everySeconds);

}
