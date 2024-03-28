package com.rexel.laocz.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 陶坛操作记录详情
 *
 * @author Yiyang.Hu
 * @version 1.0
 * @date 2024-03-20 1:46 PM
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LaoczWineHistoryInfoVO {
    /**
     * 申请的工单id
     */
    private String workOrderId;
    /**
     * 标题是入酒信息、出酒信息、倒坛信息、取样信息
     */
    private Long headline;
    /**
     * 操作酒信息信息
     */
    private CurrentWineIndustryInfoVO currentWineIndustryVO;

    /**
     * 陶坛信息
     */
    private PotteryAltarInformationInfoVO potteryAltarInformationInfoVO;
}
