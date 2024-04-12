package com.rexel.laocz.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName WineOperationDTO
 * @Description 我的事项DTO
 * @Author 孟开通
 * @Date 2024/4/12 17:29
 **/
@Data
public class WineOperationDTO {
    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date beginTime;
    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;
    /**
     * 操作类型
     */
    private String operationType;
    /**
     * 陶坛管理编号
     */
    private String potteryAltarNumber;
}
