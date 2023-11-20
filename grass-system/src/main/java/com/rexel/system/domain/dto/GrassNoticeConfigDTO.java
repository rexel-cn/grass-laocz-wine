package com.rexel.system.domain.dto;

import lombok.Data;

/**
 * ClassName GrassNoticeConfigDTO
 * Description
 * Author 孟开通
 * Date 2022/7/29 16:28
 **/
@Data
public class GrassNoticeConfigDTO {
    /**
     * 自增ID
     */
    private Long id;
    /**
     * 是否开通(0:未开通;1:开通)
     */
    private String isOpen;
}
