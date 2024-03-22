package com.rexel.laocz.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.common.exception.ServiceException;
import com.rexel.common.utils.DateUtils;
import com.rexel.laocz.domain.LaoczAreaInfo;
import com.rexel.laocz.domain.LaoczFireZoneInfo;
import com.rexel.laocz.domain.LaoczPotteryAltarManagement;
import com.rexel.laocz.domain.dto.WineEntryPotteryAltarDTO;
import com.rexel.laocz.domain.vo.CurrentWineIndustryVO;
import com.rexel.laocz.domain.vo.PotteryAltarInformationVO;
import com.rexel.laocz.domain.vo.PotteryAltarVo;
import com.rexel.laocz.domain.vo.PotteryPullDownFrameVO;
import com.rexel.laocz.mapper.LaoczPotteryAltarManagementMapper;
import com.rexel.laocz.service.ILaoczAreaInfoService;
import com.rexel.laocz.service.ILaoczFireZoneInfoService;
import com.rexel.laocz.service.ILaoczPotteryAltarManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 陶坛管理Service业务层处理
 *
 * @author grass-service
 * @date 2024-03-06
 */
@Service
public class LaoczPotteryAltarManagementServiceImpl extends ServiceImpl<LaoczPotteryAltarManagementMapper, LaoczPotteryAltarManagement> implements ILaoczPotteryAltarManagementService {

    @Autowired
    private ILaoczFireZoneInfoService iLaoczFireZoneInfoService;

    @Autowired
    private ILaoczAreaInfoService iLaoczAreaInfoService;

    /**
     * 查询陶坛管理列表
     *
     * @param laoczPotteryAltarManagement 陶坛管理
     * @return 陶坛管理
     */
    @Override
    public List<LaoczPotteryAltarManagement> selectLaoczPotteryAltarManagementList(LaoczPotteryAltarManagement laoczPotteryAltarManagement) {
        return baseMapper.selectLaoczPotteryAltarManagementList(laoczPotteryAltarManagement);
    }

    /**
     * 查询陶坛下拉框
     *
     * @param fireZoneId 防火区ID
     * @return
     */
    @Override
    public List<PotteryPullDownFrameVO> selectPotteryPullDownFrameList(Long fireZoneId) {
        List<LaoczPotteryAltarManagement> list = this.lambdaQuery()
                .eq(LaoczPotteryAltarManagement::getFireZoneId, fireZoneId)
                .list();
        if (CollectionUtil.isEmpty(list)) {
            return null;
        }
        return BeanUtil.copyToList(list, PotteryPullDownFrameVO.class);
    }

    /**
     * 获取陶坛信息
     *
     * @param potteryAltarId 主键ID
     * @return
     */
    @Override
    public PotteryAltarInformationVO selectPotteryAltarInformation(Long potteryAltarId) {
        PotteryAltarInformationVO potteryAltarInformationVO = baseMapper.setPotteryAltarInformation(potteryAltarId);
        if (ObjectUtil.isEmpty(potteryAltarInformationVO)) {
            PotteryAltarInformationVO potteryAltarInformationVO1 = new PotteryAltarInformationVO();
            return potteryAltarInformationVO1;
        }
        return potteryAltarInformationVO;
    }

