package com.rexel.system.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName TimeSeriesLastDTO
 * @Description
 * @Author 孟开通
 * @Date 2022/8/16 15:26
 **/
@Data
public class TimeSeriesLastDTO implements Serializable {
    /**
     * 网关设备ID
     */
    private String deviceId;
    /**
     * 测点列表
     */
    private List<String> pointIdList;
}
