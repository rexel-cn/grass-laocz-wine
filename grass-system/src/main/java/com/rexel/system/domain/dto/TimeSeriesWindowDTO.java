package com.rexel.system.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName TimeSeriesWindowDTO
 * @Description
 * @Author ids
 * @Date 2022/11/01
 **/
@Data
public class TimeSeriesWindowDTO implements Serializable {
    private String startTime;
    private String stopTime;
    private String bucketId;
    private String deviceId;
    private List<String> pointIdList;
    private List<String> funcList;
    private String every;
    private Integer fillType;
}
