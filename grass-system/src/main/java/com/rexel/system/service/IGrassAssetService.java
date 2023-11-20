package com.rexel.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rexel.common.core.domain.DynamicHeadExcel;
import com.rexel.system.domain.GrassAsset;
import com.rexel.system.domain.GrassAssetName;
import com.rexel.system.domain.dto.GrassAssetDTO;
import com.rexel.system.domain.dto.GrassAssetPointDTO;
import com.rexel.system.domain.dto.GrassAssetQueryDTO;
import com.rexel.system.domain.dto.GrassPointHisDTO;
import com.rexel.system.domain.vo.*;

import java.util.List;

/**
 * 资产Service接口
 *
 * @author grass-service
 * @date 2022-07-19
 */
public interface IGrassAssetService extends IService<GrassAsset> {
    /**
     * 根据id查询资产
     *
     * @param id
     * @return
     */
    GrassAsset getAssetById(Long id);

    Long selectGrassAssetListCount(GrassAssetQueryDTO grassAsset);

    /**
     * 查询资产列表
     *
     * @param grassAsset 资产
     * @return 资产集合
     */
    List<GrassAsset> selectGrassAssetList(GrassAssetQueryDTO grassAsset);

    List<GrassAssetExportVO> exportAssetList(GrassAssetQueryDTO grassAsset);

    /**
     * 添加
     *
     * @param grassAssetDTO
     * @return
     */
    Boolean saveAsset(GrassAssetDTO grassAssetDTO);

    /**
     * 修改
     *
     * @param grassAssetDTO
     * @return
     */
    Boolean updateAsset(GrassAssetDTO grassAssetDTO);


    /**
     * 根据id获取修改详情  回显
     *
     * @param id
     * @return
     */
    GrassAssetInfoVO getAssetInfoById(Long id);


    /**
     * 资产-关联测点实时数据
     *
     * @param grassAssetPointDTO
     * @return
     */
    List<PointTimeDataVO> pointTimeData(GrassAssetPointDTO grassAssetPointDTO);


    /**
     * 导入资产
     *
     * @param grassImportList
     * @return
     */
    Boolean importAssetList(List<GrassAssetExportVO> grassImportList);

    /**
     * 根据id 删除资产
     *
     * @param id
     * @return
     */
    boolean removeAsset(Long id);


    /**
     * 测点历史数据
     *
     * @return
     */
    PointHisVO pointHis(GrassPointHisDTO grassPointHisDTO);

    DynamicHeadExcel buildExcel(PointHisVO pointHisVO);

    /**
     * 根据assetTypeName和assetName查询assetId
     *
     * @param rulesVOs
     * @return
     */
    List<GrassAssetName> selectIdByName(List<RulesVO> rulesVOs);

    List<GrassAsset> getAssetByPointPrKey(Long id);

    Boolean deleteByTenantId(String tenantId);

    List<String> getAssetIdsByAssetTypeId(String assetTypeId);

    List<GrassAssetIdAndNameVO> getAssetIdAndNameByTenantId();
}
