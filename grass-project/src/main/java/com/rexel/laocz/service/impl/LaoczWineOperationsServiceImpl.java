package com.rexel.laocz.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.bpm.enums.BpmTaskStatusEnum;
import com.rexel.common.exception.CustomException;
import com.rexel.common.utils.DictUtils;
import com.rexel.common.utils.PageUtils;
import com.rexel.laocz.constant.WineDictConstants;
import com.rexel.laocz.domain.*;
import com.rexel.laocz.domain.dto.MatterDetailDTO;
import com.rexel.laocz.domain.dto.WineEntryApplyParamDTO;
import com.rexel.laocz.domain.dto.WineOperationDTO;
import com.rexel.laocz.domain.vo.MatterDetailVO;
import com.rexel.laocz.domain.vo.MatterVO;
import com.rexel.laocz.domain.vo.WineDetailVO;
import com.rexel.laocz.enums.OperationTypeEnum;
import com.rexel.laocz.enums.WineBusyStatusEnum;
import com.rexel.laocz.enums.WineDetailTypeEnum;
import com.rexel.laocz.mapper.LaoczWineDetailsMapper;
import com.rexel.laocz.mapper.LaoczWineOperationsMapper;
import com.rexel.laocz.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 酒操作业务Service业务层处理
 *
 * @author grass-service
 * @date 2024-03-12
 */
@Service
public class LaoczWineOperationsServiceImpl extends ServiceImpl<LaoczWineOperationsMapper, LaoczWineOperations> implements ILaoczWineOperationsService {
    @Autowired
    private ILaoczWineDetailsService iLaoczWineDetailsService;
    @Autowired
    private LaoczWineDetailsMapper laoczWineDetailsMapper;
    @Autowired
    private ILaoczPumpService iLaoczPumpService;
    @Autowired
    private ILaoczFireZoneInfoService iLaoczFireZoneInfoService;
    @Autowired
    private ILaoczWeighingTankService iLaoczWeighingTankService;
    @Autowired
    private WineEntryApplyService wineEntryApplyService;
    @Autowired
    private WineOutService wineOutService;
    @Autowired
    private WinePourPotService winePourPotService;
    @Autowired
    private WineSampService wineSampService;

    /**
     * 获取我的事项
     *
     * @param wineOperationDTO 酒操作业务表
     * @return 我的事项列表
     */
    @Override
    public List<MatterVO> getMatterVOList(WineOperationDTO wineOperationDTO) {
        //判断时间
        if (wineOperationDTO.getBeginTime() != null && wineOperationDTO.getEndTime() != null) {
            if (wineOperationDTO.getBeginTime().after(wineOperationDTO.getEndTime())) {
                throw new CustomException("开始时间不能大于结束时间");
            }
        }
        List<LaoczWineOperations> list = baseMapper.selectLaoczWineOperationsList(wineOperationDTO);
        return list.stream().map(laoczWineOperations -> {
            MatterVO matterVO = new MatterVO();
            matterVO.setWineOperationsId(laoczWineOperations.getWineOperationsId());
            matterVO.setWorkOrderId(laoczWineOperations.getWorkOrderId());
            matterVO.setApplyTime(laoczWineOperations.getCreateTime());
            matterVO.setOperationType(OperationTypeEnum.getNameByValue(laoczWineOperations.getOperationType()));
            matterVO.setOperationTypeNumber(laoczWineOperations.getOperationType());
            matterVO.setApprovalResult(BpmTaskStatusEnum.getName(laoczWineOperations.getApprovalResults()));
            return matterVO;
        }).collect(Collectors.toList());
    }

    /**
     * 获取我的事项详情
     *
     * @param wineOperationsId 酒操作业务表 主键
     * @return 我的事项详情列表
     */
    @Override
    public List<MatterDetailVO> getMatterDetailVOList(MatterDetailDTO wineOperationsId) {
        LaoczWineOperations operations = getById(wineOperationsId.getWineOperationsId());
        if (operations == null) {
            throw new CustomException("事项详情不存在，请刷新页面重试");
        }
        String busyId = operations.getBusyId();
        List<MatterDetailVO> matterDetailVOS;
        try {
            PageUtils.startPage();
            matterDetailVOS = laoczWineDetailsMapper.selectMatterDetailVOList(busyId, wineOperationsId.getAreaId(), wineOperationsId.getFireZoneId());
        } finally {
            PageUtils.clearPage();
        }
        for (MatterDetailVO matterDetailVO : matterDetailVOS) {
            matterDetailVO.setDetailType(DictUtils.getDictLabel(WineDictConstants.DETAIL_TYPE, matterDetailVO.getDetailType()));
        }
        return matterDetailVOS;
    }

    /**
     * 根据事项和陶坛编号获取对应事项详情
     *
     * @param wineOperationsId   事项id
     * @param potteryAltarNumber 陶坛编号
     * @return 事项详情
     */
    @Override
    public MatterDetailVO qrCodeScanMatterDetail(Long wineOperationsId, String potteryAltarNumber) {
        LaoczWineOperations operations = getById(wineOperationsId);
        if (operations == null) {
            throw new CustomException("事项详情不存在，请刷新页面重试");
        }
        String busyId = operations.getBusyId();
        if (StrUtil.isEmpty(busyId)) {
            throw new CustomException("系统异常，请联系管理员");
        }
        MatterDetailVO matterDetailVO = laoczWineDetailsMapper.selectMatterDetailByBusyIdAndpotteryAltarNumber(busyId, potteryAltarNumber);
        if (matterDetailVO == null) {
            throw new CustomException("当前事项下，找不到陶坛编号为:{}的事项任务", potteryAltarNumber);
        }

        //如果是倒坛则肯定有倒坛入和倒坛出，那么如果是倒坛入并且数量还是2的情况下就报错，因为需要先倒坛出才能倒坛入
        if (OperationTypeEnum.POUR_POT.getValue().equals(operations.getOperationType())) {
            Integer count = lambdaQuery().eq(LaoczWineOperations::getBusyId, busyId).count();
            if (WineDetailTypeEnum.POUR_IN.getCode().toString().equals(matterDetailVO.getDetailType()) && count > 2) {
                throw new CustomException("倒坛需要先扫描出酒的陶坛罐");
            }
        }
        matterDetailVO.setDetailType(DictUtils.getDictLabel(WineDictConstants.DETAIL_TYPE, matterDetailVO.getDetailType()));
        return matterDetailVO;
    }

