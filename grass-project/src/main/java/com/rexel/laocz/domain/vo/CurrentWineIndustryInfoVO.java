package com.rexel.laocz.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 当前酒液
 *
 * @author Yiyang.Hu
 * @version 1.0
 * @date 2024-03-19 4:34 PM
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrentWineIndustryInfoVO {
    /**
     * 申请重量
     */
    private Double ApplyWeight;
    /**
     * 酒品名称
     */
    private String liquorName;
    /**
     * 酒批次ID，外键关联laocz_liquor_batch
     */
    private String liquorBatchId;
    /**
     * 酒类等级——酒品字典维护
     */
    private String liquorLevel;
    /**
     * 酒业轮次
     */
    private String liquorRound;
    /**
     * 酒香名称——酒品字典维护
     */
    private String liquorFlavorName;
    /**
     * 酒液来源
     */
    private String liquorSource;
    /**
     * 酒液年份
     */
    private String liquorYear;
    /**
     * 酒液酿造时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date liquorBrewingTime;
    /**
     * 酒精度数
     */
    private String liquorContent;
}
