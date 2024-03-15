package com.rexel.laocz.domain.vo;

import lombok.Data;

/**
 * @ClassName WineDetailPointVO
 * @Description 酒详情操作相关测点
 * @Author 孟开通
 * @Date 2024/3/14 17:20
 **/
@Data
public class WineDetailPointVO {
    /**
     * 使用标识
     */
    private String useMark;
    /**
     * 测点id
     */
    private String pointId;
    /**
     * 测点类型
     */
    private String pointType;
    /**
     * 测点名称
     */
    private String pointName;
}
