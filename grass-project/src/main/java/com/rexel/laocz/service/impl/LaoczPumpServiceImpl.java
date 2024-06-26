package com.rexel.laocz.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.common.core.domain.vo.BaseNameValueVO;
import com.rexel.common.exception.CustomException;
import com.rexel.common.exception.ServiceException;
import com.rexel.laocz.domain.*;
import com.rexel.laocz.domain.dto.AreaFireZoneNameDTO;
import com.rexel.laocz.domain.dto.PumpAddDto;
import com.rexel.laocz.domain.dto.PumpImportDto;
import com.rexel.laocz.domain.vo.*;
import com.rexel.laocz.mapper.LaoczPumpMapper;
import com.rexel.laocz.service.*;
import com.rexel.laocz.utils.EquipmentUtils;
import com.rexel.system.domain.GrassPointInfo;
import com.rexel.system.service.IGrassPointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.rexel.laocz.constant.WinePointConstants.PUMP_MARK;

/**
 * 泵管理Service业务层处理
 *
 * @author grass-service
 * @date 2024-03-06
 */
@Service
public class LaoczPumpServiceImpl extends ServiceImpl<LaoczPumpMapper, LaoczPump> implements ILaoczPumpService {

    @Autowired
    private ILaoczFireZoneInfoService iLaoczFireZoneInfoService;

    @Autowired
    private ILaoczAreaInfoService iLaoczAreaInfoService;

    @Autowired
    private ILaoczPumpPointService iLaoczPumpPointService;

    @Autowired
    private ILaoczWineDetailsService iLaoczWineDetailsService;

    @Autowired
    private IGrassPointService iGrassPointService;

    @Autowired
    private ILaoczWeighingTankService ILaoczWeighingTankService;

    @Autowired
    private ILaoczWeighingTankPointService iLaoczWeighingTankPointService;

    /**
     * 查询泵管理列表
     *
     * @param laoczPump 泵管理
     * @return 泵管理
     */
    @Override
    public List<LaoczPumpVo> selectLaoczPumpList(LaoczPump laoczPump) {
        return baseMapper.selectLaoczPumpList(laoczPump);
    }

    /**
     * 查询泵管理详情
     */
    @Override
    public PumpAddDto getPumpDetail(Long pumpId) {
        //查询使用标识的名字
        List<BaseNameValueVO> dictCache = EquipmentUtils.getDictCache(PUMP_MARK);
        Map<String, String> pumpMark = dictCache.stream().collect(Collectors.toMap(baseNameValueVO -> baseNameValueVO.getValue().toString(), BaseNameValueVO::getName));

        //获取泵管理数据
        LaoczPump laoczPump = this.getById(pumpId);
        //通过泵管理对象信息获取防火区id
        Long fireZoneId = laoczPump.getFireZoneId();
        //通过防火区id获取防火区信息
        LaoczFireZoneInfo laoczFireZoneInfo = iLaoczFireZoneInfoService.getById(fireZoneId);
        //通过防火区信息的防火区id获取场区信息
        LaoczAreaInfo laoczAreaInfo = iLaoczAreaInfoService.getById(laoczFireZoneInfo.getAreaId());

        //构建泵管理对象
        PumpAddDto pumpAddDto = new PumpAddDto();
        //数据拷贝
        BeanUtil.copyProperties(laoczPump, pumpAddDto);
        //封装泵管理对象的基础信息
        pumpAddDto.setAreaId(laoczAreaInfo.getAreaId());
        pumpAddDto.setFireZoneId(laoczFireZoneInfo.getFireZoneId());

        //查询泵相关测点维护表
        List<LaoczPumpPoint> list = iLaoczPumpPointService
                .lambdaQuery()
                .eq(LaoczPumpPoint::getPumpId, pumpId)
                .list();
        List<GrassPointInfo> grassPointInfos = iGrassPointService.selectGrassPointInfoByIds(list.stream().map(LaoczPumpPoint::getPointPrimaryKey).collect(Collectors.toList()));
        Map<Long, GrassPointInfo> pointInfoMap = grassPointInfos.stream().collect(Collectors.toMap(GrassPointInfo::getId, Function.identity()));


        List<WeighingTankAddVo> addVoList = list.stream().map((item) -> {
                    WeighingTankAddVo weighingTankAddVo = new WeighingTankAddVo();
                    weighingTankAddVo.setEquipmentPointId(item.getEquipmentPointId());
                    weighingTankAddVo.setUseMark(item.getUseMark());
                    weighingTankAddVo.setPointPrimaryKey(item.getPointPrimaryKey());
            if (pointInfoMap.containsKey(item.getPointPrimaryKey())) {
                GrassPointInfo grassPointInfo = pointInfoMap.get(item.getPointPrimaryKey());
                weighingTankAddVo.setPointId(grassPointInfo.getPointId());
                weighingTankAddVo.setPointName(grassPointInfo.getPointName());
            }
            if (pumpMark.containsKey(item.getUseMark())) {
                weighingTankAddVo.setName(pumpMark.get(item.getUseMark()));
                    }
                    return weighingTankAddVo;
                }
        ).collect(Collectors.toList());

        pumpAddDto.setWeighingTankAddVos(addVoList);

        return pumpAddDto;
    }

