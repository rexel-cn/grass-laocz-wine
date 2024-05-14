package com.rexel.bpm.domain.vo.model;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class BpmModelUpdateReqVO {

    @NotEmpty(message = "编号不能为空")
    private String id;

    private String name;

    //@URL(message = "流程图标格式不正确")
    private String icon;

    private String description;

    private String category;

    private String bpmnXml;

    //@InEnum(BpmModelFormTypeEnum.class)
    private Integer formType = 20;
    private Long formId;
    private String formCustomCreatePath;
    private String formCustomViewPath;

}
