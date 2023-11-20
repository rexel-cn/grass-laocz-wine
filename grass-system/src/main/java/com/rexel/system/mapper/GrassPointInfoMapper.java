package com.rexel.system.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rexel.system.domain.GrassPointInfo;
import com.rexel.system.domain.GrassPointTagInfo;
import com.rexel.system.domain.dto.GrassAssetPointDTO;
import com.rexel.system.domain.dto.PulsePointQueryDTO;
import com.rexel.system.domain.vo.PointQueryVO;
import com.rexel.system.domain.vo.PointTimeDataVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 测点信息(同步pulse)Mapper接口
 *
 * @author grass-service
 * @date 2022-07-07
 */
@Repository
public interface GrassPointInfoMapper extends BaseMapper<GrassPointInfo> {

    /**
     * 测点名模糊查询
     *
     * @param pointQueryDTO
     * @return
     */
    List<PointQueryVO> getList(PulsePointQueryDTO pointQueryDTO);

    /**
     * 查询测点信息(同步pulse)列表
     *
     * @param grassPointInfo 测点信息(同步pulse)
     * @return 测点信息(同步pulse)集合
     */
    List<GrassPointInfo> selectGrassPointInfoList(GrassPointInfo grassPointInfo);

    List<GrassPointInfo> selectGrassPointInfoListByPulse(@Param("list") List<GrassPointInfo> pointQueryBodyDTOS);


    /**
     * 根据资产id 查询测点
     *
     * @param id
     * @return
     */
    List<GrassPointInfo> selectByAssetId(Long id);

    boolean insertGrassPointInfoBatch(@Param("list") List<GrassPointInfo> insertList);

    /**
     * 批量修改测点
     *
     * @param updateList
     * @return
     */
    boolean updateGrassPointInfoBatchById(@Param("list") List<GrassPointInfo> updateList);

    /**
     * 根据id修改测点
     *
     * @param grassPointInfo
     * @return
     */
    boolean updateGrassPointInfoById(GrassPointInfo grassPointInfo);

    /**
     * 测点下拉
     *
     * @param pointQueryDTO
     * @return
     */
    List<GrassPointInfo> dropDown(PulsePointQueryDTO pointQueryDTO);

    /**
     * @param list
     * @return
     */
    List<GrassPointInfo> selectByList(List<GrassPointInfo> list);

    /**
     * 查询数量
     *
     * @param
     * @return
     */
    Integer selectCount();

    /**
     * fhw
     * 根据测点id和设备id，拿到所有测点
     *
     * @param pointTagInfoId 测点标签信息id
     * @return
     */
    List<GrassPointInfo> selectBatchPointIdsByPointTagInfoId(Long pointTagInfoId);

    List<PointTimeDataVO> selectByAsset(GrassAssetPointDTO grassAssetPointDTO);

    List<GrassPointInfo> selectByDeviceIdAndPointId(@Param("list") List<GrassPointInfo> grassPointInfos);

    /**
     * 根据标签查询 测点信息
     *
     * @param grassPointTagInfo
     * @return List<GrassPointInfo>
     */
    @InterceptorIgnore(tenantLine = "on")
    List<GrassPointInfo> selectByTag(GrassPointTagInfo grassPointTagInfo);

    @InterceptorIgnore(tenantLine = "on")
    Boolean deleteByTenantId(String tenantId);
}
