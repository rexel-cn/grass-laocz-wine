package com.rexel.laocz.domain.vo;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.rexel.common.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 陶坛实际
 *
 * @author Yiyang.Hu
 * @version 1.0
 * @date 2024-03-20 5:44 PM
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LaoczBatchPotteryMappingVO {
    private static final long serialVersionUID = 1L;

    /**
     * 陶坛管理编号
     */
    @Excel(name = "陶坛管理编号")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String potteryAltarNumber;
    /**
     * 陶坛状态，1：使用，2封存
     */
    @Excel(name = "陶坛状态")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String potteryAltarState;
    /**
     * 场区名称
     */
    @Excel(name = "场区名称")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String areaName;
    /**
     * 防火区名称
     */
    @Excel(name = "防火区名称")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String fireZoneName;
    /**
     * 酒品名称
     */
    @Excel(name = "酒品名称")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String liquorName;
    /**
     * 酒液批次ID
     */
    @Excel(name = "酒液批次ID")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Long liquorBatchId;
    /**
     * 该批次在此陶坛中的实际酒重量
     */
    @Excel(name = "该批次在此陶坛中的实际酒重量")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private Double actualWeight;
    /**
     * 存储时长
     */
    @Excel(name = "存储时长")
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    private String StorageDuration;
}
