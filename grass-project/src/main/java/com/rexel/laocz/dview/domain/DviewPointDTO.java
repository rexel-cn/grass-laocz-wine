package com.rexel.laocz.dview.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName DviewPointDTO
 * @Description Dview访问点DTO
 * @Author 孟开通
 * @Date 2023/2/20 16:34
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DviewPointDTO implements Serializable {
    /**
     * 测点id
     */
    private String pointId;
    /**
     * 测点类型
     */
    private String pointType;
    /**
     * 测点名
     */
    private String pointName;
    /**
     * 测点值
     */
    private String pointValue;


}
