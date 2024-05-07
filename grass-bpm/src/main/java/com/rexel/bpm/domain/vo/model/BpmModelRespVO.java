package com.rexel.bpm.domain.vo.model;

import com.rexel.bpm.domain.vo.process.BpmProcessDefinitionRespVO;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class BpmModelRespVO {

    /**
     * 编号
     */
    private String id;

    /**
     * 流程标识
     */
    private String key;

    /**
     * 流程名称
     */
    private String name;

    /**
     * icon
     */
    private String icon;

    /**
     * 流程描述
     */
    private String description;

    /**
     * 流程分类编码
     */
    private String category;
    /**
     * 流程分类名字
     */
    private String categoryName;

    /**
     * 表单类型-参见 bpm_model_form_type 数据字典
     */
    private Integer formType;

    /**
     * 表单编号
     */
    private Long formId; // 在表单类型为 {@link BpmModelFormTypeEnum#CUSTOM} 时，必须非空
    /**
     * 表单名字
     */
    private String formName;

    /**
     * 自定义表单的提交路径
     * example = "/bpm/oa/leave/create"
     */
    private String formCustomCreatePath; // 使用 Vue 的路由地址-在表单类型为 {@link BpmModelFormTypeEnum#CUSTOM} 时，必须非空
    /**
     * 自定义表单的查看路径
     * example = "/bpm/oa/leave/view"
     */
    private String formCustomViewPath; // ，使用 Vue 的路由地址-在表单类型为 {@link BpmModelFormTypeEnum#CUSTOM} 时，必须非空

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * BPMN XML
     */
    private String bpmnXml;

    /**
     * 最新部署的流程定义
     */
    private BpmProcessDefinitionRespVO processDefinition;

}
