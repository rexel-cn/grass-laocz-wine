package com.rexel.laocz.service;

import com.rexel.laocz.domain.LaoczWineDetails;
import com.rexel.laocz.domain.dto.QrInCodeScanDTO;
import com.rexel.laocz.domain.dto.WineEntryDTO;
import com.rexel.laocz.domain.dto.WineOutStartDTO;
import com.rexel.laocz.domain.dto.WinePourPotApplyDTO;
import com.rexel.laocz.domain.vo.WineOperaPotteryAltarVO;

/**
 * @ClassName WinePourPotService
 * @Description 倒坛
 * @Author 孟开通
 * @Date 2024/4/1 15:05
 **/
public interface WinePourPotService {

    /**
     * 倒坛申请
     *
     * @param winePourPotApplyDTO 倒坛申请参数：申请重量，出酒陶坛罐ID，入酒陶坛罐ID
     */
    void winePourPotApply(WinePourPotApplyDTO winePourPotApplyDTO);

    /**
     * 倒坛出酒开始获取重量
     *
     * @param wineOutStartDTO 酒操作业务详情id
     * @return 重量
     */
    LaoczWineDetails winePourPotOutStart(WineOutStartDTO wineOutStartDTO);

    /**
     * 倒坛出酒
     *
     * @param wineDetailsId 酒操作业务详情id
     * @return 入酒的id
     */
    Long winePourPotOut(Long wineDetailsId);

    /**
     * 倒坛入酒
     *
     * @param wineDetailsId 酒操作业务详情id
     */
    void winePourPotIn(Long wineDetailsId);

    /**
     * 倒坛入酒开始
     *
     * @param wineEntryDTO 酒入坛参数
     */
    void winePourPotInStart(WineEntryDTO wineEntryDTO);

    /**
     * 二维码扫描获取倒坛出酒陶坛信息
     *
     * @param potteryAltarNumber 陶坛编号
     * @return 陶坛信息
     */
    WineOperaPotteryAltarVO qrOutCodeScan(String potteryAltarNumber);

    /**
     * 二维码扫描获取倒坛出酒陶坛信息
     *
     * @param potteryAltarNumber 陶坛编号，出酒陶坛id，出酒重量
     * @return 陶坛信息
     */
    WineOperaPotteryAltarVO qrInCodeScan(QrInCodeScanDTO potteryAltarNumber);

    /**
     * 倒坛审批结束后处理，审批通过或不通过
     * @param busyId 业务id
     */
    void updateWinePourStatus(String busyId);

}
