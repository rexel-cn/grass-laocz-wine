package com.rexel.system.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * ClassName PointCurveDto
 * Description
 * Author 孟开通
 * Date 2022/7/18 16:09
 **/
@Data
public class PulsePointCurveDTO implements Serializable {
    private String pointId;
    private String bucketId;
    private String deviceId;
    private int from;
    private String every;

}
