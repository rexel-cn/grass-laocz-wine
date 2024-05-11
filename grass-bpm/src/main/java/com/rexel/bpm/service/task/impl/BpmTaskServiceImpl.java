package com.rexel.bpm.service.task.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.rexel.bpm.domain.task.task.BpmTaskApproveReqVO;
import com.rexel.bpm.domain.task.task.BpmTaskPageReqVO;
import com.rexel.bpm.domain.task.task.BpmTaskRejectReqVO;
import com.rexel.bpm.framework.flowable.core.enums.BpmConstants;
import com.rexel.bpm.framework.flowable.core.util.FlowableUtils;
import com.rexel.bpm.service.definition.BpmModelService;
import com.rexel.common.core.page.PageDomain;
import com.rexel.common.core.page.TableSupport;
import com.rexel.common.exception.CustomException;
import com.rexel.common.utils.DateUtils;
import com.rexel.common.utils.NumberUtils;
import com.rexel.bpm.enums.BpmCommentTypeEnum;
import com.rexel.bpm.enums.BpmDeleteReasonEnum;
import com.rexel.bpm.enums.BpmTaskStatusEnum;
import com.rexel.bpm.service.task.BpmProcessInstanceService;
import com.rexel.bpm.service.task.BpmTaskService;
import com.rexel.common.utils.PageResult;
import com.rexel.common.utils.PageUtils;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.HistoryService;
import org.flowable.engine.ManagementService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.task.api.history.HistoricTaskInstanceQuery;
import org.flowable.task.service.impl.persistence.entity.TaskEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.*;

import static com.rexel.common.utils.CollectionUtils.*;


/**
 * 流程任务实例 Service 实现类
 *
 * @author 芋道源码
 * @author jason
 */
@Slf4j
@Service
public class BpmTaskServiceImpl implements BpmTaskService {

    @Resource
    private TaskService taskService;
    @Resource
    private HistoryService historyService;
    @Resource
    private ManagementService managementService;

    @Resource
    private BpmProcessInstanceService processInstanceService;


    @Override
    public PageResult<Task> getTaskTodoPage(Long userId, BpmTaskPageReqVO pageVO) {
        TaskQuery taskQuery = taskService.createTaskQuery()
                .taskAssignee(String.valueOf(userId)) // 分配给自己
                .active()
                .includeProcessVariables()
                .orderByTaskCreateTime().desc(); // 创建时间倒序
        if (StrUtil.isNotBlank(pageVO.getName())) {
            taskQuery.taskNameLike("%" + pageVO.getName() + "%");
        }
        if (ArrayUtil.isNotEmpty(pageVO.getCreateTime())) {
            taskQuery.taskCreatedAfter(DateUtils.of(pageVO.getCreateTime()[0]));
            taskQuery.taskCreatedAfter(DateUtils.of(pageVO.getCreateTime()[1]));
        }
        long count = taskQuery.count();
        if (count == 0) {
            return PageResult.empty();
        }
        PageDomain pageDomain = TableSupport.getPageDomain();
        List<Task> tasks = taskQuery.listPage(PageUtils.getStart(pageDomain), pageDomain.getPageSize());
        return new PageResult<>(tasks, count);
    }

    @Override
    public PageResult<HistoricTaskInstance> getTaskDonePage(Long userId, BpmTaskPageReqVO pageVO) {
        HistoricTaskInstanceQuery taskQuery = historyService.createHistoricTaskInstanceQuery()
                .finished() // 已完成
                .taskAssignee(String.valueOf(userId)) // 分配给自己
                .includeTaskLocalVariables()
                .orderByHistoricTaskInstanceEndTime().desc(); // 审批时间倒序
        if (StrUtil.isNotBlank(pageVO.getName())) {
            taskQuery.taskNameLike("%" + pageVO.getName() + "%");
        }
        if (ArrayUtil.isNotEmpty(pageVO.getCreateTime())) {
            taskQuery.taskCreatedAfter(DateUtils.of(pageVO.getCreateTime()[0]));
            taskQuery.taskCreatedAfter(DateUtils.of(pageVO.getCreateTime()[1]));
        }
        // 执行查询
        long count = taskQuery.count();
        if (count == 0) {
            return PageResult.empty();
        }
        PageDomain pageDomain = TableSupport.getPageDomain();
        List<HistoricTaskInstance> tasks = taskQuery.listPage(PageUtils.getStart(pageDomain), pageDomain.getPageSize());
        return new PageResult<>(tasks, count);
    }

