package com.rexel.system.domain.vo.common;

import lombok.Data;

/**
 * @ClassName NumberCountVO
 * @Description NumberCountVO
 * @Author Hai.Dong
 * @Date 2023/3/2 11:16
 **/
@Data
public class NumberCountVO {

    /**
     * 总数
     */
    private int total;

    /**
     * 在线数量
     */
    private int onlineNumber;
    /**
     * 离线数量
     */
    private int offlineNumber;
}
