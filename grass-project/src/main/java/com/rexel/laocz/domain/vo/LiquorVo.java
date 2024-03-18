package com.rexel.laocz.domain.vo;


import com.rexel.common.annotation.Excel;
import lombok.Data;

@Data
public class LiquorVo {


    /**
     * 酒品名称
     */
    @Excel(name = "酒品名称")
    private String liquorName;
    /**
     * 酒类等级——酒品字典维护
     */
    @Excel(name = "酒类等级")
    private String liquorLevel;
    /**
     * 酒业轮次
     */
    @Excel(name = "酒业轮次")
    private String liquorRound;
    /**
     * 酒香名称——酒品字典维护
     */
    @Excel(name = "香型名称")
    private String liquorFlavorName;
    /**
     * 酒液来源
     */
    @Excel(name = "酒液来源")
    private String liquorSource;
    /**
     * 酒液年份
     */
    @Excel(name = "酒液年份")
    private String liquorYear;
    /**
     * 酒液酿造时间
     */
    @Excel(name = "酿造时间")
    private String liquorBrewingTime;
    /**
     * 酒精度数
     */
    @Excel(name = "酒精度数")
    private String liquorContent;

}
