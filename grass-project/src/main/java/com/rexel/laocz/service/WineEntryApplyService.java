package com.rexel.laocz.service;

import com.rexel.laocz.domain.dto.WineEntryApplyDTO;
import com.rexel.laocz.domain.dto.WineEntryDTO;
import com.rexel.laocz.domain.vo.WineRealDataVO;

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
     * 查询入酒出酒当前实时数据
     *
     * @param wineDetailsId 酒操作业务详情id
     * @return 入酒出酒实时数据
     */
    WineRealDataVO getWineRealData(Long wineDetailsId);


    void wineEntryFinish(Long wineDetailsId);

}
