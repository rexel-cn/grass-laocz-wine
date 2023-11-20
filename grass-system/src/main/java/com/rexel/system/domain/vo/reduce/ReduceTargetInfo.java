package com.rexel.system.domain.vo.reduce;

import lombok.Data;

import java.util.List;

/**
 * 数据降维测点对象信息
 *
 * @author chunhui.qu
 * @date 2023-02-28
 */
@Data
public class ReduceTargetInfo {
    /**
     * 空间ID
     */
    private String bucketId;

    /**
     * 设备ID
     */
    private String deviceId;

    /**
     * 测点列表
     */
    private List<String> pointIdList;
}
