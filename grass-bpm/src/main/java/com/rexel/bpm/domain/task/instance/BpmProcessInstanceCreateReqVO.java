package com.rexel.bpm.domain.task.instance;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class BpmProcessInstanceCreateReqVO {

    /**
     * 流程定义的编号
     */
    @NotEmpty(message = "流程定义编号不能为空")
    private String processDefinitionId;

    /**
     * 变量实例（动态表单）
     */
    private Map<String, Object> variables = new HashMap<>();

}
