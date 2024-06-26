package com.rexel.laocz.domain.vo;

import lombok.Data;

/**
 * @ClassName LaoczPumpPointInfo
 * @Description 泵测点信息
 * @Author 孟开通
 * @Date 2024/6/26 10:52
 **/
@Data
public class LaoczWeighingTankPointInfo {
    /**
     * 称重罐主键ID
     */
    private Long weighingTankId;
    /**
     * 称重罐编号
     */
    private String weighingTankNumber;
    /**
     * 测点ID
     */
    private Long pointPrimaryKey;
}
