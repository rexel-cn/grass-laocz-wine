package com.rexel.laocz.bpm.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.rexel.bpm.domain.task.task.BpmTaskApproveReqVO;
import com.rexel.bpm.domain.task.task.BpmTaskRejectReqVO;
import com.rexel.bpm.enums.BpmCommentTypeEnum;
import com.rexel.bpm.enums.BpmTaskStatusEnum;
import com.rexel.bpm.framework.flowable.core.enums.BpmConstants;
import com.rexel.bpm.service.task.BpmProcessInstanceService;
import com.rexel.common.core.domain.entity.SysUser;
import com.rexel.common.core.page.PageDomain;
import com.rexel.common.core.page.TableSupport;
import com.rexel.common.exception.CustomException;
import com.rexel.common.utils.NumberUtils;
import com.rexel.common.utils.PageResult;
import com.rexel.common.utils.PageUtils;
import com.rexel.common.utils.SecurityUtils;
import com.rexel.laocz.bpm.service.LaoczBpmTaskService;
import com.rexel.laocz.domain.dto.TaskPageDTO;
import com.rexel.laocz.domain.vo.TaskPageVO;
import com.rexel.laocz.enums.OperationTypeEnum;
import com.rexel.system.service.ISysUserService;
import org.flowable.engine.HistoryService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.task.api.history.HistoricTaskInstanceQuery;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static com.rexel.laocz.constant.BpmWineVariablesConstants.*;

/**
 * @ClassName LaoczBpmTaskServiceImpl
 * @Description
 * @Author 孟开通
 * @Date 2024/5/9 15:02
 **/
@Service
public class LaoczBpmTaskServiceImpl implements LaoczBpmTaskService {
    @Resource
    private TaskService taskService;
    @Resource
    private HistoryService historyService;
    @Resource
    private ISysUserService iSysUserService;
    @Resource
    private BpmProcessInstanceService processInstanceService;

    /**
     * 获得待办的流程任务分页
     *
     * @param taskPageDTO 查询参数
     * @return 流程任务分页
     */
    @Override
    public PageResult<TaskPageVO> getTaskTodoPage(TaskPageDTO taskPageDTO) {
        TaskQuery taskQuery = taskService.createTaskQuery()
                .taskAssignee(String.valueOf(Objects.requireNonNull(SecurityUtils.getLoginUser()).getUserId())) // 分配给自己
                .active()
                .includeProcessVariables()
                .orderByTaskCreateTime().desc(); // 创建时间倒序
        if (StrUtil.isNotEmpty(taskPageDTO.getOperationType())) {
            taskQuery.processVariableValueEquals(OPERATION_TYPE, taskPageDTO.getOperationType());
        }
        if (StrUtil.isNotEmpty(taskPageDTO.getLiquorBatchId())) {
            taskQuery.processVariableValueLike(LIQUOR_BATCH_ID, "%" + taskPageDTO.getLiquorBatchId() + "%");
        }
        if (ObjectUtil.isNotNull(taskPageDTO.getBeginTime()) && ObjectUtil.isNotNull(taskPageDTO.getEndTime())) {
            taskQuery.taskCreatedAfter(taskPageDTO.getBeginTime());
            taskQuery.taskCreatedBefore(taskPageDTO.getEndTime());
        }
        long count = taskQuery.count();

        List<TaskPageVO> taskPageVOS = new ArrayList<>();
        PageDomain pageDomain = TableSupport.getPageDomain();
        List<Task> tasks = taskQuery.listPage(PageUtils.getStart(pageDomain), pageDomain.getPageSize());

        if (count == 0 || CollectionUtil.isEmpty(tasks)) {
            return PageResult.empty();
        }

        Map<String, HistoricProcessInstance> processInstanceMap = processInstanceService.getHistoricProcessInstanceMap(tasks.stream().map(Task::getProcessInstanceId).collect(Collectors.toSet()));

        Set<Long> userIds = processInstanceMap.values().stream().map(HistoricProcessInstance::getStartUserId).map(Long::parseLong).collect(Collectors.toSet());

        Map<Long, SysUser> userMap = iSysUserService.getUserMap(userIds);

        for (Task task : tasks) {
            TaskPageVO taskPageVO = new TaskPageVO();
            taskPageVO.setTaskId(task.getId());
            taskPageVO.setApplyTime(task.getCreateTime());
            String processInstanceId = task.getProcessInstanceId();
            taskPageVO.setProcessInstanceId(processInstanceId);

            if (processInstanceMap.containsKey(processInstanceId)) {
                HistoricProcessInstance historicProcessInstance = processInstanceMap.get(processInstanceId);
                String startUserId = historicProcessInstance.getStartUserId();
                if (userMap.containsKey(Long.parseLong(startUserId))) {
                    taskPageVO.setApplyUser(userMap.get(Long.parseLong(startUserId)).getUserName());
                }
            }
            Map<String, Object> processVariables = task.getProcessVariables();
            if (processVariables.containsKey(OPERATION_TYPE)) {
                taskPageVO.setOperationType(OperationTypeEnum.getNameByValue(Long.valueOf(processVariables.get(OPERATION_TYPE).toString()) ));
            }
            if (processVariables.containsKey(LIQUOR_BATCH_ID)) {
                String liquorBatchId = processVariables.get(LIQUOR_BATCH_ID).toString();
                taskPageVO.setLiquorBatchId(liquorBatchId);
            }
            if (processVariables.containsKey(LIQUOR_NAME)) {
                taskPageVO.setLiquorName(processVariables.get(LIQUOR_NAME).toString());
            }

            taskPageVOS.add(taskPageVO);
        }
        return new PageResult<>(taskPageVOS, count);
    }

