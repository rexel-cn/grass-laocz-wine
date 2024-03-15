package com.rexel.laocz.service;

import com.rexel.laocz.domain.LaoczWineDetails;
import com.rexel.laocz.enums.WineRealRunStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    /**
     * 酒操作结束(出酒、入酒)，更新状态和重量
     *
     * @param wineDetailsId      酒操作id
     * @param weighingTankWeight 称重罐重量
     */
    protected void wineEnd(Long wineDetailsId, Long weighingTankWeight) {
        iLaoczWineDetailsService.lambdaUpdate()
                .eq(LaoczWineDetails::getWineDetailsId, wineDetailsId)
                .set(LaoczWineDetails::getBusyStatus, WineRealRunStatusEnum.COMPLETED.getValue())
                .set(LaoczWineDetails::getWeighingTankWeight, weighingTankWeight).update();
    }


    protected String readWeighingTankWeight() {
        return "100";
    }
}