    /**
     * 新增泵管理
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addPump(PumpAddDto pumpAddDto) {
        //验证防火区泵编号是否已存在
        //验证防火区是否存在，以及该防火区下是否存在泵
        //验证测点与应有的测点是否都存在，以及所选测点是否被占用
        pumpNumberCeck(null, pumpAddDto.getPumpNumber());
        pumpFireZoneCheck(null, pumpAddDto.getFireZoneId());
        pointCheck(null, pumpAddDto.getWeighingTankAddVos());

        LaoczPump laoczPump = new LaoczPump();
        BeanUtil.copyProperties(pumpAddDto, laoczPump);
        save(laoczPump);

        List<LaoczPumpPoint> laoczPumpPoints = pumpAddDto.getWeighingTankAddVos().stream().map(weighingTankAddVo -> {
            LaoczPumpPoint laoczPumpPoint = new LaoczPumpPoint();
            laoczPumpPoint.setUseMark(weighingTankAddVo.getUseMark());
            laoczPumpPoint.setPumpId(laoczPump.getPumpId());
            laoczPumpPoint.setPointPrimaryKey(weighingTankAddVo.getPointPrimaryKey());
            return laoczPumpPoint;
        }).collect(Collectors.toList());
        return iLaoczPumpPointService.saveBatch(laoczPumpPoints);
    }

    /**
     * 测点校验
     *
     * @param id                 泵id
     * @param weighingTankAddVos 泵相关测点
     */
    private void pointCheck(Long id, List<WeighingTankAddVo> weighingTankAddVos) {
        if (CollectionUtil.isEmpty(weighingTankAddVos)) {
            throw new CustomException("必须配置测点信息");
        }
        List<BaseNameValueVO> dictCache = EquipmentUtils.getDictCache(PUMP_MARK);
        if (CollectionUtil.isEmpty(dictCache)) {
            throw new ServiceException("使用标识为空,请维护数据字典");
        }
        Map<String, WeighingTankAddVo> map = weighingTankAddVos.stream().collect(Collectors.toMap(WeighingTankAddVo::getUseMark, Function.identity()));
        for (BaseNameValueVO baseNameValueVO : dictCache) {
            if (!map.containsKey(baseNameValueVO.getValue().toString())) {
                throw new ServiceException("使用标识" + baseNameValueVO.getName() + "未绑定测点");
            }
        }
        List<GrassPointInfo> grassPointInfos = iGrassPointService.selectGrassPointInfoByIds(weighingTankAddVos.stream().map(WeighingTankAddVo::getPointPrimaryKey).collect(Collectors.toList()));
        if (CollectionUtil.isEmpty(grassPointInfos)) {
            throw new ServiceException("测点不存在");
        }
        Set<Long> pointPointPrimaryKeySet = weighingTankAddVos.stream().map(WeighingTankAddVo::getPointPrimaryKey).collect(Collectors.toSet());
        if (pointPointPrimaryKeySet.size() != weighingTankAddVos.size()) {
            throw new CustomException("测点重复,请检查");
        }
        for (WeighingTankAddVo weighingTankAddVo : weighingTankAddVos) {
            GrassPointInfo grassPointInfo = grassPointInfos.stream().filter((item) -> item.getId().equals(weighingTankAddVo.getPointPrimaryKey())).findFirst().orElse(null);
            if (grassPointInfo == null) {
                throw new CustomException("测点:{}不存在", weighingTankAddVo.getName());
            }
        }
        Map<Long, WeighingTankAddVo> collect = weighingTankAddVos.stream().collect(Collectors.toMap(WeighingTankAddVo::getPointPrimaryKey, Function.identity()));
        //验证测点是否被其他泵占用
        List<Long> pointIds = new ArrayList<>(collect.keySet());
        List<LaoczPumpPoint> list = iLaoczPumpPointService.lambdaQuery().in(LaoczPumpPoint::getPointPrimaryKey, pointIds)
                .ne(id != null, LaoczPumpPoint::getPumpId, id).list();
        if (CollectionUtil.isNotEmpty(list)) {
            List<String> errorMag = new ArrayList<>();
            for (LaoczPumpPoint pumpPoint : list) {
                WeighingTankAddVo weighingTankAddVo = collect.get(pumpPoint.getPointPrimaryKey());
                if (weighingTankAddVo != null) {
                    errorMag.add(StrUtil.format("{}测点已经被使用", weighingTankAddVo.getName()));
                }
            }
            throw new CustomException(StrUtil.join(",", errorMag));
        }
        //验证测点是否被称重罐占用
        List<LaoczWeighingTankPoint> weighingTankPoints = iLaoczWeighingTankPointService.lambdaQuery().in(LaoczWeighingTankPoint::getPointPrimaryKey, pointIds).list();
        if (CollectionUtil.isNotEmpty(weighingTankPoints)) {
            List<String> errorMag = new ArrayList<>();
            for (LaoczWeighingTankPoint weighingTankPoint : weighingTankPoints) {
                WeighingTankAddVo weighingTankAddVo = collect.get(weighingTankPoint.getPointPrimaryKey());
                if (weighingTankAddVo != null) {
                    errorMag.add(StrUtil.format("{}测点已经被称重罐使用", weighingTankAddVo.getName()));
                }
            }
            throw new CustomException(StrUtil.join(",", errorMag));
        }
    }

