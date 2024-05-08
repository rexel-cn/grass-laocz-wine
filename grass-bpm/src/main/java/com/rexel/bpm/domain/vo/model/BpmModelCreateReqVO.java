package com.rexel.bpm.domain.vo.model;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class BpmModelCreateReqVO {

    @NotEmpty(message = "流程标识不能为空")
    private String key;

    @NotEmpty(message = "流程名称不能为空")
    private String name;

    /**
     * 流程描述
     */
    private String description;

}
