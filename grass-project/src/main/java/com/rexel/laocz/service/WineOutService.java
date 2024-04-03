package com.rexel.laocz.service;

import com.rexel.laocz.domain.dto.WineOutApplyDTO;
import com.rexel.laocz.domain.vo.WineOperaPotteryAltarVO;

import java.util.List;

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
     * @param list 出酒申请参数：陶坛罐ID，申请重量
     */
    void wineOutApply(List<WineOutApplyDTO> list);
    /**
     * 二维码扫描获取入酒陶坛信息
     * @param potteryAltarNumber 陶坛编号
     * @return 陶坛信息
     */
    WineOperaPotteryAltarVO qrCodeScan(String potteryAltarNumber);

    /**
     * 出酒操作，称重罐称重量
     *
     * @param wineDetailsId 酒操作业务详情id
     * @return 称重量
     */
    String wineOutStart(Long wineDetailsId);

    /**
     * 出酒操作完成
     * @param wineDetailsId 酒操作业务详情id
     */
    void wineOutFinish(Long wineDetailsId);
}
