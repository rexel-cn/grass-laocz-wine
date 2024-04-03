package com.rexel.laocz.service;

import com.rexel.laocz.domain.dto.WineSampApplyDTO;
import com.rexel.laocz.domain.vo.WineOperaPotteryAltarVO;

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

    /**
     * 二维码扫描获取入酒陶坛信息
     *
     * @param potteryAltarNumber 陶坛编号
     * @return 陶坛信息
     */
    WineOperaPotteryAltarVO qrCodeScan(String potteryAltarNumber);
}
