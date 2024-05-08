package com.rexel.bpm.domain.task.instance;

import com.rexel.bpm.domain.vo.process.BpmProcessDefinitionRespVO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
public class BpmProcessInstanceRespVO {

    /**
     * 流程实例的编号
     */
    private String id;

    /**
     * 流程名称
     */
    private String name;

    /**
     * 流程分类
     */
    private String category;
    /**
     * 流程分类名称
     */
    private String categoryName;

    /**
     * 流程实例的状态
     */
    private Integer status; // 参见 BpmProcessInstanceStatusEnum 枚举

    /**
     * 发起时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 持续时间
     */
    private Long durationInMillis;

    /**
     * 提交的表单值
     */
    private Map<String, Object> formVariables;

    /**
     * 业务的唯一标识-例如说，请假申请的编号
     */
    private String businessKey;

    /**
     * 发起流程的用户
     */
    private User startUser;

    /**
     * 流程定义的编号
     */
    private String processDefinitionId;
    /**
     * 流程定义
     */
    private BpmProcessDefinitionRespVO processDefinition;

    /**
     * 当前审批中的任务
     */
    private List<Task> tasks; // 仅在流程实例分页才返回

    /**
     * 用户信息
     */
    @Data
    public static class User {

        /**
         * 用户编号
         */
        private Long userId;
        /**
         * 用户昵称
         */
        private String userName;
    }

    /**
     * 流程任务
     */
    @Data
    public static class Task {

        /**
         * 流程任务的编号
         */
        private String id;

        /**
         * 任务名称
         */
        private String name;

    }

}
