package com.rexel.laocz.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.common.core.domain.entity.SysDictData;
import com.rexel.common.exception.ServiceException;
import com.rexel.laocz.domain.*;
import com.rexel.laocz.domain.dto.WeighingTankAddDto;
import com.rexel.laocz.domain.dto.WeighingTankDto;
import com.rexel.laocz.domain.vo.PointInfo;
import com.rexel.laocz.domain.vo.WeighingTankAddVo;
import com.rexel.laocz.domain.vo.WeighingTankVo;
import com.rexel.laocz.mapper.LaoczWeighingTankMapper;
import com.rexel.laocz.service.*;
import com.rexel.system.domain.GrassPointInfo;
import com.rexel.system.service.IGrassPointService;
import com.rexel.system.service.ISysDictDataService;
import com.rexel.system.service.ISysDictTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 称重罐管理Service业务层处理
 *
 * @author grass-service
 * @date 2024-03-06
 */
@Service
public class LaoczWeighingTankServiceImpl extends ServiceImpl<LaoczWeighingTankMapper, LaoczWeighingTank> implements ILaoczWeighingTankService {

    @Autowired
    private ILaoczFireZoneInfoService iLaoczFireZoneInfoService;

    @Autowired
    private ILaoczAreaInfoService iLaoczAreaInfoService;

    @Autowired
    private ISysDictTypeService iSysDictTypeService;

    @Autowired
    private ILaoczWeighingTankPointService iLaoczWeighingTankPointService;

    @Autowired
    private ILaoczWineDetailsService iLaoczWineDetailsService;

    @Autowired
    private IGrassPointService iGrassPointService;
    /**
     * 查询称重罐管理列表
     *
     * @param laoczWeighingTank 称重罐管理
     * @return 称重罐管理
     */
    @Override
    public List<LaoczWeighingTank> selectLaoczWeighingTankList(LaoczWeighingTank laoczWeighingTank) {
        return baseMapper.selectLaoczWeighingTankList(laoczWeighingTank);
    }

    /**
     * 查询称重罐管理列表详细信息
     */
    @Override
    public List<WeighingTankVo> selectLaoczWeighingTankListDetail(LaoczWeighingTank laoczWeighingTank) {
        // 查询称重罐列表详细信息
        List<WeighingTankVo> tankVos = baseMapper.selectLaoczWeighingTankListDetail(laoczWeighingTank);

        return tankVos;
    }