    /**
     * 防火区校验
     *
     * @param id         泵id
     * @param fireZoneId 防火区id
     */
    private void pumpFireZoneCheck(Long id, Long fireZoneId) {
        if (fireZoneId == null) {
            throw new ServiceException("防火区不能为空");
        }
        LaoczFireZoneInfo laoczFireZoneInfo = iLaoczFireZoneInfoService.getById(fireZoneId);
        if (laoczFireZoneInfo == null) {
            throw new ServiceException("防火区不存在");
        }
        Integer count = this.lambdaQuery().eq(LaoczPump::getFireZoneId, fireZoneId).
                ne(id != null, LaoczPump::getPumpId, id).count();
        if (count > 0) {
            throw new ServiceException("该防火区已存在泵,请删除后重新操作");
        }
    }

    /**
     * 泵编号校验
     *
     * @param id         泵id
     * @param pumpNumber 泵编号
     */
    private void pumpNumberCeck(Long id, String... pumpNumber) {
        if (pumpNumber == null || pumpNumber.length == 0) {
            throw new ServiceException("泵编号不能为空");
        }
        for (int i = 0; i < pumpNumber.length; i++) {
            if (StrUtil.isEmpty(pumpNumber[i])) {
                throw new ServiceException("第" + (i + 2) + "行泵编号为空");
            }
        }
        Set<String> strings = CollectionUtil.newHashSet(pumpNumber);
        Integer count = this.lambdaQuery().in(LaoczPump::getPumpNumber, strings)
                .ne(id != null, LaoczPump::getPumpId, id).count();
        if (count > 0) {
            throw new ServiceException("泵编号已存在");
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateByIdWithPump(PumpAddDto pumpAddDto) {
        LaoczPump laoczPump = this.getById(pumpAddDto.getPumpId());
        if (laoczPump == null) {
            throw new ServiceException("泵不存在");
        }
        List<WeighingTankAddVo> weighingTankAddVos = pumpAddDto.getWeighingTankAddVos();

        pumpNumberCeck(laoczPump.getPumpId(), pumpAddDto.getPumpNumber());
        pumpFireZoneCheck(laoczPump.getPumpId(), pumpAddDto.getFireZoneId());
        pointCheck(laoczPump.getPumpId(), weighingTankAddVos);

        List<LaoczPumpPoint> laoczPumpPoints = BeanUtil.copyToList(pumpAddDto.getWeighingTankAddVos(), LaoczPumpPoint.class);
        laoczPumpPoints.forEach(item -> item.setPumpId(laoczPump.getPumpId()));
        //先删后加
        iLaoczPumpPointService.lambdaUpdate().eq(LaoczPumpPoint::getPumpId, laoczPump.getPumpId()).remove();
        iLaoczPumpPointService.saveBatch(laoczPumpPoints);
        return updateById(BeanUtil.copyProperties(pumpAddDto, LaoczPump.class));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByIdWithPoint(Long pumpId) {
        //泵如果在使用中，禁止删除
        Integer count = iLaoczWineDetailsService.lambdaQuery().eq(LaoczWineDetails::getPumpId, pumpId).count();
        if (count > 0) {
            throw new ServiceException("泵在被使用，禁止删除");
        }
        //删除泵管理数据
        this.removeById(pumpId);
        //与该泵相关测点数据删除
        QueryWrapper<LaoczPumpPoint> wrapper = new QueryWrapper<>();
        wrapper.eq("pump_id", pumpId);
        return iLaoczPumpPointService.remove(wrapper);
    }

    @Override
    public List<PointInfo> getPointInfo(Long pumpId) {
        // 获取所有的测点并根据测点获取测点信息
        return baseMapper.getPointInfo(pumpId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean importPump(List<PumpImportDto> pumpImportDtos) {
        // 导入数据验证
        importCheck(pumpImportDtos);
        //验证泵编号
        pumpNumberCeck(null, pumpImportDtos.stream().map(PumpImportDto::getPumpNumber).toArray(String[]::new));
        //验证防火区是否存在，以及该防火区下是否存在泵 key为防火区名称+防火区名称的map
        Map<String, AreaFireZoneInfo> stringAreaFireZoneInfoMap = importPumpFireZoneCheck(pumpImportDtos);
        /*
          验证测点与应有的测点是否都存在，以及所选测点是否被占用,
          验证思路,根据导入数据的device_id，point_id，point_type查询测点表，判断是否存在
          如果存在则查询测点是否被占用,关联查询laocz_pump，laocz_pump_point表，如果存在需要查询出来对应的泵，需使用泵名称进行报错
         */
        List<GrassPointInfo> grassPointInfos = importPointCheck(pumpImportDtos);
        Map<String, GrassPointInfo> pointInfoMap = grassPointInfos.stream().collect(Collectors.toMap(grassPointInfo -> grassPointInfo.getDeviceId() + grassPointInfo.getPointId() + grassPointInfo.getPointType(), Function.identity()));

        //保存泵信息
        for (PumpImportDto pumpImportDto : pumpImportDtos) {
            pumpImportDto.setFireZoneId(stringAreaFireZoneInfoMap.get(pumpImportDto.getAreaName() + pumpImportDto.getFireZoneName()).getFireZoneId());
        }
        List<LaoczPump> laoczPumps = BeanUtil.copyToList(pumpImportDtos, LaoczPump.class);
        //根据泵编号去重
        laoczPumps = laoczPumps.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(LaoczPump::getPumpNumber))), ArrayList::new));
        saveBatch(laoczPumps);
        //保存泵测点信息
        Map<String, Long> map = laoczPumps.stream().collect(Collectors.toMap(LaoczPump::getPumpNumber, LaoczPump::getPumpId));
        List<LaoczPumpPoint> pumpPoints = pumpImportDtos.stream().map(pumpImportDto -> {
            LaoczPumpPoint laoczPumpPoint = new LaoczPumpPoint();
            laoczPumpPoint.setPumpId(map.get(pumpImportDto.getPumpNumber()));
            laoczPumpPoint.setPointPrimaryKey(pointInfoMap.get(pumpImportDto.getDeviceId() + pumpImportDto.getPointId() + pumpImportDto.getPointType()).getId());
            laoczPumpPoint.setUseMark(pumpImportDto.getUseMark());
            return laoczPumpPoint;
        }).collect(Collectors.toList());
        return iLaoczPumpPointService.saveBatch(pumpPoints);
    }

    /**
     * 根据测点主键查询泵测点信息
     *
     * @param pointPrimaryKeys 测点主键
     * @return 泵测点信息
     */
    @Override
    public List<LaoczPumpPointInfo> selectLaoczPumpPointInfoByPointPrimaryKeys(List<Long> pointPrimaryKeys) {
        return baseMapper.selectLaoczPumpPointInfoByPointPrimaryKeys(pointPrimaryKeys);
    }

    private List<GrassPointInfo> importPointCheck(List<PumpImportDto> pumpImportDtos) {
        //验证deviceId,pointId,pointType，不能为空,且不能重复
        List<String> errList = new ArrayList<>();
        //key:deviceId+pointId+pointType,行数
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < pumpImportDtos.size(); i++) {
            PumpImportDto pumpImportDto = pumpImportDtos.get(i);
            if (StrUtil.isEmpty(pumpImportDto.getDeviceId())) {
                errList.add("第" + (i + 2) + "行deviceId为空");
            }
            if (StrUtil.isEmpty(pumpImportDto.getPointId())) {
                errList.add("第" + (i + 2) + "行pointId为空");
            }
            if (StrUtil.isEmpty(pumpImportDto.getPointType())) {
                errList.add("第" + (i + 2) + "行pointType为空");
            }
            String key = pumpImportDto.getDeviceId() + pumpImportDto.getPointId() + pumpImportDto.getPointType();
            if (map.containsKey(key)) {
                errList.add("第" + (i + 2) + "行deviceId,pointId,pointType重复");
            } else {
                map.put(key, i);
            }
        }
        if (CollectionUtil.isNotEmpty(errList)) {
            throw new ServiceException(StrUtil.join("\n", errList));
        }
        map.clear();
        //验证测点是否存在
        List<GrassPointInfo> toList = BeanUtil.copyToList(pumpImportDtos, GrassPointInfo.class);
        List<GrassPointInfo> pointInfos = iGrassPointService.selectByDeviceIdAndPointId(toList);
        Map<String, List<GrassPointInfo>> pointInfoMap = pointInfos.stream().collect(Collectors.groupingBy(GrassPointInfo::getDeviceId));
        for (PumpImportDto pumpImportDto : pumpImportDtos) {
            String deviceId = pumpImportDto.getDeviceId();
            String pointId = pumpImportDto.getPointId();
            if (pointInfoMap.containsKey(deviceId)) {
                List<GrassPointInfo> grassPointInfos = pointInfoMap.get(deviceId);
                if (CollectionUtil.isNotEmpty(grassPointInfos)) {
                    List<String> pointIdList = grassPointInfos.stream().map(GrassPointInfo::getPointId).collect(Collectors.toList());
                    if (!pointIdList.contains(pointId)) {
                        throw new ServiceException("物联设备编码:" + deviceId + "下不存在测点编码:" + pointId);
                    }
                }
            } else if (StrUtil.isNotEmpty(deviceId)) {
                throw new ServiceException("不存在物联设备编码:" + deviceId);
            }
        }
        pointInfoMap.clear();
        /*
            验证测点是否被占用，关联查询laocz_pump，laocz_pump_point表，如果存在需要查询出来对应的泵，需使用泵名称进行报错
            关联查询laocz_weighing_tank，laocz_weighing_tank_point表，如果存在需要查询出来对应的称重罐，需使用称重罐名称进行报错
         */
        List<LaoczPumpPointInfo> laoczPumpPointInfos = this.selectLaoczPumpPointInfoByPointPrimaryKeys(pointInfos.stream().map(GrassPointInfo::getId).collect(Collectors.toList()));
        if (CollectionUtil.isNotEmpty(laoczPumpPointInfos)) {
            Map<String, List<LaoczPumpPointInfo>> laoczPumpPointInfoMap = laoczPumpPointInfos.stream().collect(Collectors.groupingBy(LaoczPumpPointInfo::getPumpNumber));
            for (Map.Entry<String, List<LaoczPumpPointInfo>> stringListEntry : laoczPumpPointInfoMap.entrySet()) {
                List<LaoczPumpPointInfo> value = stringListEntry.getValue();
                List<String> errorMag = new ArrayList<>();
                List<Long> pointPrimaryKey = value.stream().map(LaoczPumpPointInfo::getPointPrimaryKey).collect(Collectors.toList());
                List<String> pointName = pointInfos.stream().filter((item) -> pointPrimaryKey.contains(item.getId())).map(GrassPointInfo::getPointName).collect(Collectors.toList());
                errorMag.add("泵编号:" + stringListEntry.getKey() + "下测点:" + StrUtil.join(",", pointName) + "已经使用");
                if (CollectionUtil.isNotEmpty(errorMag)) {
                    throw new ServiceException(StrUtil.join("\n", errorMag));
                }
            }
        }
        List<LaoczWeighingTankPointInfo> laoczWeighingTankPointInfos = ILaoczWeighingTankService.selectLaoczWeighingTankPointInfoByPointPrimaryKeys(pointInfos.stream().map(GrassPointInfo::getId).collect(Collectors.toList()));
        if (CollectionUtil.isNotEmpty(laoczWeighingTankPointInfos)) {
            Map<String, List<LaoczWeighingTankPointInfo>> laoczWeighingTankPointInfoMap = laoczWeighingTankPointInfos.stream().collect(Collectors.groupingBy(LaoczWeighingTankPointInfo::getWeighingTankNumber));
            for (Map.Entry<String, List<LaoczWeighingTankPointInfo>> stringListEntry : laoczWeighingTankPointInfoMap.entrySet()) {
                List<LaoczWeighingTankPointInfo> value = stringListEntry.getValue();
                List<String> errorMag = new ArrayList<>();
                List<Long> pointPrimaryKey = value.stream().map(LaoczWeighingTankPointInfo::getPointPrimaryKey).collect(Collectors.toList());
                List<String> pointName = pointInfos.stream().filter((item) -> pointPrimaryKey.contains(item.getId())).map(GrassPointInfo::getPointName).collect(Collectors.toList());
                errorMag.add("称重罐编号:" + stringListEntry.getKey() + "下测点:" + StrUtil.join(",", pointName) + "已经使用");
                if (CollectionUtil.isNotEmpty(errorMag)) {
                    throw new ServiceException(StrUtil.join("\n", errorMag));
                }
            }
        }
        return pointInfos;
    }

