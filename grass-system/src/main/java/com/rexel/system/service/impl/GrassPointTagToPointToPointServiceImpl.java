package com.rexel.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.system.domain.GrassPointTagInfo;
import com.rexel.system.domain.GrassPointTagPoint;
import com.rexel.system.domain.dto.PulsePointQueryDTO;
import com.rexel.system.domain.vo.GrassPointTagBucketVO;
import com.rexel.system.domain.vo.GrassPointTagDeviceVO;
import com.rexel.system.domain.vo.GrassPointTagInfoVO;
import com.rexel.system.domain.vo.PointTagVO;
import com.rexel.system.domain.vo.reduce.ReduceInfo;
import com.rexel.system.domain.vo.reduce.ReduceTargetInfo;
import com.rexel.system.mapper.GrassPointTagToPointMapper;
import com.rexel.system.service.IGrassPointTagToPointService;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 测点标签Service业务层处理
 *
 * @author grass-service
 * @date 2022-08-25
 */
@Service
public class GrassPointTagToPointToPointServiceImpl extends ServiceImpl<GrassPointTagToPointMapper, GrassPointTagPoint> implements IGrassPointTagToPointService {
    /**
     * 查询测点标签列表
     *
     * @param grassPointTagPoint 测点标签
     * @return 测点标签
     */
    @Override
    public List<GrassPointTagPoint> selectGrassPointTagList(GrassPointTagPoint grassPointTagPoint) {
        return baseMapper.selectGrassPointTagList(grassPointTagPoint);
    }


    /**
     * 获取关联数量中的测点具体信息
     *
     * @param pointTagInfoId 测点标签信息id
     * @return 关联测点信息集合
     */
    @Override
    public List<GrassPointTagDeviceVO> getByPointTagInfoId(Long pointTagInfoId) {
        return baseMapper.selectPointDevice(pointTagInfoId);
    }

    /**
     * 根据标签键查询测点标签信息数据列表，空值为查全部
     *
     * @param tagKey 标签键
     * @return 测点标签信息数据列表集合
     */
    @Override
    public List<GrassPointTagInfoVO> selectGrassPointTagRelationListByTagKey(String tagKey) {
        return baseMapper.selectGrassPointTagRelationListByTagKey(tagKey);
    }

    @Override
    public List<String> selectIdByPointTagInfoId(Long id) {
        return baseMapper.selectIdsByPointTagInfoId(id);
    }

    @Override
    public List<PointTagVO> selectPointTagVOList(GrassPointTagInfo grassPointTagInfo) {
        return baseMapper.selectPointTagVOList(grassPointTagInfo);
    }

    @Override
    public List<GrassPointTagInfo> getPointTagInfoByPoint(PulsePointQueryDTO pointQueryDTO) {
        return baseMapper.getPointTagInfoByPoint(pointQueryDTO);
    }

    @Override
    public Boolean deleteByTenantId(String tenantId) {
        return baseMapper.deleteByTenantId(tenantId);
    }

    /**
     * 查询测点标签及存储空间信息
     *
     * @return 结果
     */
    @Override
    public List<ReduceInfo> getPointTagBucket() {
        List<GrassPointTagBucketVO> list = baseMapper.getPointTagAndBucket("reduce");
        if (list == null || list.size() <= 0) {
            return new ArrayList<>();
        }

        Map<String, Integer> intervalRetentionMap = new HashMap<>();
        Map<String, List<GrassPointTagBucketVO>> intervalTargetMap = new HashMap<>();
        for (GrassPointTagBucketVO vo : list) {
            String tagValue = vo.getTagValue();
            String[] splits = tagValue.split(":");
            if (splits.length != 2) {
                continue;
            }
            String interval = splits[0];
            Integer retention = Integer.valueOf(splits[1]);

            if (intervalRetentionMap.containsKey(interval)) {
                Integer old = intervalRetentionMap.get(interval);
                if (retention > old) {
                    intervalRetentionMap.put(interval, retention);
                }
            } else {
                intervalRetentionMap.put(interval, retention);
            }

            if (!intervalTargetMap.containsKey(interval)) {
                intervalTargetMap.put(interval, new ArrayList<>());
            }
            intervalTargetMap.get(interval).add(vo);
        }

        List<ReduceInfo> result = new ArrayList<>();
        Set<Map.Entry<String, Integer>> set = intervalRetentionMap.entrySet();
        for (Map.Entry<String, Integer> entry : set) {
            String interval = entry.getKey();
            Integer retention = entry.getValue();
            if (!intervalTargetMap.containsKey(interval)) {
                continue;
            }

            List<ReduceTargetInfo> targetInfoList = toTargetInfo(intervalTargetMap.get(interval));
            if (targetInfoList.size() <= 0) {
                continue;
            }

            ReduceInfo info = new ReduceInfo();
            info.setInterval(interval);
            info.setRetentionDays(retention);
            info.setTargetList(targetInfoList);
            result.add(info);
        }
        return result;
    }

    /**
     * 生成聚合对象集合
     *
     * @param list list
     * @return 结果
     */
    private List<ReduceTargetInfo> toTargetInfo(List<GrassPointTagBucketVO> list) {
        Map<String, String> deviceBucketMap = new HashMap<>();
        Map<String, List<String>> devicePointMap = new HashMap<>();
        for (GrassPointTagBucketVO vo : list) {
            String deviceId = vo.getDeviceId();
            String bucketId = vo.getBucketId();
            String pointId = vo.getPointId();
            if (!deviceBucketMap.containsKey(deviceId)) {
                deviceBucketMap.put(deviceId, bucketId);
            }
            if (!devicePointMap.containsKey(deviceId)) {
                devicePointMap.put(deviceId, new ArrayList<>());
            }
            List<String> pointIdList = devicePointMap.get(deviceId);
            if (!pointIdList.contains(pointId)) {
                pointIdList.add(pointId);
            }
        }

        List<ReduceTargetInfo> result = new ArrayList<>();
        Set<Map.Entry<String, String>> set = deviceBucketMap.entrySet();
        for (Map.Entry<String, String> entry : set) {
            String deviceId = entry.getKey();
            String bucketId = entry.getValue();
            if (!devicePointMap.containsKey(deviceId)) {
                continue;
            }

            ReduceTargetInfo info = new ReduceTargetInfo();
            info.setDeviceId(deviceId);
            info.setBucketId(bucketId);
            info.setPointIdList(devicePointMap.get(deviceId));
            result.add(info);
        }
        return result;
    }
}
