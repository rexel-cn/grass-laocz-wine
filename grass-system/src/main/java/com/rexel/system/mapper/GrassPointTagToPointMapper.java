package com.rexel.system.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rexel.system.domain.GrassPointTagInfo;
import com.rexel.system.domain.GrassPointTagPoint;
import com.rexel.system.domain.dto.PulsePointQueryDTO;
import com.rexel.system.domain.vo.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 测点标签Mapper接口
 *
 * @author grass-service
 * @date 2022-08-25
 */
@Repository
public interface GrassPointTagToPointMapper extends BaseMapper<GrassPointTagPoint> {
    /**
     * 查询测点标签列表
     *
     * @param grassPointTagPoint 测点标签
     * @return 测点标签集合
     */
    List<GrassPointTagPoint> selectGrassPointTagList(GrassPointTagPoint grassPointTagPoint);


    /**
     * 根据测点标签信息id，拿到所有的deviceId和pointId
     * @param pointTagInfoId 测点标签信息id
     * @return deviceId和pointId的对象集合
     */
    List<GrassPDVO> selectByPointTagInfoId(Long pointTagInfoId);

    /**
     * 根据标签键查询测点标签信息数据列表，空值为查全部
     * @param tagKey 标签键
     * @return 测点标签信息数据列表集合
     */
    List<GrassPointTagInfoVO> selectGrassPointTagRelationListByTagKey(String tagKey);

    /**
     * 根据deviceId和pointId,拿到关联测点的具体信息
     *
     * @param pointTagInfoId
     * @return 结果
     */
    List<GrassPointTagDeviceVO> selectPointDevice(Long pointTagInfoId);

    /**
     * selectIdsByPointTagInfoId
     * @param id id
     * @return 结果
     */
    List<String> selectIdsByPointTagInfoId(Long id);

    /**
     * tagKey和tagValue查询测点标签信息数据列表，空值为查全部
     *
     * @param grassPointTagInfo grassPointTagInfo
     * @return 结果
     */
    List<PointTagVO> selectPointTagVOList(GrassPointTagInfo grassPointTagInfo);

    /**
     * getPointTagInfoByPoint
     * @param pointQueryDTO pointQueryDTO
     * @return 结果
     */
    List<GrassPointTagInfo> getPointTagInfoByPoint(PulsePointQueryDTO pointQueryDTO);

    /**
     * 查询数据聚合用测点标签信息
     * @param tagKey 标签键
     * @return 结果
     */
    @InterceptorIgnore(tenantLine = "on")
    List<GrassPointTagBucketVO> getPointTagAndBucket(@Param("tagKey") String tagKey);

    /**
     * deleteByTenantId
     * @param tenantId tenantId
     * @return 结果
     */
    @InterceptorIgnore(tenantLine = "on")
    Boolean deleteByTenantId(String tenantId);
}
