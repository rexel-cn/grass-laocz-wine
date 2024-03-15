package com.rexel.laocz.domain.dto;

import lombok.Data;

import java.util.List;

/**
 * @ClassName WineOutApplyDTO
 * @Description 出酒申请参数
 * @Author 孟开通
 * @Date 2024/3/11 14:06
 **/
@Data
public class WineOutApplyDTO {

    /**
     * 陶坛编号+申请重量
     */
    private List<WineOutApply> list;


    @Data
    static class WineOutApply {
        /**
         * 陶坛罐主键id
         */
        private Long potteryAltarId;
        /**
         * 申请重量
         */
        private Double applyWeight;
    }
}
