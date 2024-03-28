package com.rexel.laocz.domain.vo;

import lombok.Data;

@Data
/**
 * 移动端批次查询VO类
 */
public class BatchInfoVo {
    /**
     * 酒批次ID
     */
    private String liquorBatchId;
    /**
     * 酒品名称
     */
    private String liquorName;
    /**
     * 存储陶坛数
     */
    private Integer count;
}
