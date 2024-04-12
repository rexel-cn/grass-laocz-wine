package com.rexel.laocz.service.impl;

import com.rexel.common.exception.CustomException;
import com.rexel.common.utils.SequenceUtils;
import com.rexel.laocz.domain.LaoczBatchPotteryMapping;
import com.rexel.laocz.domain.LaoczWineDetails;
import com.rexel.laocz.domain.dto.WineSampApplyDTO;
import com.rexel.laocz.domain.dto.WineSamplePotteryAltarDTO;
import com.rexel.laocz.domain.vo.WineOperaPotteryAltarVO;
import com.rexel.laocz.enums.OperationTypeEnum;
import com.rexel.laocz.enums.RealStatusEnum;
import com.rexel.laocz.enums.WineDetailTypeEnum;
import com.rexel.laocz.service.WineAbstract;
import com.rexel.laocz.service.WineSampService;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        //新增 工单表（流程审批创建）,然后需要把laocz_liquor_batch的liquor_batch_id字段作为业务id来和流程关联
        String workId = SequenceUtils.nextId().toString();
        //生成busy_id
        String busyId = SequenceUtils.nextId().toString();
        //新增laocz_wine_operations
        saveLaoczWineOperations(busyId, workId, OperationTypeEnum.SAMPLING);
        //新增laocz_wine_details
        saveLaoczWineDetails(wineSampApplyDTO, busyId, workId, laoczBatchPotteryMapping);

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
     * @param wineSampApplyDTO 酒取样申请
     * @param busyId 业务id
     * @param workId 工单id
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
