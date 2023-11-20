package com.rexel.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.common.config.PulseUrlConfig;
import com.rexel.common.core.domain.DynamicHeadExcel;
import com.rexel.common.core.domain.entity.GrassAssetPoint;
import com.rexel.common.core.redis.RedisCache;
import com.rexel.common.enums.QtyStatus;
import com.rexel.common.exception.ServiceException;
import com.rexel.common.utils.SecurityUtils;
import com.rexel.common.utils.SequenceUtils;
import com.rexel.common.utils.StringUtils;
import com.rexel.common.utils.pulse.PulseHttpRequestUtil;
import com.rexel.common.utils.pulse.PulseHttpResponseUtil;
import com.rexel.system.domain.GrassAsset;
import com.rexel.system.domain.GrassAssetName;
import com.rexel.system.domain.GrassAssetType;
import com.rexel.system.domain.GrassPointInfo;
import com.rexel.system.domain.dto.*;
import com.rexel.system.domain.vo.*;
import com.rexel.system.mapper.GrassAssetMapper;
import com.rexel.system.service.IGrassAssetPointService;
import com.rexel.system.service.IGrassAssetService;
import com.rexel.system.service.IGrassAssetTypeService;
import com.rexel.system.service.IGrassPointService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 资产Service业务层处理
 *
 * @author grass-service
 * @date 2022-07-19
 */
@Slf4j
@Service
public class GrassAssetServiceImpl extends ServiceImpl<GrassAssetMapper, GrassAsset> implements IGrassAssetService {

    @Autowired
    private IGrassAssetPointService assetPointService;
    @Autowired
    private IGrassPointService pointService;

    @Autowired
    private PulseUrlConfig pulseUrlConfig;

    @Autowired
    private IGrassPointService iGrassPointService;

    @Autowired
    private RedisCache redisCache;
    @Autowired
    private IGrassAssetTypeService grassAssetTypeService;


    @Override
    public GrassAsset getAssetById(Long id) {
        return baseMapper.getAssetById(id);
    }

    /**
     * @param grassAsset
     * @return
     */
    @Override
    public Long selectGrassAssetListCount(GrassAssetQueryDTO grassAsset) {
        return baseMapper.getListPageCount(grassAsset);
    }

    /**
     * 查询资产列表
     *
     * @param grassAsset 资产
     * @return 资产
     */
    @Override
    public List<GrassAsset> selectGrassAssetList(GrassAssetQueryDTO grassAsset) {
        grassAsset.setTenantId(SecurityUtils.getTenantId());
        if (grassAsset.getAssetTypeId() == null) {
            return new ArrayList<>();
        }
        return baseMapper.getListPage(grassAsset);
    }

