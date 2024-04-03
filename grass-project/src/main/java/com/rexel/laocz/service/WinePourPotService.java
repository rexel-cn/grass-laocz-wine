package com.rexel.laocz.service;

import com.rexel.laocz.domain.dto.WineEntryDTO;
import com.rexel.laocz.domain.dto.WinePourPotApplyDTO;

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
     * @param wineDetailsId 酒操作业务详情id
     * @return 重量
     */
    String winePourPotOutStart(Long wineDetailsId);

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
}
