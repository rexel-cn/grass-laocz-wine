package com.rexel.system.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * ClassName PointQueryDTO
 * Description 分页查询测点请求 pulse
 *
 * @author 孟开通
 * Date 2022/7/7 14:36
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PulsePointQueryDTO implements Serializable {
    /**
     * 设备ID
     */
    private String deviceId;
    /**
     * 测点ID
     */
    private String pointId;

    private String pointType;
    /**
     * 测点名
     */
    private String pointName;
    /**
     * 资产类型Id
     */
    private String assetTypeId;
    /**
     * 资产设备id
     */
    private String assetId;
    /**
     * 标签键
     */
    private String tagKey;

    /**
     * 根据资产类型id 查询得来
     */
    private List<String> assetIds;

}
