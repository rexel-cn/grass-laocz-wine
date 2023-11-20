package com.rexel.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rexel.system.domain.GrassAssetType;
import com.rexel.system.domain.vo.GrassAssetTypeTreeVO;

import java.util.List;

/**
 * 资产类型Service接口
 *
 * @author grass-service
 * @date 2022-07-21
 */
public interface IGrassAssetTypeService extends IService<GrassAssetType> {
    /**
     * 查询资产类型
     *
     * @param
     * @return List<GrassAssetType>  资产类型集合
     */
    List<GrassAssetType> selectAssetTypeTree();

    /**
     * 构建前端所需要树结构
     *
     * @param assetTreeList
     * @return
     */
    List<GrassAssetTypeTreeVO> buildAssetTree(List<GrassAssetType> assetTreeList);

    /**
     * 添加资产类型
     *
     * @param grassAssetType
     * @return
     */
    Boolean saveAssetType(GrassAssetType grassAssetType);

    /**
     * 修改资产类型
     * @param grassAssetType
     * @return
     */
    Boolean updateAssetTypeById(GrassAssetType grassAssetType);

    /**
     * 根据id 进行删除
     *
     * @param asList
     * @return
     */
    Boolean removeAssetTypeById(String id);

    Boolean deleteByTenantId(String tenantId);
}