    /**
     * 获取入酒详情
     *
     * @param wineDetailsId 酒业务操作详情id
     * @return 入酒详情
     */
    @Override
    public WineDetailVO getWineDetailVO(Long wineDetailsId) {
        WineDetailVO wineDetailVO = laoczWineDetailsMapper.selectWineDetailVOById(wineDetailsId);
        buildSamplingPurpose(wineDetailVO);
        return wineDetailVO;
    }

    /**
     * 取样用途
     *
     * @param wineDetailVO 酒详情
     */
    private void buildSamplingPurpose(WineDetailVO wineDetailVO) {
        if (wineDetailVO == null) {
            return;
        }
        String samplingPurpose = wineDetailVO.getSamplingPurpose();
        if (StrUtil.isEmpty(samplingPurpose)) {
            return;
        }
        wineDetailVO.setSamplingPurpose(DictUtils.getDictLabel(WineDictConstants.SAMPLING_PURPOSE, samplingPurpose));
    }

    /**
     * 设置称重罐
     *
     * @param weighingTank 称重罐
     * @return 是否成功
     */
    @Override
    public Boolean setWeighingTank(WineEntryApplyParamDTO weighingTank) {
        LaoczWeighingTank tank = iLaoczWeighingTankService.getById(weighingTank.getWeighingTank());
        if (tank == null) {
            throw new CustomException("称重罐不存在");
        }
        LaoczWineDetails details = iLaoczWineDetailsService.lambdaQuery().eq(LaoczWineDetails::getWineDetailsId, weighingTank.getWineDetailsId()).one();
        if (!WineBusyStatusEnum.NOT_STARTED.getValue().equals(details.getBusyStatus())) {
            throw new CustomException("酒操作已开始，无法设置称重罐");
        }
        LaoczPump one = iLaoczPumpService.lambdaQuery().eq(LaoczPump::getFireZoneId, weighingTank.getFireZoneId()).one();
        if (one == null) {
            LaoczFireZoneInfo byId = iLaoczFireZoneInfoService.getById(weighingTank.getFireZoneId());
            throw new CustomException("请联系管理员在防火区:{}，设置泵信息", byId.getFireZoneName());
        }
        return iLaoczWineDetailsService.lambdaUpdate().eq(LaoczWineDetails::getWineDetailsId, weighingTank.getWineDetailsId())
                .set(LaoczWineDetails::getWeighingTank, weighingTank.getWeighingTank())
                .set(LaoczWineDetails::getPumpId, one.getPumpId())
                .update();
    }

    /**
     * 确认审批失败(审批失败确认后业务处理)
     * @param wineOperationsId 酒操作业务表 主键
     * @return 结果
     */
    @Override
    public Boolean confirmApprovalFailed(Long wineOperationsId) {
        LaoczWineOperations operations = getById(wineOperationsId);

        validateOperations(operations);

        if (!operations.getApprovalResults().equals(BpmTaskStatusEnum.REJECT.getStatus())) {
            throw new CustomException("该事项不是审批不通过状态,请刷新后重试");
        }
        //1：入酒，2出酒，3倒坛，4取样
        Long operationType = operations.getOperationType();
        OperationTypeEnum typeEnum = OperationTypeEnum.getByValue(operationType);
        switch (typeEnum) {
            case WINE_ENTRY:
                wineEntryApplyService.updateWineEntryStatus(operations.getBusyId());
                break;
            case WINE_OUT:
                wineOutService.updateWineOutStatus(operations.getBusyId());
                break;
            case POUR_POT:
                winePourPotService.updateWinePourStatus(operations.getBusyId());
                break;
            case SAMPLING:
                wineSampService.updateWineSampStatus(operations.getBusyId());
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + operationType);
        }
        return true;
    }

    /**
     * 确认审批状态
     *
     * @param busyId 业务id
     * @param status 状态
     * @return 是否成功
     */
    @Override
    public Boolean confirmApprovalStatus(String busyId, Integer status) {
        validateWineOperationsExistsByBusyId(busyId);
        return lambdaUpdate().eq(LaoczWineOperations::getBusyId, busyId).set(LaoczWineOperations::getApprovalResults, status).update();
    }

    /**
     * 根据业务id验证事项是否存在
     *
     * @param busyId 业务id
     */
    private void validateWineOperationsExistsByBusyId(String busyId) {
        LaoczWineOperations operations = lambdaQuery().eq(LaoczWineOperations::getBusyId, busyId).one();
        validateOperations(operations);
    }

    /**
     * 验证事项是否存在
     *
     * @param operations 事项
     */
    private void validateOperations(LaoczWineOperations operations) {
        if (ObjectUtil.isNull(operations)) {
            throw new CustomException("事项不存在，请刷新页面重试");
        }
    }
}