    /**
     * 验证防火区是否存在，以及该防火区下是否存在泵,并返回key为防火区名称+防火区名称的map
     *
     * @param pumpImportDtos 导入的泵数据
     * @return key为防火区名称+防火区名称的map
     */
    private Map<String, AreaFireZoneInfo> importPumpFireZoneCheck(List<PumpImportDto> pumpImportDtos) {
        //先验证导入的防火区是否都存在
        List<AreaFireZoneNameDTO> areaFireZoneNameDTOS = BeanUtil.copyToList(pumpImportDtos, AreaFireZoneNameDTO.class);
        List<AreaFireZoneInfo> areaFireZoneInfos = iLaoczFireZoneInfoService.selectAreaFireZoneInfoList(areaFireZoneNameDTOS);
        if (CollectionUtil.isEmpty(areaFireZoneInfos)) {
            throw new ServiceException("防火区都不存在");
        }
        Map<String, AreaFireZoneInfo> areaFireZoneInfoMap = areaFireZoneInfos.stream().collect(Collectors.toMap(item -> item.getAreaName() + item.getFireZoneName(), Function.identity()));
        List<String> errList = new ArrayList<>();
        for (int i = 0; i < pumpImportDtos.size(); i++) {
            PumpImportDto pumpImportDto = pumpImportDtos.get(i);
            AreaFireZoneInfo areaFireZoneInfo = areaFireZoneInfoMap.get(pumpImportDto.getAreaName() + pumpImportDto.getFireZoneName());
            if (areaFireZoneInfo == null) {
                errList.add("第" + (i + 2) + "行防火区不存在");
            }
        }
        if (CollectionUtil.isNotEmpty(errList)) {
            throw new ServiceException(StrUtil.join("\n", errList));
        }
        //验证每个防火区内是否有泵
        List<String> errList2 = new ArrayList<>();
        for (int i = 0; i < pumpImportDtos.size(); i++) {
            PumpImportDto pumpImportDto = pumpImportDtos.get(i);
            AreaFireZoneInfo areaFireZoneInfo = areaFireZoneInfoMap.get(pumpImportDto.getAreaName() + pumpImportDto.getFireZoneName());
            if (areaFireZoneInfo.getPumpCount() > 0) {
                errList2.add("第" + (i + 2) + "行防火区内已存在泵");
            }
        }
        if (CollectionUtil.isNotEmpty(errList2)) {
            throw new ServiceException(StrUtil.join("\n", errList2));
        }
        return areaFireZoneInfoMap;
    }

