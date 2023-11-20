package com.rexel.common.core.domain;/**
 * @Author 董海
 * @Date 2022/7/18 14:05
 * @version 1.0
 */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName BaseBucket
 * @Description BaseBucket
 * @Author Hai.Dong
 * @Date 2022/7/18 14:05
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseBucket implements Serializable {

    private String bucketId;
}
