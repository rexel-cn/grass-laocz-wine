package com.rexel.system.domain.dto;

import lombok.Data;

/**
 * @ClassName IGsCurrentPlanDTO
 * @Description
 * @Author 孟开通
 * @Date 2022/12/13 13:11
 **/
@Data
public class IGsCurrentPlanDTO {

    /**
     * 产品品名
     */
    private String specification;
    /**
     * 计划产量
     */
    private String planOutput;
}
