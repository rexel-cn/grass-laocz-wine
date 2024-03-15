package com.rexel.laocz.service;

import com.rexel.laocz.domain.dto.WineOutApplyDTO;

/**
 * @ClassName WineOutService
 * @Description 出酒
 * @Author 孟开通
 * @Date 2024/3/11 14:01
 **/
public interface WineOutService {
    /**
     * 出酒申请
     *
     * @param wineOutApplyDTO 出酒申请参数：陶坛罐ID，申请重量
     */
    void wineOutApply(WineOutApplyDTO wineOutApplyDTO);

    /**
     * 出酒
     *
     * @param busyId 业务id
     */
    void wineOutStart(String busyId);
}
