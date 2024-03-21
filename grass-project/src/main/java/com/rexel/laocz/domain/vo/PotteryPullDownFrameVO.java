package com.rexel.laocz.domain.vo;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.rexel.common.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 陶坛下拉框
 *
 * @author Yiyang.Hu
 * @version 1.0
 * @date 2024-03-19 3:23 PM
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PotteryPullDownFrameVO {
    /**
     * 陶坛管理主键ID
     */
    private Long potteryAltarId;
    /**
     * 陶坛管理编号
     */
    private String potteryAltarNumber;
}