    @Override
    public CurrentWineIndustryVO selectCurrentWineIndustry(Long potteryAltarId) throws ParseException {
        // 定义一个与字符串日期格式相匹配的SimpleDateFormat对象
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        CurrentWineIndustryVO currentWineIndustryVO = baseMapper.setCurrentWineIndustry(potteryAltarId);
        if (ObjectUtil.isEmpty(currentWineIndustryVO)) {
            return Optional.ofNullable(currentWineIndustryVO).orElseGet(CurrentWineIndustryVO::new);
        }
        String date = DateUtils.getTime();
        Date storageDuration = currentWineIndustryVO.getStoringTime();
        Date date1 = formatter.parse(date);
        String datePoor = DateUtils.getDatePoor(date1, storageDuration);
        currentWineIndustryVO.setStorageDuration(datePoor);
        return currentWineIndustryVO;
    }
    /**
     * 查询陶坛管理列表详细信息
     *
     * @param laoczPotteryAltarManagement
     * @return
     */
    @Override
    public List<PotteryAltarVo> selectLaoczPotteryAltarManagementListDetail(LaoczPotteryAltarManagement laoczPotteryAltarManagement) {
        List<LaoczPotteryAltarManagement> laoczPotteryAltarManagements = baseMapper.selectLaoczPotteryAltarManagementList(laoczPotteryAltarManagement);


        List<PotteryAltarVo> list = laoczPotteryAltarManagements.stream().map((item) -> {

            PotteryAltarVo potteryAltarVo = new PotteryAltarVo();

            Long fireZoneId = item.getFireZoneId();

            LaoczFireZoneInfo laoczFireZoneInfo = iLaoczFireZoneInfoService.getById(fireZoneId);

            LaoczAreaInfo laoczAreaInfo = iLaoczAreaInfoService.getById(laoczFireZoneInfo.getAreaId());

            BeanUtil.copyProperties(item, potteryAltarVo);

            potteryAltarVo.setAreaName(laoczAreaInfo.getAreaName());
            potteryAltarVo.setFireZoneName(laoczFireZoneInfo.getFireZoneName());

            return potteryAltarVo;
        }).collect(Collectors.toList());

        return list;
    }
    /**
     * 编辑回显,通过Id查询陶坛管理详情
     */
    @Override
    public PotteryAltarVo selectLaoczPotteryAltarManagement(Long potteryAltarId) {

        PotteryAltarVo potteryAltarVo = new PotteryAltarVo();

        LaoczPotteryAltarManagement laoczPotteryAltarManagement = this.getById(potteryAltarId);

        Long fireZoneId = laoczPotteryAltarManagement.getFireZoneId();

        LaoczFireZoneInfo laoczFireZoneInfo = iLaoczFireZoneInfoService.getById(fireZoneId);

        LaoczAreaInfo laoczAreaInfo = iLaoczAreaInfoService.getById(laoczFireZoneInfo.getAreaId());

        BeanUtil.copyProperties(laoczPotteryAltarManagement, potteryAltarVo);

        potteryAltarVo.setAreaName(laoczAreaInfo.getAreaName());
        potteryAltarVo.setFireZoneName(laoczFireZoneInfo.getFireZoneName());
        potteryAltarVo.setAreaId(laoczAreaInfo.getAreaId());

        return potteryAltarVo;
    }
    /**
     * 新增陶坛
     *
     * @param laoczPotteryAltarManagement
     * @return
     */
    @Override
    public boolean addPotteryAltar(LaoczPotteryAltarManagement laoczPotteryAltarManagement) {

        laoczPotteryAltarManagement.setPotteryAltarQrCodeAddress(" ");

        QueryWrapper<LaoczPotteryAltarManagement> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("pottery_altar_number", laoczPotteryAltarManagement.getPotteryAltarNumber());

        int count = this.count(queryWrapper);

        if (count > 0) {
            throw new ServiceException("陶坛编号已存在");
        } else {
            return this.save(laoczPotteryAltarManagement);
        }
    }
    /**
     * 修改陶坛
     *
     * @param laoczPotteryAltarManagement
     * @return
     */
    @Override
    public boolean updateByIdWithPotteryAltar(LaoczPotteryAltarManagement laoczPotteryAltarManagement) {

        QueryWrapper<LaoczPotteryAltarManagement> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("pottery_altar_number", laoczPotteryAltarManagement.getPotteryAltarNumber());

        int count = this.count(queryWrapper);

        LaoczPotteryAltarManagement altarManagement = this.getById(laoczPotteryAltarManagement.getPotteryAltarId());

        if (count > 0 && !altarManagement.getPotteryAltarNumber().equals(laoczPotteryAltarManagement.getPotteryAltarNumber())) {
            throw new ServiceException("陶坛编号已存在");
        } else {
            return updateById(laoczPotteryAltarManagement);
        }
    }

    /**
     * 入酒时，陶坛列表过滤查询
     *
     * @param wineEntryPotteryAltarDTO 入酒，陶坛筛选DTO
     * @return 陶坛列表
     */
    @Override
    public List<PotteryAltarVo> wineEntryPotteryAltarList(WineEntryPotteryAltarDTO wineEntryPotteryAltarDTO) {
        List<PotteryAltarVo> list = baseMapper.selectWineEntryPotteryAltarList(wineEntryPotteryAltarDTO);

        return list;
    }
}
