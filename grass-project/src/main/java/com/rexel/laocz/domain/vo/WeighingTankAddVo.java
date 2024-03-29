package com.rexel.laocz.domain.vo;

import lombok.Data;

@Data
public class WeighingTankAddVo {
    /**
     * 设备测点关联表Id
     */
    private Long equipmentPointId;
    /**
     * 属性名称
     */
    private String name;
    /**
     * 使用标识
     */
    private String useMark;
    /**
     * 测点的主键id
     */
    private Long pointPrimaryKey;

    private String pointName;

    private String pointId;

}
