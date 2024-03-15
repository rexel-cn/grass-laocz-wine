package com.rexel.laocz.domain.vo;

import com.rexel.laocz.domain.LaoczLiquorRuleInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LiquorRuleInfoVo extends LaoczLiquorRuleInfo {

    /**
     * 通知人员数目
     */
    private Integer count;
}
