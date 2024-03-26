package com.rexel.laocz.service;

import com.rexel.laocz.domain.LaoczSamplingHistority;
import com.rexel.laocz.domain.LaoczWineDetails;
import com.rexel.laocz.domain.LaoczWineHistory;
import com.rexel.laocz.domain.dto.WineHistoryDTO;
import com.rexel.laocz.enums.OperationTypeEnum;
import com.rexel.laocz.mapper.LaoczWineDetailsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName WineAbstract
 * @Description
 * @Author 孟开通
 * @Date 2024/3/13 17:48
 **/
@Service
public abstract class WineAbstract {

    @Autowired
    private ILaoczWineDetailsService iLaoczWineDetailsService;
    @Autowired
    private ILaoczSamplingHistorityService iLaoczSamplingHistorityService;

    @Autowired
    private LaoczWineDetailsMapper laoczWineDetailsMapper;

    protected void saveHistory(Long wineDetailsId, OperationTypeEnum operationTypeEnum) {
        List<WineHistoryDTO> wineHistoryDTOS = laoczWineDetailsMapper.selectWineHistoryDTOList(wineDetailsId);

        switch (operationTypeEnum) {
            case WINE_ENTRY:
                //入酒
                break;
            case WINE_OUT:
                //出酒
                break;
            case POUR_POT:
                //倒坛
                break;
            case SAMPLING:
                //取样
                break;
            default:
                throw new IllegalArgumentException("Unexpected value: " + operationTypeEnum);
        }
    }

    protected void saveLaoczWineDetails() {

    }


    private void saveSamplingHistory(LaoczWineDetails laoczWineDetails, LaoczWineHistory laoczWineHistory) {
        //新增数据到laocz_sampling_histority
        LaoczSamplingHistority laoczSamplingHistority = new LaoczSamplingHistority();
        //工单id
        laoczSamplingHistority.setWorkOrderId(laoczWineDetails.getWorkOrderId());
        //业务标识
        laoczSamplingHistority.setBusyId(laoczWineDetails.getBusyId());
        //酒批次
        laoczSamplingHistority.setLiquorBatchId(laoczWineDetails.getLiquorBatchId());
        //陶坛罐id
        laoczSamplingHistority.setPotteryAltarId(laoczWineDetails.getPotteryAltarId());
        //取样用途
        laoczSamplingHistority.setSamplingPurpose(laoczWineDetails.getSamplingPurpose());
        //取样重量
        laoczSamplingHistority.setSamplingWeight(laoczWineDetails.getPotteryAltarApplyWeight());
        //取样时间
        laoczSamplingHistority.setSamplingDate(laoczWineDetails.getOperationTime());
        //场区名称
        laoczSamplingHistority.setAreaName(laoczWineHistory.getAreaName());
        //防火区名称
        laoczSamplingHistority.setFireZoneName(laoczWineHistory.getFireZoneName());
        //陶坛管理编号
        laoczSamplingHistority.setPotteryAltarNumber(laoczWineHistory.getPotteryAltarNumber());
        //新增取样记录表
        iLaoczSamplingHistorityService.save(laoczSamplingHistority);
    }
}
