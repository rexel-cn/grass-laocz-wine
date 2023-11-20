package com.rexel.earlywarning.vo;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * JobNoticeVo
 *
 * @author chunhui.qu
 * @date 2022-3-2
 */
@Data
@ToString
public class JobNoticeVo {
    /** 时间 */
    private Date time;

    /** 时间 */
    private String event;
}
