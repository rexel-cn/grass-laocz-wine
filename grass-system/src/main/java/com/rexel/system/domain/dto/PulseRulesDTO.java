package com.rexel.system.domain.dto;

import com.rexel.system.domain.GrassRulesInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * ClassName PulseRulesAddDTO
 * Description
 *
 * @Author 孟开通
 * Date 2022/8/5 15:00
 **/
@Data
public class PulseRulesDTO extends GrassRulesInfo implements Serializable {
    /**
     * 模板
     */
    List<Long> noticeTemplateIds;
}