    private void importCheck(List<PumpImportDto> pumpImportDtos) {
        if (CollectionUtil.isEmpty(pumpImportDtos)) {
            throw new ServiceException("导入数据为空");
        }
        List<String> errList = new ArrayList<>();
        // 校验数据
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < pumpImportDtos.size(); i++) {
            PumpImportDto pumpImportDto = pumpImportDtos.get(i);
            if (StrUtil.isEmpty(pumpImportDto.getPumpNumber())) {
                errList.add("第" + (i + 2) + "行泵编号为空");
            }
            if (ObjectUtil.isNull(pumpImportDto.getAreaName())) {
                errList.add("第" + (i + 2) + "行归属区域为空");
            }
            if (StrUtil.isEmpty(pumpImportDto.getFireZoneName())) {
                errList.add("第" + (i + 2) + "行防火区为空");
            }
            if (StrUtil.isEmpty(pumpImportDto.getUseMark())) {
                errList.add("第" + (i + 2) + "行使用标识为空");
            }
            if (StrUtil.isEmpty(pumpImportDto.getPointId())) {
                errList.add("第" + (i + 2) + "行测点为空");
            }
            String value = pumpImportDto.getAreaName() + pumpImportDto.getFireZoneName();
            //验证同一个泵编号，不能处于不同的归属区域和归属防火区
            if (map.containsKey(pumpImportDto.getPumpNumber())) {
                String s = map.get(pumpImportDto.getPumpNumber());
                if (!s.equals(value)) {
                    errList.add("第" + (i + 2) + "行泵编号:" + pumpImportDto.getPumpNumber() + "在不同的归属区域");
                }
            } else {
                map.put(pumpImportDto.getPumpNumber(), value);
            }
        }
        map.clear();
        if (CollectionUtil.isNotEmpty(errList)) {
            throw new ServiceException(StrUtil.join("\n", errList));
        }
    }

}
