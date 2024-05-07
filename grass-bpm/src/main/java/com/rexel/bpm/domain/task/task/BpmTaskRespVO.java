package com.rexel.bpm.domain.task.task;

import com.rexel.bpm.domain.task.instance.BpmProcessInstanceRespVO;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Accessors(chain = true)
public class BpmTaskRespVO {

    /**
     * 任务编号
     */
    private String id;

    /**
     * 任务名字
     */
    private String name;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 持续时间
     */
    private Long durationInMillis;

    /**
     * 任务状态
     */
    private Integer status; // 参见 BpmTaskStatusEnum 枚举

    /**
     * 审批理由
     */
    private String reason;

    /**
     * 负责人的用户信息
     */
    private BpmProcessInstanceRespVO.User ownerUser;
    /**
     * 审核的用户信息
     */
    private BpmProcessInstanceRespVO.User assigneeUser;

    /**
     * 任务定义的标识
     */
    private String taskDefinitionKey;

    /**
     * 所属流程实例编号
     */
    private String processInstanceId;
    /**
     * 所属流程实例
     */
    private ProcessInstance processInstance;

    /**
     * 父任务编号
     */
    private String parentTaskId;
    /**
     * 子任务列表（由加签生成）
     */
    private List<BpmTaskRespVO> children;

    /**
     * 表单编号
     */
    private Long formId;
    /**
     * 表单名字
     * example = "请假表单"
     */
    private String formName;
    /**
     * 表单的配置-JSON 字符串
     */
    private String formConf;
    /**
     * 表单项的数组
     */
    private List<String> formFields;
    /**
     * 提交的表单值
     */
    private Map<String, Object> formVariables;

    /**
     * 流程实例
     */
    @Data
    public static class ProcessInstance {

        /**
         * 流程实例编号
         */
        private String id;

        /**
         * 流程实例名称
         */
        private String name;

        /**
         * 提交时间
         */
        private LocalDateTime createTime;

        /**
         * 流程定义的编号
         */
        private String processDefinitionId;

        /**
         * 发起人的用户信息
         */
        private BpmProcessInstanceRespVO.User startUser;

    }

}