    /**
     * 获取称重罐管理详细信息
     */
    @Override
    public WeighingTankAddDto getByIdWithTank(Long weighingTankId) {
        LaoczWeighingTank laoczWeighingTank = this.getById(weighingTankId);

        Long fireZoneId = laoczWeighingTank.getFireZoneId();

        LaoczFireZoneInfo laoczFireZoneInfo = iLaoczFireZoneInfoService.getById(fireZoneId);

        LaoczAreaInfo laoczAreaInfo = iLaoczAreaInfoService.getById(laoczFireZoneInfo.getAreaId());

        WeighingTankAddDto weighingTankAddDto = new WeighingTankAddDto();
        BeanUtil.copyProperties(laoczWeighingTank, weighingTankAddDto);
        weighingTankAddDto.setAreaId(laoczAreaInfo.getAreaId());
        weighingTankAddDto.setFireZoneId(laoczFireZoneInfo.getFireZoneId());

        // 查询称重罐相关测点维护表
        List<LaoczWeighingTankPoint> list = iLaoczWeighingTankPointService
                .lambdaQuery()
                .eq(LaoczWeighingTankPoint::getWeighingTankId, weighingTankId)
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

            return weighingTankAddVo;
        }).collect(Collectors.toList());

        weighingTankAddDto.setWeighingTankAddVos(addVoList);

        return weighingTankAddDto;
    }

    /**
     * 修改称重罐管理
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateByIdWithWeighingTank(WeighingTankAddDto weighingTankAddDto) {
        QueryWrapper<LaoczWeighingTank> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("weighing_tank_number", weighingTankAddDto.getWeighingTankNumber());

        int count = this.count(queryWrapper);

        LaoczWeighingTank weighingTank = this.getById(weighingTankAddDto.getWeighingTankId());

        if (count > 0 && !weighingTankAddDto.getWeighingTankNumber().equals(weighingTank.getWeighingTankNumber())) {
            throw new ServiceException("称重罐编号已存在");
        } else {
            // 称重罐管理更新
            LaoczWeighingTank laoczWeighingTank = new LaoczWeighingTank();
            BeanUtil.copyProperties(weighingTankAddDto, laoczWeighingTank);
            updateById(laoczWeighingTank);


            List<LaoczWeighingTankPoint> tankPoints = weighingTankAddDto.getWeighingTankAddVos().stream().map(
                    (item) -> {
                        LaoczWeighingTankPoint laoczWeighingTankPoint = new LaoczWeighingTankPoint();
                        BeanUtil.copyProperties(item, laoczWeighingTankPoint);
                        laoczWeighingTankPoint.setWeighingTankId(weighingTankAddDto.getWeighingTankId());
                        return laoczWeighingTankPoint;
                    }
            ).collect(Collectors.toList());

            //先将所有与该称重罐相关测点数据删除
            QueryWrapper<LaoczWeighingTankPoint> wrapper = new QueryWrapper<>();
            wrapper.eq("weighing_tank_id", weighingTankAddDto.getWeighingTankId());
            iLaoczWeighingTankPointService.remove(wrapper);

            return iLaoczWeighingTankPointService.saveBatch(tankPoints);
        }
    }

    /**
     * 新增称重罐管理
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addWeighingTank(WeighingTankAddDto weighingTankAddDto) {
        QueryWrapper<LaoczWeighingTank> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("weighing_tank_number", weighingTankAddDto.getWeighingTankNumber());

        int count = this.count(queryWrapper);

        if (count > 0) {
            throw new ServiceException("称重罐编号已存在");
        } else {
            // 称重罐管理插入
            LaoczWeighingTank laoczWeighingTank = new LaoczWeighingTank();
            BeanUtil.copyProperties(weighingTankAddDto, laoczWeighingTank);
            save(laoczWeighingTank);
            // 称重罐相关测点维护插入,获取称重罐Id
            List<LaoczWeighingTankPoint> laoczWeighingTankPoints = new ArrayList<>();

            Long weighingTankId = laoczWeighingTank.getWeighingTankId();
            List<WeighingTankAddVo> weighingTankAddVos = weighingTankAddDto.getWeighingTankAddVos();

            for (WeighingTankAddVo weighingTankAddVo : weighingTankAddVos) {
                LaoczWeighingTankPoint laoczWeighingTankPoint = new LaoczWeighingTankPoint();
                laoczWeighingTankPoint.setUseMark(weighingTankAddVo.getUseMark());
                laoczWeighingTankPoint.setWeighingTankId(weighingTankId);
                laoczWeighingTankPoint.setPointPrimaryKey(weighingTankAddVo.getPointPrimaryKey());

                laoczWeighingTankPoints.add(laoczWeighingTankPoint);
            }
            return iLaoczWeighingTankPointService.saveBatch(laoczWeighingTankPoints);
        }
    }

    /**
     * 导入称重罐管理列表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean importWeighingTank(List<WeighingTankDto> weighingTankDtos) {

        List<LaoczWeighingTank> laoczWeighingTanks = new ArrayList<>();
        if (CollectionUtil.isEmpty(weighingTankDtos)) {
            throw new ServiceException("导入数据为空");
        }

        // 数据验证
        check(weighingTankDtos);
        // 验证称重罐编号是否已存在
        for (WeighingTankDto weighingTankVo : weighingTankDtos) {

            QueryWrapper<LaoczWeighingTank> queryWrapper = new QueryWrapper<>();

            queryWrapper.eq("weighing_tank_number", weighingTankVo.getWeighingTankNumber());

            int count = this.count(queryWrapper);

            if (count > 0) {
                throw new ServiceException("称重罐编号已存在");
            }
            // 获取归属区域Id
            QueryWrapper<LaoczAreaInfo> areaWrapper = new QueryWrapper<>();
            areaWrapper.eq("area_name", weighingTankVo.getAreaName());
            LaoczAreaInfo laoczAreaInfo = iLaoczAreaInfoService.getOne(areaWrapper);
            // 获取防火区Id
            if (laoczAreaInfo != null) {
                QueryWrapper<LaoczFireZoneInfo> zoneInfoQueryWrapper = new QueryWrapper<>();
                zoneInfoQueryWrapper.eq("area_id", laoczAreaInfo.getAreaId())
                        .eq("fire_zone_name", weighingTankVo.getFireZoneName());
                LaoczFireZoneInfo fireZoneInfo = iLaoczFireZoneInfoService.getOne(zoneInfoQueryWrapper);
                weighingTankVo.setFireZoneId(fireZoneInfo.getFireZoneId());
            }

            LaoczWeighingTank laoczWeighingTank = new LaoczWeighingTank();
            // 数据拷贝
            BeanUtil.copyProperties(weighingTankVo, laoczWeighingTank);
            laoczWeighingTanks.add(laoczWeighingTank);
        }
        return saveBatch(laoczWeighingTanks);
    }

    @Override
    public List<WeighingTankAddVo> getAddVo(String dictType) {

        // 查询字典表获取所有的useMark和name
        List<SysDictData> weightMark = iSysDictTypeService.selectDictDataByType(dictType);

        List<WeighingTankAddVo> weighingTankAddVos = weightMark.stream().map((item) -> {
            WeighingTankAddVo weighingTankAddVo = new WeighingTankAddVo();
            weighingTankAddVo.setName(item.getDictLabel());
            weighingTankAddVo.setUseMark(item.getDictValue());
            return weighingTankAddVo;
        }).collect(Collectors.toList());

        return weighingTankAddVos;
    }

    @Override
    public List<PointInfo> getPointInfo(Long weighingTankId) {
        // 获取所有的测点并根据测点获取测点信息
        List<PointInfo> pointInfos = baseMapper.getPointInfo();
        return pointInfos;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByIdWithPoint(Long weighingTankId) {
        //称重罐如果在使用中，禁止删除
        Integer count = iLaoczWineDetailsService.lambdaQuery().eq(LaoczWineDetails::getWeighingTank, weighingTankId).count();
        if (count > 0) {
            throw new ServiceException("称重罐操作中，禁止删除");
        }
        //删除称重罐管理数据
        this.removeById(weighingTankId);
        //删除与该称重罐相关测点
        QueryWrapper<LaoczWeighingTankPoint> wrapper = new QueryWrapper<>();
        wrapper.eq("weighing_tank_id", weighingTankId);
        return iLaoczWeighingTankPointService.remove(wrapper);
    }

    private void check(List<WeighingTankDto> weighingTankDtos) {
        /**
         * 校验数据
         *
         * @param liquorVos
         */
        List<String> errList = new ArrayList<>();
        // 校验数据
        for (int i = 0; i < weighingTankDtos.size(); i++) {
            WeighingTankDto weighingTankVo = weighingTankDtos.get(i);
            if (StrUtil.isEmpty(weighingTankVo.getWeighingTankNumber())) {
                errList.add("第" + (i + 2) + "行称重罐编号为空");
            }
            if (ObjectUtil.isNull(weighingTankVo.getAreaName())) {
                errList.add("第" + (i + 2) + "行归属区域为空");
            }
            if (StrUtil.isEmpty(weighingTankVo.getFireZoneName())) {
                errList.add("第" + (i + 2) + "行防火区为空");
            }
            if (StrUtil.isEmpty(weighingTankVo.getFullTankUpperLimit())) {
                errList.add("第" + (i + 2) + "行满罐上限为空");
            }
        }
        if (CollectionUtil.isNotEmpty(errList)) {
            throw new ServiceException(StrUtil.join("\n", errList));
        }
    }

}
