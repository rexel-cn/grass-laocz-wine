package com.rexel.system.domain.dto;

import com.rexel.system.domain.GrassNoticeScope;
import lombok.Data;

import java.util.List;

/**
 * ClassName GrassNoticeScopeDTO
 * Description
 * Author 孟开通
 * Date 2022/8/2 16:51
 **/
@Data
public class GrassNoticeScopeDTO extends GrassNoticeScope {
    /**
     * 通知对象集合
     */
    private List<Long> noticeObjectArray;
}
