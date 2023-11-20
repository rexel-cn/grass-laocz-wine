package com.rexel.system.domain.vo;


import com.rexel.system.domain.GrassRuleNotice;
import lombok.Data;

import java.util.List;

/**
 * ClassName GrassRuleNoticeVo
 * Description
 * Author 孟开通
 * Date 2022/8/5 14:05
 **/
@Data
public class GrassRuleNoticeVO extends GrassRuleNotice {
    private List<GrassRuleNotice> ruleNoticeList;
}
