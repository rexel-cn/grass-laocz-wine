package com.rexel.system.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rexel.system.domain.GrassBucketInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 存储信息Mapper接口
 *
 * @author grass-service
 * @date 2022-08-15
 */
@Repository
public interface GrassBucketInfoMapper extends BaseMapper<GrassBucketInfo> {
    /**
     * 查询存储信息列表
     *
     * @param grassBucketInfo 存储信息
     * @return 存储信息集合
     */
    List<GrassBucketInfo> selectGrassBucketInfoList(GrassBucketInfo grassBucketInfo);

    /**
     * 根据租户id查询
     *
     * @param tenantIds 租户id
     * @return
     */
    @InterceptorIgnore(tenantLine = "on")
    List<GrassBucketInfo> selectGrassBucketInfoByTenantIds(@Param("tenantIds") List<String> tenantIds);

    /**
     * 根据租户id查询
     *
     * @param tenantId 租户id
     * @return
     */
    @InterceptorIgnore(tenantLine = "on")
    GrassBucketInfo selectGrassBucketInfoByTenantId(@Param("tenantId") String tenantId);

    /**
     * 根据bucketId批量删除
     *
     * @param bucketIds
     * @return
     */
    @InterceptorIgnore(tenantLine = "on")
    Boolean deleteByBucketIds(@Param("bucketIds") List<String> bucketIds);

    @InterceptorIgnore(tenantLine = "on")
    Boolean deleteGrassBucketInfoByTenantId(@Param("tenantId") String tenantId);
}
