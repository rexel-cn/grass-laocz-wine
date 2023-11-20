package com.rexel.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rexel.system.domain.GrassPointTagInfo;
import com.rexel.system.domain.GrassPointTagPoint;
import com.rexel.system.domain.dto.PulsePointQueryDTO;
import com.rexel.system.domain.vo.GrassPointTagDeviceVO;
import com.rexel.system.domain.vo.GrassPointTagInfoVO;
import com.rexel.system.domain.vo.PointTagVO;
import com.rexel.system.domain.vo.reduce.ReduceInfo;

import java.util.List;

/**
 * 测点标签Service接口
 *
 * @author grass-service
 * @date 2022-08-25
 */
public interface IGrassPointTagToPointService extends IService<GrassPointTagPoint> {

    /**
     * 查询测点标签列表
     *
     * @param grassPointTagPoint 测点标签
     * @return 测点标签集合
     */
    List<GrassPointTagPoint> selectGrassPointTagList(GrassPointTagPoint grassPointTagPoint);



    /**
     * 获取关联数量中的测点具体信息
     *
     * @param pointTagInfoId 测点标签信息id
     * @return 关联测点信息集合
     */
    List<GrassPointTagDeviceVO> getByPointTagInfoId(Long pointTagInfoId);

    /**
     * 根据标签键查询测点标签信息数据列表，空值为查全部
     *
     * @param tagKey 标签键
     * @return 测点标签信息数据列表集合
     */
    List<GrassPointTagInfoVO> selectGrassPointTagRelationListByTagKey(String tagKey);

    /**
     * 通过point_tag_info_id 查询所有point_id
     *
     * @param id id
     * @return 结果
     */
    List<String> selectIdByPointTagInfoId(Long id);

    /**
     * tagKey和tagValue查询测点标签信息数据列表，空值为查全部
     *
     * @param grassPointTagInfo grassPointTagInfo
     * @return 结果
     */
    List<PointTagVO> selectPointTagVOList(GrassPointTagInfo grassPointTagInfo);

    /**
     * getPointTagInfoByPoint
     *
     * @param pointQueryDTO pointQueryDTO
     * @return 结果
     */
    List<GrassPointTagInfo> getPointTagInfoByPoint(PulsePointQueryDTO pointQueryDTO);

    /**
     * 删除租户相关数据
     *
     * @param tenantId tenantId
     * @return 结果
     */
    Boolean deleteByTenantId(String tenantId);

    /**
     * 查询测点标签及存储空间信息
     *
     * @return 结果
     */
    List<ReduceInfo> getPointTagBucket();
}
