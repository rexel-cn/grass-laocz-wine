package com.rexel.system.domain.vo;

import com.rexel.system.domain.GrassRulesInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * ClassName AlarmRulesDetailVo
 * Description
 *
 * @Author 孟开通
 * Date 2022/8/8 17:32
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AlarmRulesDetailVo extends GrassRulesInfo implements Serializable {
    private List<RuleNoticeVO> ruleNoticeVOList;
}
