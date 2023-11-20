package com.rexel.system.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.common.exception.ServiceException;
import com.rexel.common.utils.SequenceUtils;
import com.rexel.system.domain.GrassPointInfo;
import com.rexel.system.domain.GrassPointTagInfo;
import com.rexel.system.domain.GrassPointTagPoint;
import com.rexel.system.domain.dto.GrassPointTagInfoDTO;
import com.rexel.system.domain.vo.GrassPointTagInfoDTVO;
import com.rexel.system.domain.vo.PointTagExportVO;
import com.rexel.system.mapper.GrassPointInfoMapper;
import com.rexel.system.mapper.GrassPointTagInfoMapper;
import com.rexel.system.mapper.GrassPointTagToPointMapper;
import com.rexel.system.service.IGrassPointTagInfoService;
import com.rexel.system.service.IGrassPointTagToPointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 测点标签信息Service业务层处理
 *
 * @author grass-service
 * @date 2022-10-17
 */
@Service
public class GrassPointTagInfoServiceImpl extends ServiceImpl<GrassPointTagInfoMapper, GrassPointTagInfo> implements IGrassPointTagInfoService {

    @Autowired
    private IGrassPointTagToPointService iGrassPointTagToPointService;

    @Autowired
    private GrassPointInfoMapper grassPointInfoMapper;
    @Autowired
    private GrassPointTagToPointMapper grassPointTagToPointMapper;


    /**
     * 查询测点标签信息列表
     *
     * @param grassPointTagInfo 测点标签信息
     * @return 测点标签信息
     */
    @Override
    public List<GrassPointTagInfo> selectGrassPointTagInfoList(GrassPointTagInfo grassPointTagInfo) {
        return baseMapper.selectGrassPointTagInfoList(grassPointTagInfo);
    }

    /**
     * 新增标签 新增关联测点
     * fhw
     *
     * @param grassPointTagInfoDTO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean savePointTagInfo(GrassPointTagInfoDTO grassPointTagInfoDTO) {
        // 判断有无重复标签
        GrassPointTagInfo grassPointTagInfo = baseMapper.selectTagKV(grassPointTagInfoDTO.getGrassPointTagInfo());
        if (grassPointTagInfo != null) {
            throw new ServiceException("标签已存在");
        }

        // 新增标签信息表
        grassPointTagInfoDTO.getGrassPointTagInfo().setId(SequenceUtils.nextId().toString());
        super.save(grassPointTagInfoDTO.getGrassPointTagInfo());
        List<Long> pointIds = grassPointTagInfoDTO.getPointIds();
        // 添加测点数量为0时，只增加标签信息
        if (CollectionUtil.isEmpty(pointIds)) {
            return true;
        }
        //根据测点的主键id，拿到拥有point_id和device_id的集合
        List<GrassPointInfo> grassPointInfos = grassPointInfoMapper.selectBatchIds(pointIds);

        // 新增测点标签
        List<GrassPointTagPoint> grassPointTagPoints = new ArrayList<>();
        for (GrassPointInfo grassPointInfo : grassPointInfos) {

            GrassPointTagPoint grassPointTagPoint = new GrassPointTagPoint();
            grassPointTagPoint.setId(SequenceUtils.nextId().toString());
            grassPointTagPoint.setPointId(grassPointInfo.getPointId());
            grassPointTagPoint.setDeviceId(grassPointInfo.getDeviceId());
            grassPointTagPoint.setPointType(grassPointInfo.getPointType());
            grassPointTagPoint.setPointTagInfoId(grassPointTagInfoDTO.getGrassPointTagInfo().getId());
            grassPointTagPoints.add(grassPointTagPoint);
        }
        if (CollectionUtil.isNotEmpty(grassPointTagPoints)) {
            iGrassPointTagToPointService.saveBatch(grassPointTagPoints);
        }
        return true;
    }

    /**
     * fhw 修改标签
     *
     * @param grassPointTagInfoDTO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updatePointTagInfo(GrassPointTagInfoDTO grassPointTagInfoDTO) {
        // 通过ID修改
        super.updateById(grassPointTagInfoDTO.getGrassPointTagInfo());

        iGrassPointTagToPointService.lambdaUpdate().eq(GrassPointTagPoint::getPointTagInfoId, grassPointTagInfoDTO.getGrassPointTagInfo().getId()).remove();

        List<GrassPointTagPoint> grassPointTagPoints = new ArrayList<>();
        if (CollectionUtil.isEmpty(grassPointTagInfoDTO.getPointIds())) {
            return true;
        }
        //根据测点的主键id，拿到拥有point_id和device_id的集合
        List<GrassPointInfo> grassPointInfos = grassPointInfoMapper.selectBatchIds(grassPointTagInfoDTO.getPointIds());

        if (CollectionUtil.isNotEmpty(grassPointInfos)) {
            for (GrassPointInfo grassPointInfo : grassPointInfos) {
                GrassPointTagPoint grassPointTagPoint = new GrassPointTagPoint();
                grassPointTagPoint.setPointTagInfoId(grassPointTagInfoDTO.getGrassPointTagInfo().getId());
                grassPointTagPoint.setPointId(grassPointInfo.getPointId());
                grassPointTagPoint.setDeviceId(grassPointInfo.getDeviceId());
                grassPointTagPoint.setPointType(grassPointInfo.getPointType());
                grassPointTagPoints.add(grassPointTagPoint);
            }
            if (CollectionUtil.isNotEmpty(grassPointTagPoints)) {
                iGrassPointTagToPointService.saveBatch(grassPointTagPoints);
            }
        }


        return true;
    }

    @Override
    public Boolean removePointTagInfo(Long pointTagInfoId) {
        // 删除测点标签信息
        QueryWrapper<GrassPointTagInfo> pointTagInfoWrapper = new QueryWrapper<>();
        pointTagInfoWrapper.eq("id", pointTagInfoId);
        super.remove(pointTagInfoWrapper);

        // 删除测点标签
        QueryWrapper<GrassPointTagPoint> pointTagWrapper = new QueryWrapper<>();
        pointTagWrapper.eq("point_tag_info_id", pointTagInfoId);
        iGrassPointTagToPointService.remove(pointTagWrapper);

        return true;
    }

    /**
     * 修改回显功能
     *
     * @param id 测点标签信息id
     * @return 返回封装标签信息和测点信息的对象集合
     */
    @Override
    public GrassPointTagInfoDTVO getPointTgeInfoById(Long id) {
        // 根据测点id和设备id，拿到所有测点
        List<GrassPointInfo> grassPointInfos = grassPointInfoMapper.selectBatchPointIdsByPointTagInfoId(id);
        return new GrassPointTagInfoDTVO(getById(id), grassPointInfos);
    }

