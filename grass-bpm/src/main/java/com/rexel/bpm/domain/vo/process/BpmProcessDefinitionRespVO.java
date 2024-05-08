package com.rexel.bpm.domain.vo.process;

import com.rexel.bpm.enums.BpmModelFormTypeEnum;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class BpmProcessDefinitionRespVO {

    /**
     * 编号
     */
    private String id;

    /**
     * 版本
     */
    private Integer version;

    /**
     * 流程名称
     */
    private String name;

    /**
     * 流程标识
     */
    private String key;

    /**
     * 流程图标
     */
    private String icon;

    /**
     * 流程描述
     */
    private String description;

    /**
     * 流程分类
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
     * 表单编号-在表单类型为 {@link BpmModelFormTypeEnum#CUSTOM} 时，必须非空
     */
    private Long formId;
    /**
     * 表单名字
     */
    private String formName;
    /**
     * 表单的配置-JSON 字符串。在表单类型为 {@link BpmModelFormTypeEnum#CUSTOM} 时，必须非空
     */
    private String formConf;
    /**
     * 表单项的数组-JSON 字符串的数组。在表单类型为 {@link BpmModelFormTypeEnum#CUSTOM} 时，必须非空
     */
    private List<String> formFields;
    /**
     * 自定义表单的提交路径，使用 Vue 的路由地址-在表单类型为 {@link BpmModelFormTypeEnum#CUSTOM} 时，必须非空
     * example = "/bpm/oa/leave/create"
     */
    private String formCustomCreatePath;
    /**
     * 自定义表单的查看路径，使用 Vue 的路由地址-在表单类型为 {@link BpmModelFormTypeEnum#CUSTOM} 时，必须非空
     * example = "/bpm/oa/leave/view"
     */
    private String formCustomViewPath;

    /**
     * 中断状态-参见 SuspensionState 枚举
     */
    private Integer suspensionState; // 参见 SuspensionState 枚举

    /**
     * 部署时间
     */
    private LocalDateTime deploymentTime; // 需要从对应的 Deployment 读取，非必须返回

    /**
     * BPMN XML
     */
    private String bpmnXml; // 需要从对应的 BpmnModel 读取，非必须返回

    /**
     * 发起用户需要选择审批人的任务数组
     */
    private List<UserTask> startUserSelectTasks; // 需要从对应的 BpmnModel 读取，非必须返回

    /**
     * BPMN UserTask 用户任务
     */
    @Data
    public static class UserTask {

        /**
         * 任务标识
         */
        private String id;

        /**
         * 任务名
         */
        private String name;

    }

}
