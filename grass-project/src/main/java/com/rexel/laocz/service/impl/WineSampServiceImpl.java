package com.rexel.laocz.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.rexel.bpm.enums.BpmTaskStatusEnum;
import com.rexel.common.exception.CustomException;
import com.rexel.laocz.domain.LaoczBatchPotteryMapping;
import com.rexel.laocz.domain.LaoczWineDetails;
import com.rexel.laocz.domain.dto.WineSampApplyDTO;
import com.rexel.laocz.domain.dto.WineSamplePotteryAltarDTO;
import com.rexel.laocz.domain.vo.WineOperaPotteryAltarVO;
import com.rexel.laocz.enums.OperationTypeEnum;
import com.rexel.laocz.enums.RealStatusEnum;
import com.rexel.laocz.enums.WineDetailTypeEnum;
import com.rexel.laocz.enums.WineProcessDefinitionKeyEnum;
import com.rexel.laocz.service.WineAbstract;
import com.rexel.laocz.service.WineSampService;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName WineSampServiceImpl
 * @Description
 * @Author 孟开通
 * @Date 2024/3/19 18:18
 **/
@Service
public class WineSampServiceImpl extends WineAbstract implements WineSampService {
    /**
     * 二维码扫描获取入酒陶坛信息
     *
     * @param potteryAltarNumber 陶坛编号
     * @return 陶坛信息
     */
    @Override
    public WineOperaPotteryAltarVO qrCodeScan(String potteryAltarNumber) {
        //陶坛验证，是否存在，是否被封存， 实时表 验证，是否被使用
        super.potteryAltarNumberCheck(potteryAltarNumber, true);
        WineSamplePotteryAltarDTO wineSamplePotteryAltarDTO = new WineSamplePotteryAltarDTO();
        wineSamplePotteryAltarDTO.setEqPotteryAltarNumber(potteryAltarNumber);
        List<WineOperaPotteryAltarVO> wineOperaPotteryAltarVOS = iLaoczPotteryAltarManagementService.wineSamplePotteryAltarList(wineSamplePotteryAltarDTO);
        if (wineOperaPotteryAltarVOS.isEmpty()) {
            throw new CustomException("系统异常，请联系管理员");
        }
        return wineOperaPotteryAltarVOS.get(0);
    }

    /**
     * 取样审批结束后处理，审批通过或不通过
     *
     * @param busyId 业务id
     */
    @Override
    public void updateWineSampStatus(String busyId) {
        //验证审核是否通过
        super.approvalCheck(busyId, BpmTaskStatusEnum.REJECT);
        List<LaoczWineDetails> list  = super.getDetailsInBusyId(busyId);
        //如果审批不通过
        // 陶坛与批次实时关系表修改为存储状态
        if (CollectionUtil.isNotEmpty(list)) {
            List<Long> potteryAltarIds = list.stream().map(LaoczWineDetails::getPotteryAltarId).collect(Collectors.toList());
            //修改陶坛与批次实时关系表
            iLaoczBatchPotteryMappingService.lambdaUpdate().in(LaoczBatchPotteryMapping::getPotteryAltarId, potteryAltarIds)
                    .set(LaoczBatchPotteryMapping::getRealStatus, RealStatusEnum.STORAGE.getCode()).update();
            //备份酒操作业务表
            super.backupWineDetails(list);
        }
        //laocz_wine_operations备份到his，然后删除
        taskVerify(busyId);

        //备份所有数据到laocz_wine_history，并标记为不通过
        rejectSaveHistory(busyId);
    }