    @Override
    public PageResult<HistoricTaskInstance> getTaskPage(Long userId, BpmTaskPageReqVO pageVO) {
        HistoricTaskInstanceQuery taskQuery = historyService.createHistoricTaskInstanceQuery()
                .includeTaskLocalVariables()
                .taskTenantId(FlowableUtils.getTenantId())
                .orderByHistoricTaskInstanceEndTime().desc(); // 审批时间倒序
        if (StrUtil.isNotBlank(pageVO.getName())) {
            taskQuery.taskNameLike("%" + pageVO.getName() + "%");
        }
        if (ArrayUtil.isNotEmpty(pageVO.getCreateTime())) {
            taskQuery.taskCreatedAfter(DateUtils.of(pageVO.getCreateTime()[0]));
            taskQuery.taskCreatedAfter(DateUtils.of(pageVO.getCreateTime()[1]));
        }
        // 执行查询
        long count = taskQuery.count();
        if (count == 0) {
            return PageResult.empty();
        }
        PageDomain pageDomain = TableSupport.getPageDomain();
        List<HistoricTaskInstance> tasks = taskQuery.listPage(PageUtils.getStart(pageDomain), pageDomain.getPageSize());
        return new PageResult<>(tasks, count);
    }

    @Override
    public List<Task> getTasksByProcessInstanceIds(List<String> processInstanceIds) {
        if (CollUtil.isEmpty(processInstanceIds)) {
            return Collections.emptyList();
        }
        return taskService.createTaskQuery().processInstanceIdIn(processInstanceIds).list();
    }

