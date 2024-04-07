package com.rexel.laocz.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.common.exception.ServiceException;
import com.rexel.laocz.domain.*;
import com.rexel.laocz.domain.dto.PumpAddDto;
import com.rexel.laocz.domain.dto.PumpImportDto;
import com.rexel.laocz.domain.dto.WeighingTankDto;
import com.rexel.laocz.domain.vo.LaoczPumpVo;
import com.rexel.laocz.domain.vo.PointInfo;
import com.rexel.laocz.domain.vo.WeighingTankAddVo;
import com.rexel.laocz.mapper.LaoczPumpMapper;
import com.rexel.laocz.service.*;
import com.rexel.system.domain.GrassPointInfo;
import com.rexel.system.domain.vo.PointQueryVO;
import com.rexel.system.mapper.GrassPointInfoMapper;
import com.rexel.system.service.IGrassPointService;
import com.rexel.system.service.ISysDictTypeService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

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
        List<LaoczPumpVo> laoczPumps = baseMapper.selectLaoczPumpList(laoczPump);
        return laoczPumps;
    }

    /**
     * 查询泵管理详情
     */
    @Override
    public PumpAddDto getPumpDetail(Long pumpId) {
        //查询使用标识的名字
        List<WeighingTankAddVo> pumpMark = ILaoczWeighingTankService.getAddVo("pump_mark");
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

        List<WeighingTankAddVo> addVoList = list.stream().map((item) -> {
                    WeighingTankAddVo weighingTankAddVo = new WeighingTankAddVo();
                    weighingTankAddVo.setEquipmentPointId(item.getEquipmentPointId());
                    weighingTankAddVo.setUseMark(item.getUseMark());
                    weighingTankAddVo.setPointPrimaryKey(item.getPointPrimaryKey());

                    //查询测点信息
                    GrassPointInfo grassPointInfo = iGrassPointService.getById(item.getPointPrimaryKey());
                    weighingTankAddVo.setPointId(grassPointInfo.getPointId());
                    weighingTankAddVo.setPointName(grassPointInfo.getPointName());

                    for (WeighingTankAddVo tankAddVo : pumpMark) {
                        if (tankAddVo.getUseMark().equals(item.getUseMark())) {
                            weighingTankAddVo.setName(tankAddVo.getName());
                        }
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
        //一个防火区只能有一个泵
        Integer countWithFire = this.lambdaQuery().eq(LaoczPump::getFireZoneId, pumpAddDto.getFireZoneId()).count();
        if (countWithFire > 0) {
            throw new ServiceException("该防火区已存在泵,请删除后重新添加");
        }

        Integer count = this.lambdaQuery().eq(LaoczPump::getPumpNumber, pumpAddDto.getPumpNumber()).count();

        if (count > 0) {
            throw new ServiceException("泵编号已存在");
        } else {
            // 泵管理插入
            LaoczPump laoczPump = new LaoczPump();
            BeanUtil.copyProperties(pumpAddDto, laoczPump);
            save(laoczPump);
            //泵管理相关测点维护插入
            List<LaoczPumpPoint> laoczPumpPoints = new ArrayList<>();

            Long pumpId = laoczPump.getPumpId();
            List<WeighingTankAddVo> weighingTankAddVos = pumpAddDto.getWeighingTankAddVos();

            for (WeighingTankAddVo weighingTankAddVo : weighingTankAddVos) {
                LaoczPumpPoint laoczPumpPoint = new LaoczPumpPoint();
                laoczPumpPoint.setUseMark(weighingTankAddVo.getUseMark());
                laoczPumpPoint.setPumpId(pumpId);

                //判断测点Id是否以及被占用，如果被占用，新增失败
                Integer countId = iLaoczPumpPointService.lambdaQuery().eq(LaoczPumpPoint::getPointPrimaryKey, weighingTankAddVo.getPointPrimaryKey()).count();
                if (countId > 0) {
                    //查询使用标识的名字
                    List<WeighingTankAddVo> weightMark = ILaoczWeighingTankService.getAddVo("pump_mark");
                    for (WeighingTankAddVo tankAddVo : weightMark) {
                        if (tankAddVo.getUseMark().equals(weighingTankAddVo.getUseMark())) {
                            throw new ServiceException(tankAddVo.getName() + "的绑定测点已经被使用，请重新选择正确测点进行新增");
                        }
                    }
                }
                laoczPumpPoint.setPointPrimaryKey(weighingTankAddVo.getPointPrimaryKey());

                laoczPumpPoints.add(laoczPumpPoint);
            }
            return iLaoczPumpPointService.saveBatch(laoczPumpPoints);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateByIdWithPump(PumpAddDto pumpAddDto) {
        // 首先查出未修改的该id的数据
        LaoczPump laoczPump = this.getById(pumpAddDto.getPumpId());

        Integer count = this.lambdaQuery().eq(LaoczPump::getPumpNumber, pumpAddDto.getPumpNumber()).count();

        if (count > 0 && !pumpAddDto.getPumpNumber().equals(laoczPump.getPumpNumber())) {
            throw new ServiceException("泵编号已存在");
        } else {
            //泵管理数据更新
            LaoczPump laoczPump1 = new LaoczPump();
            BeanUtil.copyProperties(pumpAddDto, laoczPump1);
            updateById(laoczPump1);

            //泵测点相关关联更新
            List<LaoczPumpPoint> pumpPointList = pumpAddDto.getWeighingTankAddVos().stream().map((item) -> {
                LaoczPumpPoint laoczPumpPoint = new LaoczPumpPoint();
                BeanUtil.copyProperties(item, laoczPumpPoint);
                laoczPumpPoint.setPumpId(pumpAddDto.getPumpId());
                return laoczPumpPoint;
            }).collect(Collectors.toList());

            //先将所有与该泵相关数据删除再进行插入
            QueryWrapper<LaoczPumpPoint> wrapper = new QueryWrapper<>();
            wrapper.eq("pump_id", pumpAddDto.getPumpId());

            iLaoczPumpPointService.remove(wrapper);

            return iLaoczPumpPointService.saveBatch(pumpPointList);
        }
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
        List<PointInfo> pointInfos = baseMapper.getPointInfo();
        return pointInfos;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean importPump(List<PumpImportDto> pumpImportDtos) {

        List<LaoczPumpPoint> pumpPoints = new ArrayList<>();
        if (CollectionUtil.isEmpty(pumpImportDtos)) {
            throw new ServiceException("导入数据为空");
        }

        // 数据验证
        check(pumpImportDtos);
        HashMap<String, String> pumpImportDtoMap = new HashMap<>(pumpImportDtos.size());

        //验证泵编号是否已存在
        for (PumpImportDto pumpImportDto : pumpImportDtos) {

            if (!pumpImportDtoMap.containsKey(pumpImportDto.getPumpNumber())) {
                QueryWrapper<LaoczPump> queryWrapper = new QueryWrapper<>();

                queryWrapper.eq("pump_number", pumpImportDto.getPumpNumber());

                int count = this.count(queryWrapper);

                if (count > 0) {
                    throw new ServiceException("泵编号已存在");
                }
                //获取防火区Id
                Long fireZoneId = iLaoczFireZoneInfoService.findFireZoneId(pumpImportDto.getAreaName(), pumpImportDto.getFireZoneName());
                pumpImportDto.setFireZoneId(fireZoneId);

                LaoczPump laoczPump = new LaoczPump();
                //数据拷贝
                BeanUtil.copyProperties(pumpImportDto, laoczPump);
                this.save(laoczPump);
                pumpImportDtoMap.put(pumpImportDto.getPumpNumber(), laoczPump.getPumpId().toString());
            }
            // 封装泵相关测点维护数据
            LaoczPumpPoint laoczPumpPoint = new LaoczPumpPoint();

            //根据测点pointId获取主键Id
            QueryWrapper<GrassPointInfo> wrapper = new QueryWrapper<>();
            wrapper.eq("point_id", pumpImportDto.getPointId());
            GrassPointInfo one = iGrassPointService.getOne(wrapper);
            if (one == null){
                throw new ServiceException(pumpImportDto.getPointId() + "不存在");
            }
            //判断pointId是否已经被占用
            Integer countId1 = iLaoczPumpPointService.lambdaQuery().eq(LaoczPumpPoint::getPointPrimaryKey, one.getId()).count();
            if (countId1 > 0) {
                throw new ServiceException(one.getPointId() + "测点已经被使用，请重新选择正确测点进行新增");
            }
            //判断pointId是否已经被占用
            Integer countId2 = iLaoczWeighingTankPointService.lambdaQuery().eq(LaoczWeighingTankPoint::getPointPrimaryKey, one.getId()).count();
            if (countId2 > 0) {
                throw new ServiceException(one.getPointId() + "测点已经被使用，请重新选择正确测点进行新增");
            }
            laoczPumpPoint.setUseMark(pumpImportDto.getUseMark());
            laoczPumpPoint.setPointPrimaryKey(one.getId());
            laoczPumpPoint.setPumpId(Long.parseLong(pumpImportDtoMap.get(pumpImportDto.getPumpNumber())));
            pumpPoints.add(laoczPumpPoint);
        }

        return iLaoczPumpPointService.saveBatch(pumpPoints);
    }

    private void check(List<PumpImportDto> pumpImportDtos) {
        List<String> errList = new ArrayList<>();
        // 校验数据
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
        }
        if (CollectionUtil.isNotEmpty(errList)) {
            throw new ServiceException(StrUtil.join("\n", errList));
        }
    }

}
