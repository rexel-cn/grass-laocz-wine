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
import com.rexel.laocz.domain.dto.*;
import com.rexel.laocz.domain.vo.*;
import com.rexel.laocz.mapper.LaoczPotteryAltarManagementMapper;
import com.rexel.laocz.service.*;
import com.rexel.laocz.utils.TinciPdfUtils;
import com.rexel.laocz.utils.TinciQrCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional(rollbackFor = Exception.class)
    public boolean removeWithReal(Long potteryAltarId) {
        // 陶坛不在实时使用中才可以删除
        Integer count = iLaoczBatchPotteryMappingService.lambdaQuery().eq(LaoczBatchPotteryMapping::getPotteryAltarId, potteryAltarId).count();
        if (count > 0) {
            throw new ServiceException("陶坛在使用中，禁止删除");
        } else {
            LaoczPotteryAltarManagement laoczPotteryAltarManagement = this.getById(potteryAltarId);
            //删除陶坛管理数据
            boolean flag = this.removeById(potteryAltarId);
            //删除二维码图片
            if (StringUtils.isNotEmpty(laoczPotteryAltarManagement.getPotteryAltarQrCodeAddress())) {
                qrCodeUtils.deleteQrcode(laoczPotteryAltarManagement.getPotteryAltarQrCodeAddress());
            }
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
        if (CollectionUtil.isEmpty(list)){
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