    @Override
    public List<HistoricTaskInstance> getTaskListByProcessInstanceId(String processInstanceId) {
        List<HistoricTaskInstance> tasks = historyService.createHistoricTaskInstanceQuery()
                .includeTaskLocalVariables()
                .processInstanceId(processInstanceId)
                .orderByHistoricTaskInstanceStartTime().desc() // 创建时间倒序
                .list();
        if (CollUtil.isEmpty(tasks)) {
            return Collections.emptyList();
        }
        return tasks;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void approveTask(Long userId, @Valid BpmTaskApproveReqVO reqVO) {
        // 1.1 校验任务存在
        Task task = validateTask(userId, reqVO.getId());
        // 1.2 校验流程实例存在
        ProcessInstance instance = processInstanceService.getProcessInstance(task.getProcessInstanceId());
        if (instance == null) {
            throw new CustomException("流程实例不存在");
        }


        //传递参数，方便页面查询（2024年5月10日 13:40:05 临时解决）
        taskService.setVariablesLocal(task.getId(),instance.getProcessVariables());


        // 情况三：审批普通的任务。大多数情况下，都是这样
        // 3.1 更新 task 状态、原因
        updateTaskStatusAndReason(task.getId(), BpmTaskStatusEnum.APPROVE.getStatus(), reqVO.getReason());

        // 3.2 添加评论
        taskService.addComment(task.getId(), task.getProcessInstanceId(), BpmCommentTypeEnum.APPROVE.getType(),
                BpmCommentTypeEnum.APPROVE.formatComment(reqVO.getReason()));
        // 3.3 调用 BPM complete 去完成任务
        // 其中，variables 是存储动态表单到 local 任务级别。过滤一下，避免 ProcessInstance 系统级的变量被占用
        if (CollUtil.isNotEmpty(reqVO.getVariables())) {
            Map<String, Object> variables = FlowableUtils.filterTaskFormVariable(reqVO.getVariables());
            taskService.complete(task.getId(), variables, true);
        } else {
            taskService.complete(task.getId());
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void rejectTask(Long userId, @Valid BpmTaskRejectReqVO reqVO) {
        // 1.1 校验任务存在
        Task task = validateTask(userId, reqVO.getId());
        // 1.2 校验流程实例存在
        ProcessInstance instance = processInstanceService.getProcessInstance(task.getProcessInstanceId());
        if (instance == null) {
            throw new CustomException("流程实例不存在");
        }

        //传递参数，方便页面查询（2024年5月10日 13:40:05 临时解决）
        taskService.setVariablesLocal(task.getId(),instance.getProcessVariables());


        // 2.1 更新流程实例为不通过
        updateTaskStatusAndReason(task.getId(), BpmTaskStatusEnum.REJECT.getStatus(), reqVO.getReason());
        // 2.2 添加评论
        taskService.addComment(task.getId(), task.getProcessInstanceId(), BpmCommentTypeEnum.REJECT.getType(),
                BpmCommentTypeEnum.REJECT.formatComment(reqVO.getReason()));

        // 3. 更新流程实例，审批不通过！
        processInstanceService.updateProcessInstanceReject(instance.getProcessInstanceId(), reqVO.getReason());
    }

    /**
     * 更新流程任务的 status 状态
     *
     * @param id    任务编号
     * @param status 状态
     */
    private void updateTaskStatus(String id, Integer status) {
        taskService.setVariableLocal(id, BpmConstants.TASK_VARIABLE_STATUS, status);
    }

    /**
     * 更新流程任务的 status 状态、reason 理由
     *
     * @param id 任务编号
     * @param status 状态
     * @param reason 理由（审批通过、审批不通过的理由）
     */
    private void updateTaskStatusAndReason(String id, Integer status, String reason) {
        updateTaskStatus(id, status);
        taskService.setVariableLocal(id, BpmConstants.TASK_VARIABLE_REASON, reason);
    }

    /**
     * 校验任务是否存在，并且是否是分配给自己的任务
     *
     * @param userId 用户 id
     * @param taskId task id
     */
    private Task validateTask(Long userId, String taskId) {
        Task task = validateTaskExist(taskId);
        if (!Objects.equals(userId, NumberUtils.parseLong(task.getAssignee()))) {
            throw new CustomException("操作失败，原因：该任务的审批人不是你");
        }
        return task;
    }

    @Override
    public void updateTaskStatusWhenCreated(Task task) {
        Integer status = (Integer) task.getTaskLocalVariables().get(BpmConstants.TASK_VARIABLE_STATUS);
        if (status != null) {
            log.error("[updateTaskStatusWhenCreated][taskId({}) 已经有状态({})]", task.getId(), status);
            return;
        }
        updateTaskStatus(task.getId(), BpmTaskStatusEnum.RUNNING.getStatus());
    }

    @Override
    public void updateTaskStatusWhenCanceled(String taskId) {
        Task task = getTask(taskId);
        // 1. 可能只是活动，不是任务，所以查询不到
        if (task == null) {
            log.error("[updateTaskStatusWhenCanceled][taskId({}) 任务不存在]", taskId);
            return;
        }

        // 2. 更新 task 状态 + 原因
        Integer status = (Integer) task.getTaskLocalVariables().get(BpmConstants.TASK_VARIABLE_STATUS);
        if (BpmTaskStatusEnum.isEndStatus(status)) {
            log.error("[updateTaskStatusWhenCanceled][taskId({}) 处于结果({})，无需进行更新]", taskId, status);
            return;
        }
        updateTaskStatusAndReason(taskId, BpmTaskStatusEnum.CANCEL.getStatus(), BpmDeleteReasonEnum.CANCEL_BY_SYSTEM.getReason());
        // 补充说明：由于 Task 被删除成 HistoricTask 后，无法通过 taskService.addComment 添加理由，所以无法存储具体的取消理由
    }

    @Override
    public void updateTaskExtAssign(Task task) {
        // 发送通知。在事务提交时，批量执行操作，所以直接查询会无法查询到 ProcessInstance，所以这里是通过监听事务的提交来实现。
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {

            @Override
            public void afterCommit() {
                if (StrUtil.isEmpty(task.getAssignee())) {
                    return;
                }
                ProcessInstance processInstance = processInstanceService.getProcessInstance(task.getProcessInstanceId());
                //AdminUserRespDTO startUser = adminUserApi.getUser(Long.valueOf(processInstance.getStartUserId()));
                //messageService.sendMessageWhenTaskAssigned(BpmTaskConvert.INSTANCE.convert(processInstance, startUser, task));
            }

        });
    }

    private Task validateTaskExist(String id) {
        Task task = getTask(id);
        if (task == null) {
            throw new CustomException("流程任务不存在");
        }
        return task;
    }

    @Override
    public Task getTask(String id) {
        return taskService.createTaskQuery().taskId(id).includeTaskLocalVariables().singleResult();
    }
    /**
     * 获取子任务个数
     *
     * @param parentTaskId 父任务 ID
     * @return 剩余子任务个数
     */
    private Long getTaskCountByParentTaskId(String parentTaskId) {
        String tableName = managementService.getTableName(TaskEntity.class);
        String sql = "SELECT COUNT(1) from " + tableName + " WHERE PARENT_TASK_ID_=#{parentTaskId}";
        return taskService.createNativeTaskQuery().sql(sql).parameter("parentTaskId", parentTaskId).count();
    }
}