    /**
     * 获得已办的流程任务分页
     *
     * @param taskPageDTO 查询参数
     * @return 流程任务分页
     */
    @Override
    public PageResult<TaskPageVO> getTaskDonePage(TaskPageDTO taskPageDTO) {

        HistoricTaskInstanceQuery taskQuery = historyService.createHistoricTaskInstanceQuery()
                .finished() // 已完成
                .taskAssignee(String.valueOf(Objects.requireNonNull(SecurityUtils.getLoginUser()).getUserId())) // 分配给自己
                .includeTaskLocalVariables()
                .orderByHistoricTaskInstanceEndTime().desc(); // 审批时间倒序
        if (StrUtil.isNotEmpty(taskPageDTO.getOperationType())) {
            taskQuery.processVariableValueEquals(OPERATION_TYPE, taskPageDTO.getOperationType());
        }
        if (StrUtil.isNotEmpty(taskPageDTO.getLiquorBatchId())) {
            taskQuery.processVariableValueLike(LIQUOR_BATCH_ID, "%" + taskPageDTO.getLiquorBatchId() + "%");
        }
        if (ObjectUtil.isNotNull(taskPageDTO.getBeginTime()) && ObjectUtil.isNotNull(taskPageDTO.getEndTime())) {
            taskQuery.taskCreatedAfter(taskPageDTO.getBeginTime());
            taskQuery.taskCreatedBefore(taskPageDTO.getEndTime());
        }
        // 执行查询
        long count = taskQuery.count();
        if (count == 0) {
            return PageResult.empty();
        }
        List<TaskPageVO> taskPageVOS = new ArrayList<>();
        PageDomain pageDomain = TableSupport.getPageDomain();
        List<HistoricTaskInstance> tasks = taskQuery.listPage(PageUtils.getStart(pageDomain), pageDomain.getPageSize());
        if (CollectionUtil.isEmpty(tasks)) {
            return PageResult.empty();
        }

        Map<String, HistoricProcessInstance> processInstanceMap = processInstanceService.getHistoricProcessInstanceMap(
                tasks.stream().map(HistoricTaskInstance::getProcessInstanceId).collect(Collectors.toSet()));
        Set<Long> userIds = processInstanceMap.values().stream().map(HistoricProcessInstance::getStartUserId).map(Long::parseLong).collect(Collectors.toSet());


        Map<Long, SysUser> userMap = iSysUserService.getUserMap(userIds);

        for (HistoricTaskInstance task : tasks) {
            TaskPageVO taskPageVO = new TaskPageVO();
            taskPageVO.setTaskId(task.getId());
            taskPageVO.setApplyTime(task.getCreateTime());

            String processInstanceId = task.getProcessInstanceId();
            taskPageVO.setProcessInstanceId(processInstanceId);
            if (processInstanceMap.containsKey(processInstanceId)) {
                HistoricProcessInstance historicProcessInstance = processInstanceMap.get(processInstanceId);
                String startUserId = historicProcessInstance.getStartUserId();
                if (userMap.containsKey(Long.parseLong(startUserId))) {
                    taskPageVO.setApplyUser(userMap.get(Long.parseLong(startUserId)).getUserName());
                }
            }
            Map<String, Object> processVariables = task.getTaskLocalVariables();
            if (processVariables.containsKey(OPERATION_TYPE)) {
                taskPageVO.setOperationType(OperationTypeEnum.getNameByValue(Long.valueOf(processVariables.get(OPERATION_TYPE).toString())));
            }
            if (processVariables.containsKey(LIQUOR_BATCH_ID)) {
                String liquorBatchId = processVariables.get(LIQUOR_BATCH_ID).toString();
                taskPageVO.setLiquorBatchId(liquorBatchId);
            }
            if (processVariables.containsKey(LIQUOR_NAME)) {
                taskPageVO.setLiquorName(processVariables.get(LIQUOR_NAME).toString());
            }
            taskPageVO.setStatus((Integer) processVariables.get(BpmConstants.TASK_VARIABLE_STATUS));
            taskPageVOS.add(taskPageVO);
        }
        return new PageResult<>(taskPageVOS, count);
    }

