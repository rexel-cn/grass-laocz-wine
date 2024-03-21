package com.rexel.laocz.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 防火区下拉框
 *
 * @author Yiyang.Hu
 * @version 1.0
 * @date 2024-03-19 3:14 PM
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FireZoneDropDownBoxVO {
    /**
     * 防火区主键ID
     */
    private Long fireZoneId;
    /**
     * 防火区名称
     */
    private String fireZoneName;
}
