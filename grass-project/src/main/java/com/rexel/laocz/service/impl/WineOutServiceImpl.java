package com.rexel.laocz.service.impl;

import com.rexel.laocz.domain.dto.WineOutApplyDTO;
import com.rexel.laocz.service.WineOutService;
import org.springframework.stereotype.Service;

/**
 * @ClassName WineOutServiceImpl
 * @Description
 * @Author 孟开通
 * @Date 2024/3/11 14:26
 **/
@Service
public class WineOutServiceImpl implements WineOutService {

    /**
     * 出酒申请
     *
     * @param wineOutApplyDTO 出酒申请参数：陶坛罐ID，申请重量
     */
    @Override
    public void wineOutApply(WineOutApplyDTO wineOutApplyDTO) {

    }


    /**
     * 出酒
     *
     * @param busyId 业务id
     */
    @Override
    public void wineOutStart(String busyId) {

    }
}
