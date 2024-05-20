package com.rexel.laocz.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.common.core.domain.AjaxResult;
import com.rexel.common.exception.ServiceException;
import com.rexel.common.utils.DateUtils;
import com.rexel.common.utils.StringUtils;
import com.rexel.laocz.domain.*;
import com.rexel.laocz.domain.dto.WineEntryPotteryAltarDTO;
import com.rexel.laocz.domain.dto.WineOutPotteryAltarDTO;
import com.rexel.laocz.domain.dto.WinePourPotteryAltarDTO;
import com.rexel.laocz.domain.dto.WineSamplePotteryAltarDTO;
import com.rexel.laocz.domain.vo.*;
import com.rexel.laocz.enums.PotteryAltarStateEnum;
import com.rexel.laocz.mapper.LaoczPotteryAltarManagementMapper;
import com.rexel.laocz.service.*;
import com.rexel.laocz.utils.TinciPdfUtils;
import com.rexel.laocz.utils.TinciQrCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
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

    @Autowired
    private TinciQrCodeUtils qrCodeUtils;

    @Autowired
    private TinciPdfUtils pdfUtils;

    @Autowired
    private ILaoczWineDetailsService iLaoczWineDetailsService;

    @Autowired
    private ILaoczWineDetailsHisService iLaoczWineDetailsHisService;

    @Autowired
    private ILaoczLiquorBatchService iLaoczLiquorBatchService;

    @Autowired
    private ILaoczWineHistoryService laoczWineHistoryService;

    @Autowired
    private ILaoczWeighingTankService laoczWeighingTankService;


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
            return new PotteryAltarInformationVO();
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
        long differenceInDays = TimeUnit.DAYS.convert(
                date1.getTime() - storageDuration.getTime(),
                TimeUnit.MILLISECONDS
        );
        currentWineIndustryVO.setStorageDuration(String.valueOf(differenceInDays));
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
        return baseMapper.selectLaoczPotteryAltarManagementListDetail(laoczPotteryAltarManagement);
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
        if (laoczPotteryAltarManagement.getPotteryAltarFullAltarWeight() <= 0) {
            throw new ServiceException("满坛重量应大于0");
        }

        validatePotteryAltarNumber(laoczPotteryAltarManagement.getPotteryAltarNumber());


        laoczPotteryAltarManagement.setPotteryAltarQrCodeAddress(" ");

        QueryWrapper<LaoczPotteryAltarManagement> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("pottery_altar_number", laoczPotteryAltarManagement.getPotteryAltarNumber());

        int count = this.count(queryWrapper);

        if (count > 0) {
            throw new ServiceException("陶坛编号已存在");
        } else {
            //生成陶坛二维码
            String content = qrCodeUtils.makeQrCodeContent(
                    laoczPotteryAltarManagement.getPotteryAltarNumber());
            String url = qrCodeUtils.generateQrcode(
                    "taotan", laoczPotteryAltarManagement.getPotteryAltarNumber(), content);
            laoczPotteryAltarManagement.setPotteryAltarQrCodeAddress(url);
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
        if (laoczPotteryAltarManagement.getPotteryAltarFullAltarWeight() <= 0) {
            throw new ServiceException("满坛重量应大于0");
        }
        //validatePotteryAltarNumber(laoczPotteryAltarManagement.getPotteryAltarNumber());

//        QueryWrapper<LaoczPotteryAltarManagement> queryWrapper = new QueryWrapper<>();
//
//        queryWrapper.eq("pottery_altar_number", laoczPotteryAltarManagement.getPotteryAltarNumber());
//
//        int count = this.count(queryWrapper);
//
        LaoczPotteryAltarManagement altarManagement = this.getById(laoczPotteryAltarManagement.getPotteryAltarId());
//
//        if (count > 0 && !altarManagement.getPotteryAltarNumber().equals(laoczPotteryAltarManagement.getPotteryAltarNumber())) {
//            throw new ServiceException("陶坛编号已存在");
//        } else {
//
//        }
        //不允许修改
        laoczPotteryAltarManagement.setPotteryAltarNumber(altarManagement.getPotteryAltarNumber());
        return updateById(laoczPotteryAltarManagement);
    }

    /**
     * 验证陶坛编号 只能是数字并且是6位
     *
     * @param potteryAltarNumber 陶坛编号
     */
    private void validatePotteryAltarNumber(String potteryAltarNumber) {
        if (StrUtil.isEmpty(potteryAltarNumber)) {
            throw new ServiceException("陶坛编号不能为空");
        }
        if (!potteryAltarNumber.matches("^[0-9]*$")) {
            throw new ServiceException("陶坛编号只能是数字");
        }
        if (potteryAltarNumber.length() != 6) {
            throw new ServiceException("陶坛编号只能是6位");
        }
    }

    /**
     * 删除陶坛管理
     *
     * @param potteryAltarId 陶坛Id
     * @return 返回标识
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeWithReal(Long potteryAltarId) {
        // 陶坛不在实时使用中才可以删除
        Integer count = iLaoczBatchPotteryMappingService.lambdaQuery().eq(LaoczBatchPotteryMapping::getPotteryAltarId, potteryAltarId).count();
        if (count > 0) {
            throw new ServiceException("陶坛在使用中，禁止删除");
        } else {
            LaoczPotteryAltarManagement laoczPotteryAltarManagement = this.getById(potteryAltarId);
            if (laoczPotteryAltarManagement == null) {
                return false;
            }
            //删除陶坛管理数据
            boolean flag = this.removeById(potteryAltarId);
            //
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    //删除二维码图片
                    if (StringUtils.isNotEmpty(laoczPotteryAltarManagement.getPotteryAltarQrCodeAddress())) {
                        qrCodeUtils.deleteQrcode(laoczPotteryAltarManagement.getPotteryAltarQrCodeAddress());
                    }
                }
            });
            return flag;
        }
    }

    /**
     * 去除已选陶坛
     *
     * @param potteryAltarIds          已选陶坛
     * @param wineOperaPotteryAltarVOS 陶坛列表
     * @return 去除已选陶坛后的陶坛列表
     */
    private static List<WineOperaPotteryAltarVO> filterWinOperaPotteryAltar(List<Long> potteryAltarIds, List<WineOperaPotteryAltarVO> wineOperaPotteryAltarVOS) {
        if (CollectionUtil.isNotEmpty(potteryAltarIds)) {
            wineOperaPotteryAltarVOS = wineOperaPotteryAltarVOS.stream()
                    .filter(wineOperaPotteryAltarVO ->
                            !potteryAltarIds.contains(wineOperaPotteryAltarVO.getPotteryAltarId()))
                    .collect(Collectors.toList());
        }
        return wineOperaPotteryAltarVOS;
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
        wineOperaPotteryAltarVOS = filterWinOperaPotteryAltar(wineEntryPotteryAltarDTO.getPotteryAltarIds(), wineOperaPotteryAltarVOS);
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
        wineOperaPotteryAltarVOS = filterWinOperaPotteryAltar(wineOutPotteryAltarDTO.getPotteryAltarIds(), wineOperaPotteryAltarVOS);
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
     * @param winePourPotteryAltarDTO 倒坛，陶坛筛选DTO
     * @return 倒坛，陶坛列表
     */
    @Override
    public List<WineOperaPotteryAltarVO> winePourPotteryAltarList(WinePourPotteryAltarDTO winePourPotteryAltarDTO) {
        List<WineOperaPotteryAltarVO> wineOperaPotteryAltarVOS = new ArrayList<>();

        //如果不等于空，就是倒坛入酒
        boolean isPout = winePourPotteryAltarDTO.getPotteryAltarId() != null;

        //true 是倒坛入酒，false是倒坛出酒
        if (isPout) {
            LaoczBatchPotteryMapping one = iLaoczBatchPotteryMappingService.lambdaQuery().eq(LaoczBatchPotteryMapping::getPotteryAltarId, winePourPotteryAltarDTO.getPotteryAltarId()).one();

            //空罐子，满坛重量要大于等于申请重量
            WineEntryPotteryAltarDTO wineEntryPotteryAltarDTO = BeanUtil.copyProperties(winePourPotteryAltarDTO, WineEntryPotteryAltarDTO.class);
            List<WineOperaPotteryAltarVO> wineEntryPotteryAltarList = baseMapper.wineEntryPotteryAltarList(wineEntryPotteryAltarDTO);
            List<WineOperaPotteryAltarVO> list = wineEntryPotteryAltarList.stream().filter(wineOperaPotteryAltarVO -> wineOperaPotteryAltarVO.getPotteryAltarFullAltarWeight() >= winePourPotteryAltarDTO.getWineWeight()).collect(Collectors.toList());
            wineOperaPotteryAltarVOS.addAll(list);
            //有酒的罐子，必须是同一批次，并且剩余重量要大于等于申请重量
            WineOutPotteryAltarDTO wineOutPotteryAltarDTO = BeanUtil.copyProperties(winePourPotteryAltarDTO, WineOutPotteryAltarDTO.class);
            List<WineOperaPotteryAltarVO> wineOutPotteryAltarList = baseMapper.wineOutPotteryAltarList(wineOutPotteryAltarDTO);
            List<WineOperaPotteryAltarVO> list1 = wineOutPotteryAltarList.stream()
                    .filter(wineOperaPotteryAltarVO -> wineOperaPotteryAltarVO.getPotteryAltarFullAltarWeight() - wineOperaPotteryAltarVO.getActualWeight() >= winePourPotteryAltarDTO.getWineWeight())
                    .filter(wineOperaPotteryAltarVO -> wineOperaPotteryAltarVO.getLiquorBatchId().equals(one.getLiquorBatchId()))
                    .collect(Collectors.toList());
            wineOperaPotteryAltarVOS.addAll(list1);
        } else {
            WineOutPotteryAltarDTO wineOutPotteryAltarDTO = BeanUtil.copyProperties(winePourPotteryAltarDTO, WineOutPotteryAltarDTO.class);
            wineOperaPotteryAltarVOS = baseMapper.wineOutPotteryAltarList(wineOutPotteryAltarDTO);
        }
        wineOperaPotteryAltarVOS = filterWinOperaPotteryAltar(new ArrayList<>(Collections.singletonList(winePourPotteryAltarDTO.getPotteryAltarId())), wineOperaPotteryAltarVOS);
        return wineOperaPotteryAltarVOS;
    }

    @Override
    public AjaxResult getPotteryAltarManagementQrCodePdf() {
        // 查询陶坛列表
        List<LaoczPotteryAltarManagement> list = baseMapper.selectLaoczPotteryAltarManagementList(null);
        if (CollectionUtil.isEmpty(list)) {
            throw new ServiceException("没有二维码可以导出");
        }
        // 生成图片集合
        LinkedHashMap<String, String> idMap = new LinkedHashMap<>();
        list.forEach(m -> {
            if (StringUtils.isNotEmpty(m.getPotteryAltarQrCodeAddress())) {
                idMap.put(m.getPotteryAltarNumber(), m.getPotteryAltarQrCodeAddress());
            }
        });

        // 转换为PDF
        String pdfUrl = pdfUtils.convertToPdf(idMap, "陶坛二维码");
        if (StringUtils.isEmpty(pdfUrl)) {
            return AjaxResult.success();
        }

        return AjaxResult.success(pdfUrl);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean importPotteryAltar(List<PotteryAltarVo> potteryAltarVos) {
        //检查excel是否为空
        List<LaoczPotteryAltarManagement> laoczPotteryAltarManagements = new ArrayList<>();
        if (CollectionUtil.isEmpty(potteryAltarVos)) {
            throw new ServiceException("导入数据为空");
        }
        //数据校验
        check(potteryAltarVos);
        //校验陶坛编号是否有重复
        Set<String> potteryAltarNumberSet = new HashSet<>();
        for (PotteryAltarVo potteryAltarVo : potteryAltarVos) {
            if (!potteryAltarNumberSet.add(potteryAltarVo.getPotteryAltarNumber())) {
                throw new ServiceException("Excel中有重复的陶坛编号" + potteryAltarVo.getPotteryAltarNumber());
            }
        }
        //查询数据库中的所有陶坛数据
        Integer count1 = this.lambdaQuery().in(LaoczPotteryAltarManagement::getPotteryAltarNumber, potteryAltarNumberSet).count();
        if (count1 > 0) {
            throw new ServiceException("陶坛编号已存在,请删除后再导入");
        }
        //查询所有的场区名称、防火区名称、防火区id
        List<FireZoneInfoVo> areaFires = iLaoczAreaInfoService.getAreaFire();


        //导入
        for (PotteryAltarVo potteryAltarVo : potteryAltarVos) {
            if (!PotteryAltarStateEnum.USE.getCode().toString().equals(potteryAltarVo.getPotteryAltarState())
                    && !PotteryAltarStateEnum.SEAL.getCode().toString().equals(potteryAltarVo.getPotteryAltarState())) {
                throw new ServiceException("陶坛状态值必须为1或2");
            }
            if (potteryAltarVo.getPotteryAltarFullAltarWeight() <= 0) {
                throw new ServiceException("满坛重量应大于0");
            }
            // 获取防火区Id
            for (FireZoneInfoVo areaFire : areaFires) {
                String excelInfo = potteryAltarVo.getAreaName() + potteryAltarVo.getFireZoneName();
                String dataInfo = areaFire.getAreaName() + areaFire.getFireZoneName();
                if (excelInfo.equals(dataInfo)) {
                    potteryAltarVo.setFireZoneId(areaFire.getFireZoneId());
                }
            }
            LaoczPotteryAltarManagement laoczPotteryAltarManagement = new LaoczPotteryAltarManagement();
            // 数据拷贝
            BeanUtil.copyProperties(potteryAltarVo, laoczPotteryAltarManagement);
            //生成二维码
            String content = qrCodeUtils.makeQrCodeContent(
                    laoczPotteryAltarManagement.getPotteryAltarNumber());
            String url = qrCodeUtils.generateQrcode(
                    "taotan", laoczPotteryAltarManagement.getPotteryAltarNumber(), content);
            laoczPotteryAltarManagement.setPotteryAltarQrCodeAddress(url);
            laoczPotteryAltarManagements.add(laoczPotteryAltarManagement);
        }
        return saveBatch(laoczPotteryAltarManagements);
    }

    @Override
    public WaitPotteryVO getPotteryByWorkOrderId(String workOrderId) {
        WaitPotteryVO waitPotteryVO = new WaitPotteryVO();

        // 获取工单申请重量
        QueryWrapper<LaoczLiquorBatch> wrapper = new QueryWrapper<>();
        wrapper.eq("work_order_id", workOrderId);
        LaoczLiquorBatch one = iLaoczLiquorBatchService.getOne(wrapper);
        if (one == null) {
            return new WaitPotteryVO();
        }

        // 酒液批次
        waitPotteryVO.setLiquorBatchId(one.getLiquorBatchId());
        // 申请的重量
        waitPotteryVO.setApplyWeight(one.getApplyWeight());

        // 酒品信息
        LaoczLiquorManagement liquorManagement = iLaoczLiquorManagementService.getById(one.getLiquorManagementId());
        waitPotteryVO.setLaoczLiquorManagement(liquorManagement);

        // 根据工单Id在操作业务详情实时表中查询所有的陶坛Id
        QueryWrapper<LaoczWineDetails> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("work_order_id", workOrderId);
        // 拿到该工单下所有陶坛Id
        List<LaoczWineDetails> laoczWineDetails = iLaoczWineDetailsService.list(queryWrapper);
        // 酒批次Id查询酒品信息
        List<WaitPotteryAltarVO> list = laoczWineDetails.stream().map((item) -> {
            //创建陶坛对象
            WaitPotteryAltarVO waitPotteryAltarVO = new WaitPotteryAltarVO();
            // 申请重量
            waitPotteryAltarVO.setPotteryAltarApplyWeight(item.getPotteryAltarApplyWeight());
            // 陶坛信息
            PotteryAltarInformationVO potteryAltarInformationVO = this.selectPotteryAltarInformation(item.getPotteryAltarId());
            BeanUtil.copyProperties(potteryAltarInformationVO, waitPotteryAltarVO);
            waitPotteryAltarVO.setCreateBy("---");
            waitPotteryAltarVO.setOperationTime(null);
            waitPotteryAltarVO.setWeighingTankNumber("---");
            return waitPotteryAltarVO;
        }).collect(Collectors.toList());

        waitPotteryVO.setWaitPotteryAltarVOS(list);
        return waitPotteryVO;
    }

    @Override
    public List<WaitPotteryVO> getOutPotteryByWorkOrderId(String workOrderId,String detailType) {

        List<WaitPotteryVO> waitPotteryVOS = new ArrayList<>();

        // 根据工单Id在操作业务详情实时表中查询所有的陶坛Id,出酒时，里面有多个酒品批次，每个酒品信息对应多个陶坛
        QueryWrapper<LaoczWineDetails> queryWrapper = new QueryWrapper<>();
        if (detailType.isEmpty()){
            queryWrapper.eq("work_order_id", workOrderId);
        }else {
            queryWrapper.eq("work_order_id", workOrderId).eq("detail_type",detailType);
        }
        // 拿到该工单下所有陶坛Id
        List<LaoczWineDetails> laoczWineDetails = iLaoczWineDetailsService.list(queryWrapper);
        if (CollectionUtil.isNotEmpty(laoczWineDetails)) {
            // 酒批次Id查询陶坛信息
            List<WaitPotteryAltarVO> list = laoczWineDetails.stream().map((item) -> {
                // 创建陶坛对象
                WaitPotteryAltarVO waitPotteryAltarVO = new WaitPotteryAltarVO();
                // 申请重量
                waitPotteryAltarVO.setPotteryAltarApplyWeight(item.getPotteryAltarApplyWeight());
                // 陶坛信息
                LaoczPotteryAltarManagement laoczPotteryAltarManagement = this.getById(item.getPotteryAltarId());
                OverviewVo overview = iLaoczBatchPotteryMappingService.getOverview(laoczPotteryAltarManagement.getPotteryAltarNumber());
                // 获取陶坛状态
                PotteryAltarInformationVO potteryAltarInformationVO = this.selectPotteryAltarInformation(item.getPotteryAltarId());
                BeanUtil.copyProperties(overview, waitPotteryAltarVO);
                waitPotteryAltarVO.setPotteryAltarState(potteryAltarInformationVO.getPotteryAltarState());
                waitPotteryAltarVO.setCreateBy("---");
                waitPotteryAltarVO.setOperationTime(null);
                waitPotteryAltarVO.setWeighingTankNumber("---");
                return waitPotteryAltarVO;
            }).collect(Collectors.toList());
            // 相同酒品信息的在一组
            Map<String, List<WaitPotteryAltarVO>> collect = list.stream().collect(Collectors.groupingBy(WaitPotteryAltarVO::getLiquorBatchId));

            for (Map.Entry<String, List<WaitPotteryAltarVO>> longListEntry : collect.entrySet()) {
                WaitPotteryVO waitPotteryVO = new WaitPotteryVO();
                // 陶坛信息
                waitPotteryVO.setWaitPotteryAltarVOS(longListEntry.getValue());
                // 酒液批次Id
                waitPotteryVO.setLiquorBatchId(longListEntry.getKey());
                // 酒品信息
                QueryWrapper<LaoczLiquorBatch> wrapper = new QueryWrapper<>();
                wrapper.eq("liquor_batch_id", longListEntry.getKey());
                LaoczLiquorBatch one = iLaoczLiquorBatchService.getOne(wrapper);
                LaoczLiquorManagement liquorManagement = iLaoczLiquorManagementService.getById(one.getLiquorManagementId());
                waitPotteryVO.setLaoczLiquorManagement(liquorManagement);
                waitPotteryVOS.add(waitPotteryVO);
            }
        }
        return waitPotteryVOS;
    }

    @Override
    public WaitPotteryVO getFinishPotteryByWorkOrderId(String workOrderId) {

        WaitPotteryVO waitPotteryVO = this.getPotteryByWorkOrderId(workOrderId);
        QueryWrapper<LaoczWineDetailsHis> hisWrapper = new QueryWrapper<>();
        hisWrapper.eq("work_order_id", workOrderId);
        List<LaoczWineDetailsHis> laoczWineDetailsHis = iLaoczWineDetailsHisService.list(hisWrapper);
        // 拿到历史表下所有的陶坛Id
        List<WaitPotteryAltarVO> collect = laoczWineDetailsHis.stream().map((item) -> {
            // 创建陶坛对象
            WaitPotteryAltarVO waitPotteryAltarVO = new WaitPotteryAltarVO();
            // 申请重量
            waitPotteryAltarVO.setPotteryAltarApplyWeight(item.getPotteryAltarApplyWeight());
            // 陶坛Id
            Long potteryAltarId = item.getPotteryAltarId();
            //称重罐编号
            if (item.getWeighingTank()!= null){
                LaoczWeighingTank weighingTank = laoczWeighingTankService.getById(item.getWeighingTank());
                waitPotteryAltarVO.setWeighingTankNumber(weighingTank.getWeighingTankNumber());
            }
            // 酒液重量
            QueryWrapper<LaoczBatchPotteryMapping> wrapper = new QueryWrapper<>();
            wrapper.eq("pottery_altar_id",potteryAltarId).eq("liquor_batch_id",waitPotteryVO.getLiquorBatchId());
            LaoczBatchPotteryMapping potteryMapping = iLaoczBatchPotteryMappingService.getOne(wrapper);
            // 可能倒坛后，该陶坛就不存在该批次的酒了
            if (potteryMapping != null){
                waitPotteryAltarVO.setActualWeight(potteryMapping.getActualWeight());
            }else {
                waitPotteryAltarVO.setActualWeight(0.0);
            }
            // 封装参数查询陶坛信息
            LaoczWineHistory laoczWineHistory = new LaoczWineHistory();
            laoczWineHistory.setWorkOrderId(workOrderId);
            laoczWineHistory.setPotteryAltarId(potteryAltarId);
            List<LaoczWineHistory> laoczWineHistories = laoczWineHistoryService.selectDetailByWorkId(laoczWineHistory);
            LaoczWineHistory wineHistory = new LaoczWineHistory();
            if (CollectionUtil.isNotEmpty(laoczWineHistories)) {
                wineHistory = laoczWineHistories.get(0);
            }
            // 获取陶坛状态
            PotteryAltarInformationVO potteryAltarInformationVO = this.selectPotteryAltarInformation(item.getPotteryAltarId());
            BeanUtil.copyProperties(potteryAltarInformationVO, waitPotteryAltarVO);
            BeanUtil.copyProperties(wineHistory, waitPotteryAltarVO);
            return waitPotteryAltarVO;
        }).collect(Collectors.toList());

        waitPotteryVO.getWaitPotteryAltarVOS().addAll(collect);

        return waitPotteryVO;
    }

    @Override
    public List<WaitPotteryVO> getFinishOutPotteryByWorkOrderId(String workOrderId,String detailType) {
        List<WaitPotteryVO> outPottery = this.getOutPotteryByWorkOrderId(workOrderId,detailType);
        if (CollectionUtil.isNotEmpty(outPottery)) {
            for (WaitPotteryVO waitPotteryVO : outPottery) {
                QueryWrapper<LaoczWineDetailsHis> hisWrapper = new QueryWrapper<>();
                if (detailType.isEmpty()){
                    hisWrapper.eq("work_order_id", workOrderId);
                }else {
                    hisWrapper.eq("work_order_id", workOrderId).eq("detail_type",detailType);
                }
                List<LaoczWineDetailsHis> laoczWineDetailsHis = iLaoczWineDetailsHisService.list(hisWrapper);
                // 拿到历史表下所有的陶坛Id
                List<WaitPotteryAltarVO> collect = laoczWineDetailsHis.stream().map((item) -> {
                    // 创建陶坛对象
                    WaitPotteryAltarVO waitPotteryAltarVO = new WaitPotteryAltarVO();
                    //开始之前重量
                    waitPotteryAltarVO.setBeforeWeight(item.getBeforeWeight());
                    //结束之后重量
                    waitPotteryAltarVO.setAfterWeight(item.getAfterWeight());
                    //操作时间
                    waitPotteryAltarVO.setOperationTime(item.getOperationTime());
                    //称重罐编号
                    if (item.getWeighingTank()!= null){
                        LaoczWeighingTank weighingTank = laoczWeighingTankService.getById(item.getWeighingTank());
                        waitPotteryAltarVO.setWeighingTankNumber(weighingTank.getWeighingTankNumber());
                     }
                    // 申请重量
                    waitPotteryAltarVO.setPotteryAltarApplyWeight(item.getPotteryAltarApplyWeight());
                    // 陶坛Id
                    Long potteryAltarId = item.getPotteryAltarId();
                    // 陶坛信息
                    LaoczPotteryAltarManagement laoczPotteryAltarManagement = this.getById(potteryAltarId);
                    OverviewVo overview = iLaoczBatchPotteryMappingService.getOverview(laoczPotteryAltarManagement.getPotteryAltarNumber());
                    // 获取陶坛状态
                    PotteryAltarInformationVO potteryAltarInformationVO = this.selectPotteryAltarInformation(potteryAltarId);
                    BeanUtil.copyProperties(overview, waitPotteryAltarVO);
                    waitPotteryAltarVO.setPotteryAltarState(potteryAltarInformationVO.getPotteryAltarState());
                    return waitPotteryAltarVO;
                }).collect(Collectors.toList());
                waitPotteryVO.getWaitPotteryAltarVOS().addAll(collect);
            }
        } else {
            List<WaitPotteryVO> waitPotteryVOS = new ArrayList<>();
            QueryWrapper<LaoczWineDetailsHis> hisWrapper = new QueryWrapper<>();
            if (detailType.isEmpty()){
                hisWrapper.eq("work_order_id", workOrderId);
            }else {
                hisWrapper.eq("work_order_id", workOrderId).eq("detail_type",detailType);
            }
            List<LaoczWineDetailsHis> laoczWineDetailsHis = iLaoczWineDetailsHisService.list(hisWrapper);
            // 拿到历史表下所有的陶坛Id
            List<WaitPotteryAltarVO> list = laoczWineDetailsHis.stream().map((item) -> {
                // 创建陶坛对象
                WaitPotteryAltarVO waitPotteryAltarVO = new WaitPotteryAltarVO();
                //开始之前重量
                waitPotteryAltarVO.setBeforeWeight(item.getBeforeWeight());
                //结束之后重量
                waitPotteryAltarVO.setAfterWeight(item.getAfterWeight());
                //操作时间
                waitPotteryAltarVO.setOperationTime(item.getOperationTime());

                //称重罐编号
                if (item.getWeighingTank()!= null){
                    LaoczWeighingTank weighingTank = laoczWeighingTankService.getById(item.getWeighingTank());
                    waitPotteryAltarVO.setWeighingTankNumber(weighingTank.getWeighingTankNumber());
                }
                // 申请重量
                waitPotteryAltarVO.setPotteryAltarApplyWeight(item.getPotteryAltarApplyWeight());
                // 陶坛Id
                Long potteryAltarId = item.getPotteryAltarId();
                // 陶坛信息
                LaoczPotteryAltarManagement laoczPotteryAltarManagement = this.getById(potteryAltarId);
                OverviewVo overview = iLaoczBatchPotteryMappingService.getOverview(laoczPotteryAltarManagement.getPotteryAltarNumber());
                // 获取陶坛状态
                PotteryAltarInformationVO potteryAltarInformationVO = this.selectPotteryAltarInformation(potteryAltarId);
                BeanUtil.copyProperties(overview, waitPotteryAltarVO);
                //todo 虎卫注意 已经出酒的陶坛可能存在于laocz_batch_pottery_mapping，也可能不存在，取决于是否出完酒。所以OverviewVo查询有问题，导致批号查询有问题
                waitPotteryAltarVO.setLiquorBatchId(item.getLiquorBatchId());

                waitPotteryAltarVO.setPotteryAltarState(potteryAltarInformationVO.getPotteryAltarState());
                return waitPotteryAltarVO;
            }).collect(Collectors.toList());
            // 相同酒品信息的在一组
            Map<String, List<WaitPotteryAltarVO>> collect = list.stream().collect(Collectors.groupingBy(WaitPotteryAltarVO::getLiquorBatchId));

            for (Map.Entry<String, List<WaitPotteryAltarVO>> longListEntry : collect.entrySet()) {
                WaitPotteryVO waitPotteryVO = new WaitPotteryVO();
                // 陶坛信息
                waitPotteryVO.setWaitPotteryAltarVOS(longListEntry.getValue());
                // 酒液批次Id
                waitPotteryVO.setLiquorBatchId(longListEntry.getKey());
                // 酒品信息
                QueryWrapper<LaoczLiquorBatch> wrapper = new QueryWrapper<>();
                wrapper.eq("liquor_batch_id", longListEntry.getKey());
                LaoczLiquorBatch one = iLaoczLiquorBatchService.getOne(wrapper);
                LaoczLiquorManagement liquorManagement = iLaoczLiquorManagementService.getById(one.getLiquorManagementId());
                waitPotteryVO.setLaoczLiquorManagement(liquorManagement);
                waitPotteryVOS.add(waitPotteryVO);
            }
            outPottery = waitPotteryVOS;
        }
        return outPottery;
    }

    private void check(List<PotteryAltarVo> potteryAltarVos) {
        List<String> errList = new ArrayList<>();
        // 校验数据
        for (int i = 0; i < potteryAltarVos.size(); i++) {
            PotteryAltarVo potteryAltarVo = potteryAltarVos.get(i);
            if (StrUtil.isEmpty(potteryAltarVo.getPotteryAltarNumber())) {
                errList.add("第" + (i + 2) + "行陶坛编号为空");
            }
            if (ObjectUtil.isNull(potteryAltarVo.getAreaName())) {
                errList.add("第" + (i + 2) + "行归属区域为空");
            }
            if (StrUtil.isEmpty(potteryAltarVo.getFireZoneName())) {
                errList.add("第" + (i + 2) + "行防火区为空");
            }
            if (StrUtil.isEmpty(potteryAltarVo.getPotteryAltarState())) {
                errList.add("第" + (i + 2) + "行陶坛状态为空");
            }
            if (StrUtil.isEmpty(potteryAltarVo.getPotteryAltarFullAltarWeight().toString())) {
                errList.add("第" + (i + 2) + "行满坛重量为空");
            }
        }
        if (CollectionUtil.isNotEmpty(errList)) {
            throw new ServiceException(StrUtil.join("\n", errList));
        }
    }
}
