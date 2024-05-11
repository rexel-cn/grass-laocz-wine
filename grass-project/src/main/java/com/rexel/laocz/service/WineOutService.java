package com.rexel.laocz.service;

import com.rexel.laocz.domain.LaoczWineDetails;
import com.rexel.laocz.domain.dto.WineOutApplyDTO;
import com.rexel.laocz.domain.dto.WineOutStartDTO;

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
     * 出酒操作，称重罐称重量
     *
     * @param wineOutStartDTO 酒操作业务详情id
     * @return 称重量
     */
    LaoczWineDetails wineOutStart(WineOutStartDTO wineOutStartDTO);

    /**
     * 出酒操作完成
     * @param wineDetailsId 酒操作业务详情id
     */
    void wineOutFinish(Long wineDetailsId);

    /**
     * 出酒审批结束后处理，审批通过或不通过
     * @param busyId 业务id
     */
    void updateWineOutStatus(String busyId);
}
