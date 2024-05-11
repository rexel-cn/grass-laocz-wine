package com.rexel.laocz.service;

import com.rexel.laocz.domain.dto.WineEntryApplyDTO;
import com.rexel.laocz.domain.dto.WineEntryDTO;
import com.rexel.laocz.domain.vo.WineOperaPotteryAltarVO;
import com.rexel.laocz.domain.vo.WineRealDataVO;

import java.util.List;

/**
 * @ClassName WineEntryApplyService
 * @Description 入酒
 * @Author 孟开通
 * @Date 2024/3/11 09:34
 **/
public interface WineEntryApplyService {
    /**
     * 生成酒品批次号
     *
     * @return 酒品批次号
     */
    String getLiquorBatchId();

    /**
     * 自动选择陶坛
     *
     * @param applyWeight 申请重量
     * @return 陶坛列表
     */
    List<WineOperaPotteryAltarVO> automaticChoosePotteryAltar(Double applyWeight);

    /**
     * 二维码扫描获取入酒陶坛信息
     *
     * @param potteryAltarNumber 陶坛编号
     * @return 陶坛信息
     */
    WineOperaPotteryAltarVO qrCodeScan(String potteryAltarNumber);

    /**
     * 入酒申请
     *
     * @param wineEntryApplyDTO 入酒申请参数：申请重量，陶坛罐ID，酒品管理ID，酒品批次号
     */
    void wineEntryApply(WineEntryApplyDTO wineEntryApplyDTO);

    /**
     * 入酒开始
     *
     * @param wineEntryDTO 业务id
     */
    void wineEntry(WineEntryDTO wineEntryDTO);

    /**
     * 入酒
     *
     * @param wineEntryDTO 入酒参数
     */
    void wineIn(WineEntryDTO wineEntryDTO);

    /**
     * 查询入酒出酒当前实时数据
     *
     * @param wineDetailsId 酒操作业务详情id
     * @return 入酒出酒实时数据
     */
    WineRealDataVO getWineRealData(Long wineDetailsId);

    /**
     * 入酒结束
     *
     * @param wineDetailsId 酒操作业务详情id
     */
    void wineEntryFinish(Long wineDetailsId);

    /**
     * 入酒审批结束后处理，审批不通过后确认操作
     * @param busyId 业务id
     */
    void updateWineEntryStatus(String busyId);


}
