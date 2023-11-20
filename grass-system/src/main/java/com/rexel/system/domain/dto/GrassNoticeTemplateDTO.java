package com.rexel.system.domain.dto;

import com.rexel.system.domain.GrassNoticeTemplate;
import lombok.Data;

import java.util.List;

/**
 * ClassName GrassNoticeTemplateDTO
 * Description
 * Author 孟开通
 * Date 2022/8/2 16:26
 **/
@Data
public class GrassNoticeTemplateDTO extends GrassNoticeTemplate {
    /**
     * 通知范围
     */
    List<GrassNoticeScopeDTO> grassNoticeScopeList;
    /**
     * 通知方式
     */
    private List<Long> modeArray;
}
