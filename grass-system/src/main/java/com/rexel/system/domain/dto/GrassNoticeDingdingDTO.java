package com.rexel.system.domain.dto;

import com.rexel.system.domain.GrassNoticeDingding;
import lombok.Data;

import java.util.List;

/**
 * ClassName GrassNoticeDingdingDTO
 * Description
 * Author 孟开通
 * Date 2022/8/4 14:02
 **/
@Data
public class GrassNoticeDingdingDTO {
    private List<GrassNoticeDingding> dingDingList;
    private Long noticeConfigId;
}
