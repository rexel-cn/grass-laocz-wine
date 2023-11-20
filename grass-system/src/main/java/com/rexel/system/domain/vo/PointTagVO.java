package com.rexel.system.domain.vo;

import lombok.Data;

/**
 * @ClassName PointTagVO
 * @Description
 * @Author 孟开通
 * @Date 2022/11/16 11:58
 **/
@Data
public class PointTagVO {
    /**
     * 测点id
     */
    private String pointId;
    /**
     * 设备id
     */
    private String deviceId;
    /**
     * 租户id
     */
    private String tenantId;
    /**
     * 标签键
     */
    private String tagKey;
    /**
     * 标签值
     */
    private String tagValue;
    /**
     * 标签类型
     */
    private String tagType;
}
