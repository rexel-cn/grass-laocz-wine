package com.rexel.system.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * ClassName PointUpdateDTO
 * Description
 * Author 孟开通
 * Date 2022/7/18 15:40
 **/
@Data
public class PulsePointUpdateDTO implements Serializable {
    private Integer id;
    private String deviceId;
    private String pointType;
    private Double inMin;
    private Double inMax;
    private Double outMin;
    private Double outMax;
}
