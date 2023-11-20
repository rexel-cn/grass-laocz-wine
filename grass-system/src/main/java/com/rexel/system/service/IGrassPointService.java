package com.rexel.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rexel.common.core.domain.AjaxResult;
import com.rexel.system.domain.GrassPointInfo;
import com.rexel.system.domain.GrassPointTagInfo;
import com.rexel.system.domain.dto.GrassAssetPointDTO;
import com.rexel.system.domain.dto.PulsePointCurveDTO;
import com.rexel.system.domain.dto.PulsePointQueryDTO;
import com.rexel.system.domain.vo.PointQueryVO;
import com.rexel.system.domain.vo.PointTimeDataVO;
import com.rexel.system.domain.vo.common.NumberCountVO;

import java.util.List;

/**
 * ClassName PointService
 * Description GrassPointService
 * Author donghai
 **/
public interface IGrassPointService extends IService<GrassPointInfo> {

    List<PointTimeDataVO> selectByAsset(GrassAssetPointDTO grassAssetPointDTO);

    /**
     * 根据 资产id  查询测点
     *
     * @param id
     * @return
     */
    List<GrassPointInfo> selectByAssetId(Long id);

    /**
     * 分页查询测点列表
     *
     * @param pointQueryDTO
     * @return
     */
    List<PointQueryVO> getListPage(PulsePointQueryDTO pointQueryDTO);

    /**
     * 分页查询测点列表
     *
     * @param pointQueryDTO
     * @return
     */
    List<PointQueryVO> getListPageRealValue(PulsePointQueryDTO pointQueryDTO);

    /**
     * 修改测点信息
     *
     * @param grassPointInfo
     * @return
     */
    Boolean updateByGrassPointInfo(GrassPointInfo grassPointInfo);

    /**
     * 数据曲线
     *
     * @param pointCurveDTO
     * @return
     */
    AjaxResult curve(PulsePointCurveDTO pointCurveDTO);

    /**
     * 导入
     *
     * @param grassPointInfos
     * @return
     */
    AjaxResult importPoint(List<GrassPointInfo> grassPointInfos);

    /**
     * 导出
     *
     * @return
     */
    List<PointQueryVO> export(PulsePointQueryDTO pointQueryDTO);


    /**
     * 根据资产id，物联设备id查询测点，  资产id或物联设备id必须有一个
     *
     * @param pointQueryDTO
     * @return
     */
    List<PointQueryVO> dropDown(PulsePointQueryDTO pointQueryDTO);

    /**
     * 根据设备id查询数量
     *
     * @param
     * @return
     */
    Integer selectCount();


    List<GrassPointInfo> selectByDeviceIdAndPointId(List<GrassPointInfo> grassPointInfos);

    /**
     * 根据标签key、value  查询对应测点信息
     *
     * @param grassPointTagInfo
     */
    List<GrassPointInfo> selectByTag(GrassPointTagInfo grassPointTagInfo);

    Boolean deleteByTenantId(String tenantId);

    /**
     * 根据租户id 查询测点
     * @param tenantId
     * @return
     */
    NumberCountVO pointStatisticalByTenantId();
}
