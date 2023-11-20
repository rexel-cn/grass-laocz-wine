package com.rexel.system.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rexel.system.domain.GrassAsset;
import com.rexel.system.domain.GrassAssetName;
import com.rexel.system.domain.dto.GrassAssetQueryDTO;
import com.rexel.system.domain.vo.GrassAssetExportVO;
import com.rexel.system.domain.vo.RulesVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 资产Mapper接口
 *
 * @author grass-service
 * @date 2022-07-19
 */
@Repository
public interface GrassAssetMapper extends BaseMapper<GrassAsset> {
    @InterceptorIgnore(tenantLine = "on")
    Long getListPageCount(GrassAssetQueryDTO grassAsset);

    /**
     * 查询资产列表
     *
     * @param grassAsset 资产
     * @return 资产集合
     */
    @InterceptorIgnore(tenantLine = "on")
    List<GrassAsset> getListPage(GrassAssetQueryDTO grassAsset);

    /**
     * 根据资产类型Id 查询资产Id
     *
     * @param assetTypeId
     * @return
     */
    @InterceptorIgnore(tenantLine = "on")
    List<String> getAssetIdsByAssetTypeId(String assetTypeId);


    /**
     * 删除租户下的所有数据
     */
    @InterceptorIgnore(tenantLine = "on")
    Boolean deleteToTenant(String tenantId);


    GrassAsset getAssetById(Long id);

    List<GrassAssetExportVO> exportAssetList(GrassAssetQueryDTO grassAsset);

    /**
     * 根据assetTypeName和assetName查询assetId
     *
     * @param rulesVOs
     * @return
     */
    List<GrassAssetName> selectIdByName(@Param("rulesVOs") List<RulesVO> rulesVOs);

    List<GrassAsset> getAssetByPointPrKey(Long id);

}
