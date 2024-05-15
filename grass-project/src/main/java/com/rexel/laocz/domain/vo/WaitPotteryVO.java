package com.rexel.laocz.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rexel.laocz.domain.LaoczLiquorManagement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * 待办事宜查询陶坛信息VO类
 * @author Huwei.Fu
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WaitPotteryVO {
    /**
     * 申请的重量
     */
    private Double applyWeight;
    /**
     * 酒液批次
     */
    private String liquorBatchId;
    /**
     * 酒品信息
     */
    private LaoczLiquorManagement laoczLiquorManagement;
    /**
     * 陶坛信息
     */
    private List<WaitPotteryAltarVO> waitPotteryAltarVOS;

}
