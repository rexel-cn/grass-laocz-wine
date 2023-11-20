package com.rexel.earlywarning.vo;

import lombok.Data;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 * JobDetailVo
 *
 * @author chunhui.qu
 * @date 2022-3-2
 */
@Data
@ToString
public class JobDetailVo {
    /** 时间 */
    private Date time;

    /** 时间 */
    private String event;

    /** 状态数组 */
    private List<DetailDataVo> data;
}