    @Override
    public void approveTask(BpmTaskApproveReqVO reqVO) {
        // 1.1 校验任务存在
        Task task = validateTask(SecurityUtils.getUserId(), reqVO.getId());
        // 1.2 校验流程实例存在
        ProcessInstance instance = processInstanceService.getProcessInstance(task.getProcessInstanceId());
        if (instance == null) {
            throw new CustomException("流程实例不存在");
        }


        //传递参数，方便页面查询（2024年5月10日 13:40:05 临时解决）
        taskService.setVariablesLocal(task.getId(), instance.getProcessVariables());


        // 情况三：审批普通的任务。大多数情况下，都是这样
        // 3.1 更新 task 状态、原因
        updateTaskStatusAndReason(task.getId(), BpmTaskStatusEnum.APPROVE.getStatus(), reqVO.getReason());

        // 3.3 调用 BPM complete 去完成任务
        taskService.complete(task.getId());
    }

    @Override
    public void rejectTask(BpmTaskRejectReqVO reqVO) {
        // 1.1 校验任务存在
        Task task = validateTask(SecurityUtils.getUserId(), reqVO.getId());
        // 1.2 校验流程实例存在
        ProcessInstance instance = processInstanceService.getProcessInstance(task.getProcessInstanceId());
        if (instance == null) {
            throw new CustomException("流程实例不存在");
        }

        //传递参数，方便页面查询（2024年5月10日 13:40:05 临时解决）
        taskService.setVariablesLocal(task.getId(), instance.getProcessVariables());


        // 2.1 更新流程实例为不通过
        updateTaskStatusAndReason(task.getId(), BpmTaskStatusEnum.REJECT.getStatus(), reqVO.getReason());
        // 2.2 添加评论
        taskService.addComment(task.getId(), task.getProcessInstanceId(), BpmCommentTypeEnum.REJECT.getType(),
                BpmCommentTypeEnum.REJECT.formatComment(reqVO.getReason()));

        // 3. 更新流程实例，审批不通过！
        processInstanceService.updateProcessInstanceReject(instance.getProcessInstanceId(), reqVO.getReason());
    }


    /**
     * 更新流程任务的 status 状态、reason 理由
     *
     * @param id     任务编号
     * @param status 状态
     * @param reason 理由（审批通过、审批不通过的理由）
     */
    private void updateTaskStatusAndReason(String id, Integer status, String reason) {
        updateTaskStatus(id, status);
        taskService.setVariableLocal(id, BpmConstants.TASK_VARIABLE_REASON, reason);
    }

    /**
     * 更新流程任务的 status 状态
     *
     * @param id     任务编号
     * @param status 状态
     */
    private void updateTaskStatus(String id, Integer status) {
        taskService.setVariableLocal(id, BpmConstants.TASK_VARIABLE_STATUS, status);
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
}
