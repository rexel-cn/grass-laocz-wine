package com.rexel.system.domain.vo.reduce;

import lombok.Data;

import java.util.List;

/**
 * 数据降维信息
 *
 * @author chunhui.qu
 * @date 2023-02-28
 */
@Data
public class ReduceInfo {
    /**
     * 颗粒度
     */
    private String interval;

    /**
     * 存储日期
     */
    private int retentionDays;

    /**
     * 聚合目标
     */
    private List<ReduceTargetInfo> targetList;
}
