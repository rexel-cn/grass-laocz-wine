package com.rexel.system.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rexel.system.domain.GrassAssetType;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 资产类型Mapper接口
 *
 * @author grass-service
 * @date 2022-07-21
 */
@Repository
public interface GrassAssetTypeMapper extends BaseMapper<GrassAssetType> {
    /**
     * 查询资产类型树
     *
     * @return 资产类型树
     */
    List<GrassAssetType> selectAssetTypeTree();

    /**
     * 级联删除
     *
     * @param id
     * @return
     */
    @InterceptorIgnore(tenantLine = "on")
    int deleteAssetTypeById(Long id);

    /**
     * 根据ID查询所有子
     *
     * @param
     * @return
     */
    public List<GrassAssetType> selectChildrenById(String id);

    /**
     * 修改子元素关系
     *
     * @param
     * @return 结果
     */
    public int updateChildren(@Param("list") List<GrassAssetType> list);

    @InterceptorIgnore(tenantLine = "on")
    Boolean deleteByTenantId(String tenantId);
}
