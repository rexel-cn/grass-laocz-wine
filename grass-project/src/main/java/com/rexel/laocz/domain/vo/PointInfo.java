package com.rexel.laocz.domain.vo;

import lombok.Data;

@Data
public class PointInfo {
    /**
     * 测点类型
     */
    private String pointType;
    /**
     * 测点Id
     */
    private String pointId;
    /**
     * 测点名称
     */
    private String point_name;
}
