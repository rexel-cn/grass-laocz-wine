package com.rexel.earlywarning.vo;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * JobDetailVo
 *
 * @author chunhui.qu
 * @date 2022-3-2
 */
@Data
@ToString
public class DetailDataVo {
    /** 任务ID */
    private String jobId;
    /** 任务状态 */
    private String jobStatus;
    /** 启动时间 */
    private Date jobStartTime;
}