    @Override
    public List<GrassAssetExportVO> exportAssetList(GrassAssetQueryDTO grassAsset) {
        List<GrassAssetExportVO> grassAssetExportVOS = baseMapper.exportAssetList(grassAsset);
        //根据id去重
        List<GrassAssetExportVO> collect = grassAssetExportVOS.stream().collect(
                Collectors.collectingAndThen(
                        Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(GrassAssetExportVO::getId))), ArrayList::new)
        );

        Map<String, GrassAssetExportVO> map = collect.stream().collect(Collectors.toMap(GrassAssetExportVO::getId, Function.identity()));
        grassAssetExportVOS.forEach(grassAssetExportVO -> {
            String parentId = grassAssetExportVO.getParentId();
            if (StrUtil.isNotEmpty(parentId)) {
                if (map.containsKey(parentId)) {
                    grassAssetExportVO.setParentAssetTypeName(map.get(parentId).getAssetTypeName());
                }
            }
        });
        return grassAssetExportVOS;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveAsset(GrassAssetDTO grassAssetDTO) {
        lambdaQuery().eq(GrassAsset::getAssetName, grassAssetDTO.getAsset().getAssetName())
                .eq(GrassAsset::getAssetTypeId, grassAssetDTO.getAsset().getAssetTypeId())
                .eq(GrassAsset::getTenantId, SecurityUtils.getTenantId())
                .oneOpt().ifPresent(asset -> {
                    throw new ServiceException("资产名称已存在");
                });
        //新增
        GrassAsset grassAsset = grassAssetDTO.getAsset();
        grassAsset.setId(SequenceUtils.nextId().toString());
        super.save(grassAsset);
        List<GrassAssetPoint> list = getGrassAssetPoints(grassAssetDTO);
        if (CollectionUtil.isNotEmpty(list)) {
            assetPointService.saveBatch(list);
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateAsset(GrassAssetDTO grassAssetDTO) {
        lambdaQuery().eq(GrassAsset::getAssetName, grassAssetDTO.getAsset().getAssetName())
                .eq(GrassAsset::getAssetTypeId, grassAssetDTO.getAsset().getAssetTypeId())
                .eq(GrassAsset::getTenantId, SecurityUtils.getTenantId())
                .ne(GrassAsset::getId, grassAssetDTO.getAsset().getId())
                .oneOpt().ifPresent(asset -> {
                    throw new ServiceException("资产名称已存在");
                });
        //修改
        super.updateById(grassAssetDTO.getAsset());
        assetPointService.lambdaUpdate().eq(GrassAssetPoint::getAssetId, grassAssetDTO.getAsset().getId()).remove();
        List<GrassAssetPoint> list = getGrassAssetPoints(grassAssetDTO);
        if (CollectionUtil.isNotEmpty(list)) {
            assetPointService.saveBatch(list);
        }
        return true;
    }

    /**
     * 根据id获取修改详情  回显
     *
     * @param id
     * @return
     */
    @Override
    public GrassAssetInfoVO getAssetInfoById(Long id) {
        return new GrassAssetInfoVO(getById(id), pointService.selectByAssetId(id));
    }

    @Override
    public List<PointTimeDataVO> pointTimeData(GrassAssetPointDTO grassAssetPointDTO) {
        grassAssetPointDTO.setUserId(SecurityUtils.getUserId());
        List<PointTimeDataVO> pointTimeDataVOS = pointService.selectByAsset(grassAssetPointDTO);
        if (CollectionUtil.isEmpty(pointTimeDataVOS)) {
            return new ArrayList<>();
        }
        Map<String, List<String>> map = pointTimeDataVOS.stream().collect(
                Collectors.groupingBy(PointTimeDataVO::getDeviceId, Collectors.mapping(PointTimeDataVO::getPointId, Collectors.toList())));
        List<TimeSeriesLastVO> timeSeriesLastVOS = new ArrayList<>();
        map.forEach((k, v) -> {
            TimeSeriesLastDTO timeSeriesLastDTO = new TimeSeriesLastDTO();
            timeSeriesLastDTO.setDeviceId(k);
            timeSeriesLastDTO.setPointIdList(v);
            try {
                List<TimeSeriesLastVO> voList = PulseHttpResponseUtil
                        .pretreatmentResultsList(PulseHttpRequestUtil
                                .sendPostJson(pulseUrlConfig.getTimeSeriesBatch(), JSON.toJSONString(timeSeriesLastDTO)), TimeSeriesLastVO.class);
                if (CollectionUtil.isNotEmpty(voList)) {
                    timeSeriesLastVOS.addAll(voList);
                }
            } catch (Exception e) {
                log.error("[查询资产实时数据异常，物联设备id:{},测点id:{},异常信息:{}] ", k, v, e.getMessage());
            }
        });
        Map<String, TimeSeriesLastVO> stringTimeSeriesLastVOMap = timeSeriesLastVOS.stream().
                collect(Collectors.toMap(timeSeriesLastVO ->
                        timeSeriesLastVO.getDeviceId() + timeSeriesLastVO.getPointId(), Function.identity()));
        pointTimeDataVOS.forEach(pointTimeDataVO -> {
            String key = pointTimeDataVO.getDeviceId() + pointTimeDataVO.getPointId();
            if (stringTimeSeriesLastVOMap.containsKey(key)) {
                TimeSeriesLastVO seriesLastVO = stringTimeSeriesLastVOMap.get(key);
                pointTimeDataVO.setPointValue(seriesLastVO.getValue());
                pointTimeDataVO.setQty(seriesLastVO.getQty());
                pointTimeDataVO.setLastTime(seriesLastVO.getTime());

                //数据质量
                String qty = seriesLastVO.getQty();
                if (StringUtils.isNotEmpty(qty)) {
                    pointTimeDataVO.setQty(QtyStatus.getInfo(qty));
                }
            }
        });
        return pointTimeDataVOS;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean importAssetList(List<GrassAssetExportVO> grassImportList) {
        if (CollectionUtil.isEmpty(grassImportList)) {
            throw new ServiceException("导入数据为空");
        }


        //importAssetListCheck(grassImportList);
        //检查测点，返回测点集合
        List<GrassPointInfo> pointInfos = importAssetListPointCheck(grassImportList);
        Map<String, GrassPointInfo> pointInfoMap = pointInfos.stream().collect(Collectors.toMap(grassPointInfo ->
                grassPointInfo.getDeviceId() + grassPointInfo.getPointId(), Function.identity()));

        grassAssetTypeService.lambdaUpdate().remove();
        lambdaUpdate().remove();
        assetPointService.lambdaUpdate().remove();

        List<GrassAssetType> assetTypes = new ArrayList<>();
        List<GrassAsset> grassAssets = new ArrayList<>();
        List<GrassAssetPoint> grassAssetPoints = new ArrayList<>();

        Map<String, String> grassAssetTypeMap = new HashMap<>();
        Map<String, String> grassAssetMap = new HashMap<>();

        for (GrassAssetExportVO grassAssetExportVO : grassImportList) {
            if (!grassAssetTypeMap.containsKey(grassAssetExportVO.getAssetTypeName())) {
                //改造资产设备类型
                GrassAssetType assetType = new GrassAssetType(grassAssetExportVO.getAssetTypeName());
                //生成id
                String assetTypeId = SequenceUtils.nextId().toString();
                assetType.setId(assetTypeId);
                assetTypes.add(assetType);
                grassAssetTypeMap.put(grassAssetExportVO.getAssetTypeName(), assetTypeId);
            }
            if (!grassAssetMap.containsKey(grassAssetExportVO.getAssetTypeName() + grassAssetExportVO.getAssetName())
                    && StringUtils.isNotEmpty(grassAssetExportVO.getAssetName())) {
                //构造资产设备表
                GrassAsset grassAsset = BeanUtil.copyProperties(grassAssetExportVO, GrassAsset.class);
                String assetId = SequenceUtils.nextId().toString();
                grassAsset.setId(assetId);
                grassAsset.setAssetTypeId(grassAssetTypeMap.get(grassAssetExportVO.getAssetTypeName()));
                grassAssets.add(grassAsset);
                grassAssetMap.put(grassAssetExportVO.getAssetTypeName() + grassAssetExportVO.getAssetName(), assetId);
            }
            if (StrUtil.isNotEmpty(grassAssetExportVO.getDeviceId()) && StrUtil.isNotEmpty(grassAssetExportVO.getPointId())
                    && grassAssetMap.containsKey(grassAssetExportVO.getAssetTypeName() + grassAssetExportVO.getAssetName())) {
                //构造测点资产设备关联表
                GrassAssetPoint grassAssetPoint = new GrassAssetPoint();
                Long pointPrimaryKey = pointInfoMap.get(grassAssetExportVO.getDeviceId() + grassAssetExportVO.getPointId()).getId();
                grassAssetPoint.setPointPrimaryKey(pointPrimaryKey);
                grassAssetPoint.setAssetId(grassAssetMap.get(grassAssetExportVO.getAssetTypeName() + grassAssetExportVO.getAssetName()));
                grassAssetPoints.add(grassAssetPoint);
            }
        }


        //过滤 parentAssetTypeName 不为空的数据
        List<GrassAssetExportVO> grassAssetExportVOS = grassImportList.stream()
                .filter(grassAssetExportVO -> StrUtil.isNotEmpty(grassAssetExportVO.getParentAssetTypeName())).collect(Collectors.toList());
        Map<String, GrassAssetType> map = assetTypes.stream().collect(Collectors.toMap(GrassAssetType::getAssetTypeName, Function.identity()));
        for (GrassAssetExportVO grassAssetExportVO : grassAssetExportVOS) {
            if (map.containsKey(grassAssetExportVO.getAssetTypeName())) {
                GrassAssetType grassAssetType = map.get(grassAssetExportVO.getAssetTypeName());
                if (map.containsKey(grassAssetExportVO.getParentAssetTypeName())) {
                    GrassAssetType parentAssetType = map.get(grassAssetExportVO.getParentAssetTypeName());
                    grassAssetType.setParentId(parentAssetType.getId());
                }
            }
        }

        grassAssetTypeService.saveBatch(assetTypes);
        saveBatch(grassAssets);
        assetPointService.saveBatch(grassAssetPoints);
        return true;
    }

    private List<GrassPointInfo> importAssetListPointCheck(List<GrassAssetExportVO> grassImportList) {
        //同一个资产设备类型名称不能有两个不同的上级资产设备
        Map<String, String> stringMap = new HashMap<>();
        for (int i = 0; i < grassImportList.size(); i++) {
            String parentAssetTypeName = grassImportList.get(i).getParentAssetTypeName();
            if (StrUtil.isNotEmpty(parentAssetTypeName)) {
                String assetTypeName = grassImportList.get(i).getAssetTypeName();
                if (stringMap.containsKey(assetTypeName)) {
                    if (!stringMap.get(assetTypeName).equals(parentAssetTypeName)) {
                        throw new ServiceException("第" + (i + 1) + "行，同一个资产设备类型名称不能有两个不同的上级资产设备");
                    }
                }
                stringMap.put(assetTypeName, parentAssetTypeName);
            }
        }
        stringMap.clear();

        //上级资产设备不能是自己
        for (int i = 0; i < grassImportList.size(); i++) {
            GrassAssetExportVO grassAssetExportVO = grassImportList.get(i);
            if (StrUtil.isNotEmpty(grassAssetExportVO.getParentAssetTypeName()) && StrUtil.isNotEmpty(grassAssetExportVO.getAssetTypeName())) {
                if (grassAssetExportVO.getParentAssetTypeName().equals(grassAssetExportVO.getAssetTypeName())) {
                    throw new ServiceException("第" + (i + 1) + "行上级资产设备不能是自己");
                }
            }
        }

        //assetTypeNames 是否包含 parentAssetTypeNames
        List<String> parentAssetTypeNames = grassImportList.stream().map(GrassAssetExportVO::getParentAssetTypeName).collect(Collectors.toList());
        List<String> assetTypeNames = grassImportList.stream().map(GrassAssetExportVO::getAssetTypeName).collect(Collectors.toList());
        for (int i = 0; i < parentAssetTypeNames.size(); i++) {
            String parentAssetTypeName = parentAssetTypeNames.get(i);
            if (StrUtil.isNotEmpty(parentAssetTypeName) && !assetTypeNames.contains(parentAssetTypeName)) {
                throw new ServiceException("第" + (i + 1) + "行上级资产设备类型必须在资产设备类型中");
            }
        }


        //测点编码和物联设备id应该同时存在
        for (int i = 0; i < grassImportList.size(); i++) {
            String deviceId = grassImportList.get(i).getDeviceId();
            String pointId = grassImportList.get(i).getPointId();
            if (deviceId != null && pointId == null) {
                throw new ServiceException("第" + (i + 1) + "行测点编码为空");
            }
        }
        Map<String, Map<String, Object>> map = new HashMap<>();
        for (int i = 0; i < grassImportList.size(); i++) {
            String key = grassImportList.get(i).getAssetTypeName() + grassImportList.get(i).getAssetName();
            if (!map.containsKey(key)) {
                String pointKey = grassImportList.get(i).getDeviceId() + grassImportList.get(i).getPointId();
                Map<String, Object> stringObjectMap = map.get(key);
                if (stringObjectMap == null) {
                    stringObjectMap = new HashMap<>();
                }
                if (stringObjectMap.containsKey(pointKey)) {
                    throw new ServiceException("第" + (i + 1) + "行测点编码重复");
                }
                stringObjectMap.put(pointKey, null);
                map.put(key, stringObjectMap);
            }
        }
        map.clear();
        List<GrassPointInfo> toList = BeanUtil.copyToList(grassImportList, GrassPointInfo.class);
        List<GrassPointInfo> pointInfos = iGrassPointService.selectByDeviceIdAndPointId(toList);
        Map<String, List<GrassPointInfo>> pointInfoMap = pointInfos.stream().collect(Collectors.groupingBy(GrassPointInfo::getDeviceId));
        for (GrassAssetExportVO grassAssetExportVO : grassImportList) {
            if (pointInfoMap.containsKey(grassAssetExportVO.getDeviceId())) {
                List<GrassPointInfo> grassPointInfos = pointInfoMap.get(grassAssetExportVO.getDeviceId());
                if (CollectionUtil.isNotEmpty(grassPointInfos)) {
                    List<String> pointIdList = grassPointInfos.stream().map(GrassPointInfo::getPointId).collect(Collectors.toList());
                    if (!pointIdList.contains(grassAssetExportVO.getPointId())) {
                        throw new ServiceException("物联设备编码:" + grassAssetExportVO.getDeviceId() + "下不存在测点编码:" + grassAssetExportVO.getPointId());
                    }
                }
            } else if (StrUtil.isNotEmpty(grassAssetExportVO.getDeviceId())) {
                throw new ServiceException("不存在物联设备编码:" + grassAssetExportVO.getDeviceId());
            }
        }
        pointInfoMap.clear();
        return pointInfos;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeAsset(Long id) {

        //删除资产-测点关联关系
        QueryWrapper<GrassAssetPoint> assetPointWrapper = new QueryWrapper<>();
        assetPointWrapper.eq("asset_id", id);
        assetPointService.remove(assetPointWrapper);

        //删除资产
        return super.removeById(id);
    }

    /**
     * 测点历史检测信息
     *
     * @param grassPointHisDTO
     * @return
     */
    @Override
    public PointHisVO pointHis(GrassPointHisDTO grassPointHisDTO) {
        //校验
        pointHisCheck(grassPointHisDTO);

        String redisKey = JSON.toJSONString(grassPointHisDTO);
        if (redisCache.hasKey(redisKey)) {
            return redisCache.getCacheObject(redisKey);
        }

        PointHisVO pointHisVO = new PointHisVO();
        List<GrassPointInfo> list = iGrassPointService.lambdaQuery().in(GrassPointInfo::getId,
                grassPointHisDTO.getIds()).list();
        if (CollectionUtil.isEmpty(list)) {
            return pointHisVO;
        }
        //封装列头
        LinkedHashMap<String, String> headMap = new LinkedHashMap<>();
        headMap.put("time", "上报时间");
        for (GrassPointInfo grassPointInfo : list) {
            headMap.put(grassPointInfo.getDeviceId() + grassPointInfo.getPointId(), getHeadName(grassPointInfo));
        }
        pointHisVO.setHeaderMetadata(headMap);

        //根据deviceId+pointId分组进行查询历史数据
        Map<String, List<String>> map = list.stream().collect(Collectors.groupingBy(GrassPointInfo::getDeviceId,
                Collectors.mapping(GrassPointInfo::getPointId, Collectors.toList())));
        List<TimeSeriesDataVO> voList = new ArrayList<>();
        //遍历分组进行查询
        map.forEach((k, v) -> {
            TimeSeriesDataDTO timeSeriesData = new TimeSeriesDataDTO();
            timeSeriesData.setStartTime(DateUtil.formatDateTime(grassPointHisDTO.getFrom()));
            timeSeriesData.setStopTime(DateUtil.formatDateTime(grassPointHisDTO.getTo()));
            timeSeriesData.setBucketId(SecurityUtils.getLoginUser().getBucketId());
            timeSeriesData.setDeviceId(k);
            timeSeriesData.setPointIdList(v);
            List<TimeSeriesDataVO> timeSeriesDataVOS = PulseHttpResponseUtil.pretreatmentResultsList(PulseHttpRequestUtil.sendPostJson(pulseUrlConfig.getTimeSeriesData(),
                    JSON.toJSONString(timeSeriesData)), TimeSeriesDataVO.class);
            if (CollectionUtil.isNotEmpty(timeSeriesDataVOS)) {
                voList.addAll(timeSeriesDataVOS);
            }
        });
        //根据时间倒序排序
        voList.sort(Comparator.comparing(TimeSeriesDataVO::getTime).reversed());


        //封装数据
        LinkedHashMap<String, LinkedHashMap<String, String>> dateMapMap = new LinkedHashMap<>(voList.size() / list.size());
        for (TimeSeriesDataVO timeSeriesDataVO : voList) {
            String time = timeSeriesDataVO.getTime();
            if (!dateMapMap.containsKey(time)) {
                dateMapMap.put(time, null);
            }
            LinkedHashMap<String, String> stringStringMap = dateMapMap.get(time);
            if (stringStringMap == null) {
                stringStringMap = new LinkedHashMap<>(list.size());
            }
            stringStringMap.put(timeSeriesDataVO.getDeviceId() + timeSeriesDataVO.getPointId(), String.valueOf(timeSeriesDataVO.getValue()));
            dateMapMap.put(time, stringStringMap);
        }
        List<Map<String, String>> pointHisData = new ArrayList<>();
        dateMapMap.forEach((k, v) -> {
            LinkedHashMap<String, String> dataMap = new LinkedHashMap<>();
            dataMap.put("time", k);
            dataMap.putAll(v);
            pointHisData.add(dataMap);
        });
        pointHisVO.setPointHisData(pointHisData);
        // 缓存并保存结果集
        redisCache.setCacheObject(redisKey, pointHisVO, 10, TimeUnit.MINUTES);
        return pointHisVO;
    }

    /**
     * 构造excel
     *
     * @param pointHisVO
     * @return
     */
    @Override
    public DynamicHeadExcel buildExcel(PointHisVO pointHisVO) {
        DynamicHeadExcel dynamicHeadExcel = new DynamicHeadExcel();
        Map<String, String> metadata = pointHisVO.getHeaderMetadata();
        Set<String> keySet = metadata.keySet();
        List<List<String>> head = new ArrayList<>();
        for (String s : keySet) {
            head.add(Collections.singletonList(metadata.get(s)));
        }
        dynamicHeadExcel.setHead(head);
        List<List<String>> data = new ArrayList<>();
        List<Map<String, String>> pointHisData = pointHisVO.getPointHisData();
        for (Map<String, String> pointHisDatum : pointHisData) {
            List<String> list = new ArrayList<>();
            for (String s : keySet) {
                list.add(pointHisDatum.get(s));
            }
            data.add(list);
        }
        dynamicHeadExcel.setData(data);
        return dynamicHeadExcel;
    }

    /**
     * 根据assetTypeName和assetName查询assetId
     *
     * @param rulesVOs
     * @return
     */
    @Override
    public List<GrassAssetName> selectIdByName(List<RulesVO> rulesVOs) {
        return baseMapper.selectIdByName(rulesVOs);
    }

    @Override
    public List<GrassAsset> getAssetByPointPrKey(Long id) {
        return baseMapper.getAssetByPointPrKey(id);
    }

    @Override
    @Transactional
    public Boolean deleteByTenantId(String tenantId) {
        assetPointService.deleteToTenant(tenantId);
        return this.baseMapper.deleteToTenant(tenantId);
    }

    /**
     * @param assetTypeId
     * @return
     */
    @Override
    public List<String> getAssetIdsByAssetTypeId(String assetTypeId) {
        return baseMapper.getAssetIdsByAssetTypeId(assetTypeId);
    }

    @Override
    public List<GrassAssetIdAndNameVO> getAssetIdAndNameByTenantId() {
        List<GrassAsset> list = this.lambdaQuery().list();
        List<GrassAssetIdAndNameVO> grassAssetIdAndNameVOList = new ArrayList<>();
        for (GrassAsset grassAsset : list) {
            GrassAssetIdAndNameVO grassAssetIdAndNameVO = new GrassAssetIdAndNameVO();
            grassAssetIdAndNameVO.setId(grassAsset.getId());
            grassAssetIdAndNameVO.setAssetName(grassAsset.getAssetName());
            grassAssetIdAndNameVOList.add(grassAssetIdAndNameVO);
        }
        return grassAssetIdAndNameVOList;
    }

    /**
     * 获取列头名称
     *
     * @param grassPointInfo
     * @return
     */
    private String getHeadName(GrassPointInfo grassPointInfo) {
        StringBuilder sb = new StringBuilder();
        if (StrUtil.isNotEmpty(grassPointInfo.getPointName()) && StrUtil.isNotEmpty(grassPointInfo.getPointUnit())) {
            sb.append(grassPointInfo.getPointName());
            sb.append("_");
            sb.append(grassPointInfo.getPointUnit());
        } else {
            sb.append(grassPointInfo.getPointId());
        }
        return sb.toString();
    }

    /**
     * 校验参数 历史检测数据
     *
     * @param grassPointHisDTO
     */
    private void pointHisCheck(GrassPointHisDTO grassPointHisDTO) {
        List<Long> list = grassPointHisDTO.getIds();
        if (CollectionUtil.isEmpty(list)) {
            throw new ServiceException("测点信息不能为空");
        }
        if (list.size() > 10) {
            throw new ServiceException("测点信息不能超过10个");
        }
        Date from = grassPointHisDTO.getFrom();
        Date to = grassPointHisDTO.getTo();
        if (from == null || to == null) {
            throw new ServiceException("开始时间和结束时间不能为空");
        }
        if (to.getTime() - from.getTime() > 86400000 * 2) {
            throw new ServiceException("时间范围不能超过2天");
        }
    }

    /**
     * 修改资产关联测点
     * 先删除，后添加
     *
     * @param grassAsset grassAsset
     */
    private void updateAssetPoint(GrassAsset grassAsset) {
        LambdaQueryWrapper<GrassAssetPoint> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GrassAssetPoint::getAssetId, grassAsset.getId());
        assetPointService.remove(queryWrapper);

        //
        addAssetPoint(grassAsset);
    }

    /**
     * 添加资产关联的设备测点
     *
     * @param grassAsset grassAsset
     */
    private void addAssetPoint(GrassAsset grassAsset) {
        grassAsset.getPointList().forEach(a ->
        {
            a.setAssetId(grassAsset.getId());
            a.setPointPrimaryKey(a.getId());
        });
        assetPointService.saveBatch(grassAsset.getPointList());
    }


    /**
     * 生成资产测点信息
     *
     * @param grassAssetDTO
     * @return
     */
    private List<GrassAssetPoint> getGrassAssetPoints(GrassAssetDTO grassAssetDTO) {
        if (grassAssetDTO.getPointIds() == null || grassAssetDTO.getPointIds().length == 0) {
            return null;
        }
        List<GrassAssetPoint> list = new ArrayList<>();
        for (Long pointPrimaryKey : grassAssetDTO.getPointIds()) {
            GrassAssetPoint assetPoint = new GrassAssetPoint();
            assetPoint.setAssetId(grassAssetDTO.getAsset().getId());
            assetPoint.setPointPrimaryKey(pointPrimaryKey);
            list.add(assetPoint);
        }
        return list;
    }
}