    /**
     * 酒取样申请
     *
     * @param wineSampApplyDTO 酒取样申请
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void wineSampApply(WineSampApplyDTO wineSampApplyDTO) {
        //查询 使用中，有酒，必须是存储状态
        //二维码扫描:有没有这个罐子，是否在使用中，必须有酒，必须是存储状态
        //手动输入：有没有这个罐子，是否在使用中，必须有酒,必须是存储状态，申请重量必须小于等于实际重量

        LaoczBatchPotteryMapping laoczBatchPotteryMapping = check(wineSampApplyDTO);

        //新增酒操作业务表
        //新增酒操作业务详情
        //生成busy_id
        String busyId = super.getBusyId();
        Collection<String> values = iLaoczLiquorBatchService.getLiquorManagementNameMap(Collections.singletonList(laoczBatchPotteryMapping.getLiquorBatchId())).values();
        String liquorNames = String.join(",", values);
        //新增流程实例
        String processInstanceId = saveProcessInstancesService(busyId
                , super.getVariables(laoczBatchPotteryMapping.getLiquorBatchId(), OperationTypeEnum.SAMPLING.getValue(),liquorNames)
                , WineProcessDefinitionKeyEnum.POUR_TANK);
        //新增laocz_wine_operations
        saveLaoczWineOperations(busyId, processInstanceId, OperationTypeEnum.SAMPLING);
        //新增laocz_wine_details
        saveLaoczWineDetails(wineSampApplyDTO, busyId, processInstanceId, laoczBatchPotteryMapping);

        //更新陶坛实时关系表，占用
        LaoczBatchPotteryMapping mapping = new LaoczBatchPotteryMapping();
        mapping.setLiquorBatchId(laoczBatchPotteryMapping.getLiquorBatchId());
        mapping.setRealStatus(RealStatusEnum.OCCUPY.getCode());
        super.updatePotteryMappingState(mapping);
    }

    /**
     * 验证陶坛是否可以取样
     *
     * @param wineSampApplyDTO 酒取样申请
     * @return 陶坛实时关系表
     */
    @NotNull
    private LaoczBatchPotteryMapping check(WineSampApplyDTO wineSampApplyDTO) {
        //根据陶坛id查询实时表，查询陶坛批次号
        LaoczBatchPotteryMapping laoczBatchPotteryMapping = iLaoczBatchPotteryMappingService.lambdaQuery().eq(LaoczBatchPotteryMapping::getPotteryAltarId, wineSampApplyDTO.getPotteryAltarId()).one();
        //验证是否存在，以及是否是存储状态
        if (laoczBatchPotteryMapping == null) {
            throw new CustomException("陶坛罐没有酒不允许取样");
        }
        if (!RealStatusEnum.STORAGE.getCode().equals(laoczBatchPotteryMapping.getRealStatus())) {
            throw new CustomException("陶坛罐还未存储不允许取样");
        }
        if (wineSampApplyDTO.getSamplingWeight() > laoczBatchPotteryMapping.getActualWeight()) {
            throw new CustomException("实际重量已不满足取样重量");
        }
        return laoczBatchPotteryMapping;
    }

    /**
     * 新增酒操作业务表
     *
     * @param wineSampApplyDTO         酒取样申请
     * @param busyId                   业务id
     * @param workId                   工单id
     * @param laoczBatchPotteryMapping 陶坛实时关系表
     */
    private void saveLaoczWineDetails(WineSampApplyDTO wineSampApplyDTO, String busyId, String workId, LaoczBatchPotteryMapping laoczBatchPotteryMapping) {
        LaoczWineDetails laoczWineDetails = buildLaoczWineDetails(busyId, workId, laoczBatchPotteryMapping.getLiquorBatchId(), wineSampApplyDTO.getPotteryAltarId(), wineSampApplyDTO.getSamplingWeight(), WineDetailTypeEnum.SAMPLING, wineSampApplyDTO.getSampPurpose());
        //因取样特殊，取样重量就是陶坛申请重量，也是称重罐重量,后续查询"陶坛查询历史操作页面用到"，操作重量就是称重罐重量
        Double potteryAltarApplyWeight = laoczWineDetails.getPotteryAltarApplyWeight();
        laoczWineDetails.setWeighingTankWeight(potteryAltarApplyWeight);
        iLaoczWineDetailsService.save(laoczWineDetails);
    }

    /**
     * 酒取样完成
     *
     * @param wineDetailsId 酒操作业务详情id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void wineSampFinish(Long wineDetailsId) {
        LaoczWineDetails laoczWineDetails = iLaoczWineDetailsService.getById(wineDetailsId);
        //验证审核是否通过
        super.approvalCheck(laoczWineDetails.getBusyId(), BpmTaskStatusEnum.APPROVE);
        //更新陶坛实时关系表，入酒，更新为存储，更新实际重量（为称重罐的实际重量）
        super.updatePotteryMappingState(laoczWineDetails.getPotteryAltarId(), "-",
                laoczWineDetails.getPotteryAltarApplyWeight());
        //备份酒操作业务表
        super.backupWineDetails(laoczWineDetails);
        //新增数据到历史表
        super.saveHistory(laoczWineDetails);
        //查询当前业务id还有没有正在完成的任务，如果没有了，就备份酒操作业务表
        super.taskVerify(laoczWineDetails.getBusyId());
    }


}
