package com.rexel.laocz.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.common.exception.ServiceException;
import com.rexel.laocz.domain.LaoczAreaInfo;
import com.rexel.laocz.domain.LaoczFireZoneInfo;
import com.rexel.laocz.domain.LaoczWeighingTank;
import com.rexel.laocz.domain.dto.WeighingTankDto;
import com.rexel.laocz.domain.vo.WeighingTankVo;
import com.rexel.laocz.mapper.LaoczWeighingTankMapper;
import com.rexel.laocz.service.ILaoczAreaInfoService;
import com.rexel.laocz.service.ILaoczFireZoneInfoService;
import com.rexel.laocz.service.ILaoczWeighingTankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
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

        List<LaoczWeighingTank> laoczWeighingTanks = this.selectLaoczWeighingTankList(laoczWeighingTank);

        List<WeighingTankVo> tankVos = laoczWeighingTanks.stream().map((item) -> {
            WeighingTankVo weighingTankVo = new WeighingTankVo();

            Long fireZoneId = item.getFireZoneId();

            LaoczFireZoneInfo laoczFireZoneInfo = iLaoczFireZoneInfoService.getById(fireZoneId);

            LaoczAreaInfo laoczAreaInfo = iLaoczAreaInfoService.getById(laoczFireZoneInfo.getAreaId());

            BeanUtil.copyProperties(item, weighingTankVo);

            weighingTankVo.setAreaName(laoczAreaInfo.getAreaName());
            weighingTankVo.setFireZoneName(laoczFireZoneInfo.getFireZoneName());
            weighingTankVo.setAreaId(laoczAreaInfo.getAreaId());


            return weighingTankVo;

        }).collect(Collectors.toList());

        return tankVos;
    }
    /**
     * 获取称重罐管理详细信息
     */
    @Override
    public WeighingTankVo getByIdWithTank(Long weighingTankId) {
        LaoczWeighingTank laoczWeighingTank = this.getById(weighingTankId);

        WeighingTankVo weighingTankVo = new WeighingTankVo();

        Long fireZoneId = laoczWeighingTank.getFireZoneId();

        LaoczFireZoneInfo laoczFireZoneInfo = iLaoczFireZoneInfoService.getById(fireZoneId);

        LaoczAreaInfo laoczAreaInfo = iLaoczAreaInfoService.getById(laoczFireZoneInfo.getAreaId());

        BeanUtil.copyProperties(laoczWeighingTank, weighingTankVo);

        weighingTankVo.setAreaName(laoczAreaInfo.getAreaName());
        weighingTankVo.setFireZoneName(laoczFireZoneInfo.getFireZoneName());

        return weighingTankVo;
    }
    /**
     * 修改称重罐管理
     */
    @Override
    public boolean updateByIdWithWeighingTank(LaoczWeighingTank laoczWeighingTank) {
        QueryWrapper<LaoczWeighingTank> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("weighing_tank_number", laoczWeighingTank.getWeighingTankNumber());

        int count = this.count(queryWrapper);

        LaoczWeighingTank weighingTank = this.getById(laoczWeighingTank.getWeighingTankId());

        if (count > 0 && !laoczWeighingTank.getWeighingTankNumber().equals(weighingTank.getWeighingTankNumber())) {
            throw new ServiceException("称重罐编号已存在");
        } else {
            return updateById(laoczWeighingTank);
        }
    }
    /**
     * 新增称重罐管理
     */
    @Override
    public boolean addWeighingTank(LaoczWeighingTank laoczWeighingTank) {
        QueryWrapper<LaoczWeighingTank> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("weighing_tank_number", laoczWeighingTank.getWeighingTankNumber());

        int count = this.count(queryWrapper);

        if (count > 0) {
            throw new ServiceException("称重罐编号已存在");
        } else {
            return save(laoczWeighingTank);
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
            areaWrapper.eq("area_name",weighingTankVo.getAreaName());
            LaoczAreaInfo laoczAreaInfo = iLaoczAreaInfoService.getOne(areaWrapper);
            // 获取防火区Id
            if (laoczAreaInfo != null){
                QueryWrapper<LaoczFireZoneInfo> zoneInfoQueryWrapper = new QueryWrapper<>();
                zoneInfoQueryWrapper.eq("area_id",laoczAreaInfo.getAreaId())
                        .eq("fire_zone_name",weighingTankVo.getFireZoneName());
                LaoczFireZoneInfo fireZoneInfo = iLaoczFireZoneInfoService.getOne(zoneInfoQueryWrapper);
                weighingTankVo.setFireZoneId(fireZoneInfo.getFireZoneId());
            }

            LaoczWeighingTank laoczWeighingTank = new LaoczWeighingTank();
            // 数据拷贝
            BeanUtil.copyProperties(weighingTankVo,laoczWeighingTank);
            laoczWeighingTanks.add(laoczWeighingTank);
        }
        return saveBatch(laoczWeighingTanks);
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