    /**
     * 导入功能
     * fhw
     * 20221025
     *
     * @param pointTagExportVOS
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean importPoint(List<PointTagExportVO> pointTagExportVOS) {
        if (CollectionUtil.isEmpty(pointTagExportVOS)) {
            throw new ServiceException("导入数据为空");
        }
        // 数据验证
        check(pointTagExportVOS);

        // 清空测点标签信息表 和测点标签表
        iGrassPointTagToPointService.lambdaUpdate().remove();
        lambdaUpdate().remove();

        // 主表去重判断
        List<GrassPointTagInfo> grassPointTagInfos = new ArrayList<>();
        List<GrassPointTagPoint> grassPointTagPoints = new ArrayList<>();
        Map<String, String> grassPointTagInfoMap = new HashMap<>(pointTagExportVOS.size());

        for (PointTagExportVO pointTagExportVO : pointTagExportVOS) {
            if (!grassPointTagInfoMap.containsKey(pointTagExportVO.getTagKey() + pointTagExportVO.getTagValue() + pointTagExportVO.getRemark())) {
                // 构建标签信息对象
                GrassPointTagInfo grassPointTagInfo = new GrassPointTagInfo();
                grassPointTagInfo.setTagKey(pointTagExportVO.getTagKey());
                grassPointTagInfo.setTagValue(pointTagExportVO.getTagValue());
                grassPointTagInfo.setTagType(pointTagExportVO.getTagType());
                grassPointTagInfo.setRemark(pointTagExportVO.getRemark());
                //生成id
                String pointTagInfoId = SequenceUtils.nextId().toString();
                grassPointTagInfo.setId(pointTagInfoId);
                grassPointTagInfos.add(grassPointTagInfo);
                grassPointTagInfoMap.put(pointTagExportVO.getTagKey() + pointTagExportVO.getTagValue() + pointTagExportVO.getRemark(), pointTagInfoId);
            }
            GrassPointTagPoint grassPointTagPoint = new GrassPointTagPoint();
            grassPointTagPoint.setId(SequenceUtils.nextId().toString());
            grassPointTagPoint.setPointTagInfoId(grassPointTagInfoMap.get(pointTagExportVO.getTagKey() + pointTagExportVO.getTagValue() + pointTagExportVO.getRemark()));
            grassPointTagPoint.setPointId(pointTagExportVO.getPointId());
            grassPointTagPoint.setPointType(pointTagExportVO.getPointType());
            grassPointTagPoint.setDeviceId(pointTagExportVO.getDeviceId());
            grassPointTagPoints.add(grassPointTagPoint);
        }
        if (CollectionUtil.isNotEmpty(grassPointTagInfos)) {
            saveBatch(grassPointTagInfos);
        }
        if (CollectionUtil.isNotEmpty(grassPointTagPoints)) {
            iGrassPointTagToPointService.saveBatch(grassPointTagPoints);
        }
        return true;
    }

    private void check(List<PointTagExportVO> pointTagExportVOS) {
        List<String> errList = new ArrayList<>();
        // 校验数据
        for (int i = 0; i < pointTagExportVOS.size(); i++) {
            PointTagExportVO pointTagExportVO = pointTagExportVOS.get(i);
            if (StrUtil.isEmpty(pointTagExportVO.getTagKey())) {
                errList.add("第" + (i + 2) + "行标签key为空");
            }
            if (ObjectUtil.isNull(pointTagExportVO.getTagType())) {
                errList.add("第" + (i + 2) + "行标签类型为空");
            }
            if (StrUtil.isEmpty(pointTagExportVO.getDeviceId())) {
                errList.add("第" + (i + 2) + "行设备id为空");
            }
            if (StrUtil.isEmpty(pointTagExportVO.getPointId())) {
                errList.add("第" + (i + 2) + "行测点标识为空");
            }
            if (StrUtil.isEmpty(pointTagExportVO.getPointType())) {
                errList.add("第" + (i + 2) + "行测点类型为空");
            }
        }
        if (CollectionUtil.isNotEmpty(errList)) {
            throw new ServiceException(StrUtil.join("\n", errList));
        }
    }

    /**
     * 查询所有的TagKey去重后返回
     *
     * @return
     */
    @Override
    public List<String> selectTagKey() {
        return baseMapper.selectTagKey();
    }

    /**
     * 导出
     * fhw
     *
     * @return
     */
    @Override
    public List<PointTagExportVO> selectExport() {
        return baseMapper.selectExport();
    }

    @Override
    public Boolean deleteByTenantId(String tenantId) {
        return baseMapper.deleteByTenantId(tenantId);
    }


}
