package com.rexel.system.domain.vo;

import lombok.Data;
// 封装测点id和设备id
@Data
public class GrassPDVO{
    // 设备ID
    private String deviceId;
    // 测点ID
    private String pointId;
    private String pointType;
}
