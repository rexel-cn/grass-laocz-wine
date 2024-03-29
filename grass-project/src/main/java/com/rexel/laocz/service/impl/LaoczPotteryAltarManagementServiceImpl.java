package com.rexel.laocz.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.common.exception.ServiceException;
import com.rexel.common.utils.DateUtils;
import com.rexel.laocz.domain.*;
import com.rexel.laocz.domain.dto.WineEntryPotteryAltarDTO;
import com.rexel.laocz.domain.dto.WineOutPotteryAltarDTO;
import com.rexel.laocz.domain.dto.WinePourPotteryAltarDTO;
import com.rexel.laocz.domain.dto.WineSamplePotteryAltarDTO;
import com.rexel.laocz.domain.vo.*;
import com.rexel.laocz.mapper.LaoczPotteryAltarManagementMapper;
import com.rexel.laocz.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
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

    @Autowired
    private ILaoczBatchPotteryMappingService iLaoczBatchPotteryMappingService;

    @Autowired
    private ILaoczLiquorManagementService iLaoczLiquorManagementService;


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
            CurrentWineIndustryVO currentWineIndustryVO1 = new CurrentWineIndustryVO();
            currentWineIndustryVO1.setLiquorName("--");
            currentWineIndustryVO1.setLiquorBatchId("--");
            currentWineIndustryVO1.setActualWeight(0L);
            currentWineIndustryVO1.setStorageDuration("--");
            currentWineIndustryVO1.setLiquorLevel("--");
            currentWineIndustryVO1.setLiquorRound("--");
            currentWineIndustryVO1.setLiquorFlavorName("--");
            currentWineIndustryVO1.setLiquorSource("--");
            currentWineIndustryVO1.setLiquorYear("--");
            currentWineIndustryVO1.setLiquorContent("--");
            return currentWineIndustryVO1;
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
     * @param laoczPotteryAltarManagement 陶坛信息
     * @return 陶坛回显信息
     */
    @Override
    public List<PotteryAltarVo> selectLaoczPotteryAltarManagementListDetail(LaoczPotteryAltarManagement laoczPotteryAltarManagement) {
        List<PotteryAltarVo> list = baseMapper.selectLaoczPotteryAltarManagementListDetail(laoczPotteryAltarManagement);
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
     * @param laoczPotteryAltarManagement 陶坛信息
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
     * @param laoczPotteryAltarManagement 陶坛信息
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
     * 删除陶坛管理
     *
     * @param potteryAltarId 陶坛Id
     * @return 返回标识
     */
    @Override
    public boolean removeWithReal(Long potteryAltarId) {
        // 陶坛不在实时使用中才可以删除
        Integer count = iLaoczBatchPotteryMappingService.lambdaQuery().eq(LaoczBatchPotteryMapping::getPotteryAltarId, potteryAltarId).count();
        if (count > 0) {
            throw new ServiceException("陶坛在使用中，禁止删除");
        } else {
            return this.removeById(potteryAltarId);
        }
    }

    /**
     * 入酒时，陶坛列表：
     * 1：查询条件如下：
     * 1：防火区id 可选
     * 2：陶坛编号 可选
     * 3：已选陶坛 可选  过滤用
     * 4: 陶坛状态 必须是使用状态
     * 5：陶坛没有酒
     * 2:返回参数如下：
     * 1：陶坛管理主键id （用来入酒时带入，选择的那个陶坛）
     * 2：陶坛管理编号 用来显示
     * 3：区域名称
     * 4：防火区名称
     * 5: 满坛重量
     *
     * @param wineEntryPotteryAltarDTO 入酒，陶坛筛选DTO
     */
    @Override
    public List<WineOperaPotteryAltarVO> wineEntryPotteryAltarList(WineEntryPotteryAltarDTO wineEntryPotteryAltarDTO) {
        List<WineOperaPotteryAltarVO> wineOperaPotteryAltarVOS = baseMapper.wineEntryPotteryAltarList(wineEntryPotteryAltarDTO);
        if (CollectionUtil.isNotEmpty(wineEntryPotteryAltarDTO.getPotteryAltarIds())) {
            List<Long> potteryAltarIds = wineEntryPotteryAltarDTO.getPotteryAltarIds();
            wineOperaPotteryAltarVOS = wineOperaPotteryAltarVOS.stream()
                    .filter(wineOperaPotteryAltarVO ->
                            !potteryAltarIds.contains(wineOperaPotteryAltarVO.getPotteryAltarId()))
                    .collect(Collectors.toList());
        }
        return wineOperaPotteryAltarVOS;
    }

    /**
     * 出酒时，陶坛列表
     * 1：查询条件如下：
     * 1：酒液批次id（可以根据酒液批次查询在酒的信息）  必须是有酒并且存储   可选
     * 2：防火区id 可选
     * 3：陶坛编号 可选 过滤用
     * 4：已选陶坛 可选
     * 5：陶坛状态 必须是使用状态
     * 6：陶坛有酒
     * 7：陶坛目前没有进行其他任务，目前是存储状态
     * 2:返回参数如下：
     * 1：陶坛管理主键id （用来出酒时带入，选择的那个陶坛）
     * 2：陶坛管理编号 用来显示
     * 3：区域名称
     * 4：防火区名称
     * 5: 酒液重量
     * 6: 存储时长
     * 7: 酒品相关信息
     */
    @Override
    public List<WineOperaPotteryAltarVO> wineOutPotteryAltarList(WineOutPotteryAltarDTO wineOutPotteryAltarDTO) {
        List<WineOperaPotteryAltarVO> wineOperaPotteryAltarVOS = baseMapper.wineOutPotteryAltarList(wineOutPotteryAltarDTO);

        //存储时长
        for (WineOperaPotteryAltarVO potteryAltarVo : wineOperaPotteryAltarVOS) {
            if (potteryAltarVo.getStoringTime() != null) {
                potteryAltarVo.setStorageTime(DateUtils.daysBetween(potteryAltarVo.getStoringTime(), new Date()));
            }
        }


        //酒品相关
        List<Long> liquorManagementIds = wineOperaPotteryAltarVOS.stream().map(WineOperaPotteryAltarVO::getLiquorManagementId).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(liquorManagementIds)) {
            List<LaoczLiquorManagement> managements = iLaoczLiquorManagementService.lambdaQuery().in(LaoczLiquorManagement::getLiquorManagementId, liquorManagementIds).list();
            Map<Long, LaoczLiquorManagement> managementMap = managements.stream().collect(Collectors.toMap(LaoczLiquorManagement::getLiquorManagementId, Function.identity()));
            for (WineOperaPotteryAltarVO potteryAltarVo : wineOperaPotteryAltarVOS) {
                LaoczLiquorManagement laoczLiquorManagement = managementMap.get(potteryAltarVo.getLiquorManagementId());
                if (laoczLiquorManagement != null) {
                    potteryAltarVo.setLaoczLiquorManagement(laoczLiquorManagement);
                }
            }
        }
        return wineOperaPotteryAltarVOS;
    }

    /**
     * 取样时，陶坛列表
     * 1：查询条件如下：
     * 1：防火区id
     * 2：陶坛编号
     * 3：陶坛状态 必须是使用状态
     * 4：陶坛有酒
     * 5：陶坛目前没有进行其他任务，目前是存储状态
     * <p>
     * 2:返回参数如下：
     * 1:  陶坛管理主键id （用来取样时带入，选择的那个陶坛）
     * 2:  陶坛管理编号 用来显示
     * 3:  区域名称
     * 4:  防火区名称
     * 5:  酒液重量
     * 6: 满坛重量
     * 7:  酒品相关信息 id就行，点击才会查询
     *
     * @param WineSamplePotteryAltarDTO 取样，陶坛筛选DTO
     */
    @Override
    public List<WineOperaPotteryAltarVO> wineSamplePotteryAltarList(WineSamplePotteryAltarDTO WineSamplePotteryAltarDTO) {
        return baseMapper.wineSamplePotteryAltarList(WineSamplePotteryAltarDTO);
    }

    /**
     * 倒坛时，陶坛列表
     * 1：倒坛出，陶坛列表查询条件如下：
     * 1：防火区id
     * 2：陶坛编号
     * 3：陶坛状态 必须是使用状态
     * 4：陶坛有酒
     * 5：陶坛目前没有进行其他任务，目前是存储状态
     * 2：返回参数如下：
     * 1：酒批次id
     * 2：陶坛管理主键id （用来倒坛时带入，选择的那个陶坛）
     * 3：陶坛管理编号 用来显示
     * 4：区域名称
     * 5：防火区名称
     * 6：酒液重量
     * 7: 满坛重量
     * 8：存储时长
     * 9：酒品相关信息
     * 3：倒坛入，陶坛列表查询条件如下： 空陶坛或者同一批次的有酒的陶坛
     * 1：酒批次id(以倒坛出的酒返回的查询)
     * 2：防火区id
     * 3：陶坛编号
     * 4：陶坛状态 必须是使用状态
     * 5：陶坛没有酒或者同一个批次有酒的陶坛
     * 6：陶坛如果有酒，那么酒液重量必须小于等于倒坛出的酒液重量
     * 7：陶坛如果有酒目前没有进行其他任务，目前是存储状态
     * <p>
     * 4:返回参数如下：
     * 1:  陶坛管理主键id （用来倒坛时带入，选择的那个陶坛）
     * 2:  陶坛管理编号 用来显示
     * 3:  区域名称
     * 4:  防火区名称
     * 5:  满坛重量
     * 6:  酒液重量
     *
     * @param winePourPotteryAltarDTO
     */
    @Override
    public List<WineOperaPotteryAltarVO> winePourPotteryAltarList(WinePourPotteryAltarDTO winePourPotteryAltarDTO) {
        return null;
    }
}
