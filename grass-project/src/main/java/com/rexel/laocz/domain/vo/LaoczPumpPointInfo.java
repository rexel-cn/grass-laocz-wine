package com.rexel.laocz.domain.vo;

import lombok.Data;

/**
 * @ClassName LaoczPumpPointInfo
 * @Description 泵测点信息
 * @Author 孟开通
 * @Date 2024/6/26 10:52
 **/
@Data
public class LaoczPumpPointInfo {
    /**
     * 泵主键ID
     */
    private Long pumpId;
    /**
     * 泵编号
     */
    private String pumpNumber;
    /**
     * 测点ID
     */
    private Long pointPrimaryKey;
}
