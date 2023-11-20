package com.rexel.system.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * ClassName PointQueryDTO
 * Description 分页查询规则请求 pulse
 * Author 孟开通
 * Date 2022/8/5 11:36
 **/
@Data
@AllArgsConstructor
public class PulseRulesQueryDTO implements Serializable {
    /**
     * 页号
     */
    private Integer pageNum;
    /**
     * 单页条数
     */
    private Integer pageSize;
    /**
     * 存储空间id
     */
    private String bucketId;
    /**
     * 设备ID
     */
    private String deviceId;
    /**
     * 测点ID
     */
    private String pointId;
    /**
     * 规则名称
     */
    private String rulesName;

}
