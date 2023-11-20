package com.rexel.system.domain.dto;/**
 * @Author 董海
 * @Date 2022/7/21 18:27
 * @version 1.0
 */

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName PointListRtDto
 * @Description dh
 * @Author Hai.Dong
 * @Date 2022/7/21 18:27
 **/
@Data
public class PointListRtDto implements Serializable {


    private String bucketId;
    private String deviceId;
    private List<String> pointIdList;
}
