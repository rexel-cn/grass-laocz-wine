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
import com.rexel.laocz.domain.vo.LaoczWeighingTankPointInfo;
import com.rexel.laocz.domain.vo.PointInfo;
import com.rexel.laocz.domain.vo.WeighingTankAddVo;
import com.rexel.laocz.domain.vo.WeighingTankVo;
import com.rexel.laocz.mapper.LaoczWeighingTankMapper;
import com.rexel.laocz.service.*;
import com.rexel.system.domain.GrassPointInfo;
import com.rexel.system.domain.vo.PointQueryVO;
import com.rexel.system.mapper.GrassPointInfoMapper;
import com.rexel.system.service.IGrassPointService;
import com.rexel.system.service.ISysDictTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Autowired
    private GrassPointInfoMapper pointInfoMapper;

    @Autowired
    private ILaoczPumpPointService iLaoczPumpPointService;

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
        WeighingTankAddDto weighingTankAddDto = new WeighingTankAddDto();
        //查询使用标识的名字
        List<WeighingTankAddVo> weightMark = this.getAddVo("weight_mark");
        //封装
        LaoczWeighingTank laoczWeighingTank = this.getById(weighingTankId);
        if(ObjectUtil.isNull(laoczWeighingTank)){
            return weighingTankAddDto;
        }

        Long fireZoneId = laoczWeighingTank.getFireZoneId();

        LaoczFireZoneInfo laoczFireZoneInfo = iLaoczFireZoneInfoService.getById(fireZoneId);

        LaoczAreaInfo laoczAreaInfo = iLaoczAreaInfoService.getById(laoczFireZoneInfo.getAreaId());


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

            for (WeighingTankAddVo tankAddVo : weightMark) {
                if (tankAddVo.getUseMark().equals(item.getUseMark())) {
                    weighingTankAddVo.setName(tankAddVo.getName());
                }
            }

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
        if (Long.parseLong(weighingTankAddDto.getFullTankUpperLimit()) <= 0){
            throw new ServiceException("满罐上限值必须大于0");
        }
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
        if (Long.parseLong(weighingTankAddDto.getFullTankUpperLimit()) <= 0){
            throw new ServiceException("满罐上限值必须大于0");
        }

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
                //判断测点Id是否以及被占用，如果被占用，新增失败
                Integer countId = iLaoczWeighingTankPointService.lambdaQuery().eq(LaoczWeighingTankPoint::getPointPrimaryKey, weighingTankAddVo.getPointPrimaryKey()).count();
                if (countId > 0) {
                    //查询使用标识的名字
                    List<WeighingTankAddVo> weightMark = this.getAddVo("weight_mark");
                    for (WeighingTankAddVo tankAddVo : weightMark) {
                        if (tankAddVo.getUseMark().equals(weighingTankAddVo.getUseMark())) {
                            throw new ServiceException(tankAddVo.getName() + "的绑定测点已经被使用，请重新选择正确测点进行新增");
                        }
                    }
                }
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

        List<LaoczWeighingTankPoint> laoczWeighingTankPoints = new ArrayList<>();
        if (CollectionUtil.isEmpty(weighingTankDtos)) {
            throw new ServiceException("导入数据为空");
        }

        // 数据验证
        check(weighingTankDtos);
        Map<String, String> weighingTankDtoMap = new HashMap<>(weighingTankDtos.size());
        // 验证称重罐编号是否已存在
        for (WeighingTankDto weighingTankVo : weighingTankDtos) {
            if (Long.parseLong(weighingTankVo.getFullTankUpperLimit()) <= 0){
                throw new ServiceException("满罐上限值必须大于0");
            }
            if (!weighingTankDtoMap.containsKey(weighingTankVo.getWeighingTankNumber())){
                QueryWrapper<LaoczWeighingTank> queryWrapper = new QueryWrapper<>();

                queryWrapper.eq("weighing_tank_number", weighingTankVo.getWeighingTankNumber());

                int count = this.count(queryWrapper);

                if (count > 0) {
                    throw new ServiceException("称重罐编号已存在");
                }
                //获取防火区Id
                Long fireZoneId = iLaoczFireZoneInfoService.findFireZoneId(weighingTankVo.getAreaName(), weighingTankVo.getFireZoneName());
                weighingTankVo.setFireZoneId(fireZoneId);

                LaoczWeighingTank laoczWeighingTank = new LaoczWeighingTank();
                // 数据拷贝
                BeanUtil.copyProperties(weighingTankVo, laoczWeighingTank);
                this.save(laoczWeighingTank);
                weighingTankDtoMap.put(weighingTankVo.getWeighingTankNumber(),laoczWeighingTank.getWeighingTankId().toString());
            }
            //封装称重罐相关测点维护数据
            LaoczWeighingTankPoint laoczWeighingTankPoint = new LaoczWeighingTankPoint();

            //根据测点pointId获取主键Id
            QueryWrapper<GrassPointInfo> wrapper = new QueryWrapper<>();
            wrapper.eq("device_id", weighingTankVo.getDeviceId())
                    .eq("point_type", weighingTankVo.getPointType())
                    .eq("point_id", weighingTankVo.getPointId());
            GrassPointInfo one = iGrassPointService.getOne(wrapper);
            //判断pointId是否已经被占用
            Integer countId1 = iLaoczPumpPointService.lambdaQuery().eq(LaoczPumpPoint::getPointPrimaryKey, one.getId()).count();
            if (countId1 > 0) {
                throw new ServiceException(one.getPointId() + "测点已经被使用，请重新选择正确测点进行新增");
            }
            Integer countId2 = iLaoczWeighingTankPointService.lambdaQuery().eq(LaoczWeighingTankPoint::getPointPrimaryKey, one.getId()).count();
            if (countId2 > 0) {
                throw new ServiceException(one.getPointId() + "测点已经被使用，请重新选择正确测点进行新增");
            }
            laoczWeighingTankPoint.setUseMark(weighingTankVo.getUseMark());
            laoczWeighingTankPoint.setPointPrimaryKey(one.getId());
            laoczWeighingTankPoint.setWeighingTankId(Long.parseLong(weighingTankDtoMap.get(weighingTankVo.getWeighingTankNumber())));
            laoczWeighingTankPoints.add(laoczWeighingTankPoint);
        }
        return iLaoczWeighingTankPointService.saveBatch(laoczWeighingTankPoints);
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
        List<PointInfo> pointInfos = baseMapper.getPointInfo(weighingTankId);
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
        boolean remove = this.removeById(weighingTankId);
        //删除与该称重罐相关测点
        QueryWrapper<LaoczWeighingTankPoint> wrapper = new QueryWrapper<>();
        wrapper.eq("weighing_tank_id", weighingTankId);
        iLaoczWeighingTankPointService.remove(wrapper);
        return remove;
    }

    /**
     * 分页查询过滤已被选择测点
     *
     * @param
     * @return
     */
    @Override
    public List<PointQueryVO> getListPageNoChoice(String deviceId, String pointId, String pointName, String pointPrimaryKey) {
        //获取全部分页数据已经选择的测点过滤，自己不过滤
        List<PointQueryVO> list = pointInfoMapper.getFilterList(deviceId, pointId, pointName, pointPrimaryKey);
        return list;
    }

    /**
     * @param pointPrimaryKeys
     * @return
     */
    @Override
    public List<LaoczWeighingTankPointInfo> selectLaoczWeighingTankPointInfoByPointPrimaryKeys(List<Long> pointPrimaryKeys) {
        return baseMapper.selectLaoczWeighingTankPointInfoByPointPrimaryKeys(pointPrimaryKeys);
    }

    private void check(List<WeighingTankDto> weighingTankDtos) {
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
            if (StrUtil.isEmpty(weighingTankVo.getUseMark())) {
                errList.add("第" + (i + 2) + "行使用标识为空");
            }
            if (StrUtil.isEmpty(weighingTankVo.getPointId())) {
                errList.add("第" + (i + 2) + "行测点为空");
            }
            if (StrUtil.isEmpty(weighingTankVo.getAbout())) {
                errList.add("第" + (i + 2) + "行罐位置为空");
            }
            if (Long.parseLong(weighingTankVo.getFullTankUpperLimit()) <= 0){
                errList.add("满罐重量不能小于等于0");
            }
        }
        if (CollectionUtil.isNotEmpty(errList)) {
            throw new ServiceException(StrUtil.join("\n", errList));
        }
    }

}
