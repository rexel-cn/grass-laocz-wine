package com.rexel.laocz.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.common.exception.ServiceException;
import com.rexel.laocz.domain.*;
import com.rexel.laocz.domain.dto.PumpAddDto;
import com.rexel.laocz.domain.vo.LaoczPumpVo;
import com.rexel.laocz.domain.vo.PointInfo;
import com.rexel.laocz.domain.vo.WeighingTankAddVo;
import com.rexel.laocz.mapper.LaoczPumpMapper;
import com.rexel.laocz.service.*;
import com.rexel.system.service.ISysDictTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
        List<PointInfo>  pointInfos = baseMapper.getPointInfo();
        return pointInfos;
    }

}
