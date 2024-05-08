package com.rexel.bpm.domain.vo.model;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class BpmModelUpdateStateReqVO {

    /**
     * 编号
     */
    @NotNull(message = "编号不能为空")
    private String id;

    /**
     * 状态
     */
    @NotNull(message = "状态不能为空")
    private Integer state; // 参见 Flowable SuspensionState 枚举

}
