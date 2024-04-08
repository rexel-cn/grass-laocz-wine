package com.rexel.laocz.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rexel.common.annotation.Excel;
import lombok.Data;

import java.util.Date;

@Data
public class OverviewVo extends BoardDataVO {
    /**
     * 区域名称
     */
    private String areaName;
    /**
     * 防火区名称
     */
    private String fireZoneName;
    /**
     * 操作人
     */
    private String createBy;
    /**
     * 酒液酿造时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;
    /**
     * 满坛重量
     */
    private String potteryAltarFullAltarWeight;
    /**
     * 酒品管理ID
     */
    private Long liquorManagementId;
}
