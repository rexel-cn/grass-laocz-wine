package com.rexel.laocz.service;

import com.rexel.laocz.domain.dto.WineSampApplyDTO;

/**
 * @ClassName WineSampService
 * @Description 酒取样
 * @Author 孟开通
 * @Date 2024/3/19 18:13
 **/
public interface WineSampService {

    /**
     * 酒取样申请
     *
     * @param wineSampApplyDTO 酒取样申请
     */
    void wineSampApply(WineSampApplyDTO wineSampApplyDTO);

    /**
     * 酒取样完成
     *
     * @param wineDetailsId 酒操作业务详情id
     */
    void wineSampFinish(Long wineDetailsId);
}
