package com.rexel.system.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName PulseBucketDTO
 * @Description
 * @Author 孟开通
 * @Date 2022/9/16 14:47
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PulseBucketDTO implements Serializable {
    private String bucketId;
    private Long everySeconds;
}
