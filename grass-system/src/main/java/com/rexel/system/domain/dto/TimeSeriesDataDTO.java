package com.rexel.system.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassName timeSeriesData
 * @Description
 * @Author 孟开通
 * @Date 2022/10/24 11:08
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeSeriesDataDTO {
    private String startTime;
    private String stopTime;
    private String bucketId;
    private String deviceId;
    private List<String> pointIdList;
}
