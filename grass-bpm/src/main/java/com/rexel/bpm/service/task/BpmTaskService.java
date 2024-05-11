package com.rexel.bpm.service.task;

import com.rexel.bpm.domain.task.task.BpmTaskApproveReqVO;
import com.rexel.bpm.domain.task.task.BpmTaskPageReqVO;
import com.rexel.bpm.domain.task.task.BpmTaskRejectReqVO;
import com.rexel.common.utils.CollectionUtils;
import com.rexel.common.utils.PageResult;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 流程任务实例 Service 接口
 *
 * @author jason
 * @author 芋道源码
 */
public interface BpmTaskService {

    /**
     * 获得待办的流程任务分页
     *
     * @param userId    用户编号
     * @param pageReqVO 分页请求
     * @return 流程任务分页
     */
    PageResult<Task> getTaskTodoPage(Long userId, BpmTaskPageReqVO pageReqVO);

    /**
     * 获得已办的流程任务分页
     *
     * @param userId    用户编号
     * @param pageReqVO 分页请求
     * @return 流程任务分页
     */
    PageResult<HistoricTaskInstance> getTaskDonePage(Long userId, BpmTaskPageReqVO pageReqVO);

    /**
     * 获得全部的流程任务分页
     *
     * @param userId    用户编号
     * @param pageReqVO 分页请求
     * @return 流程任务分页
     */
    PageResult<HistoricTaskInstance> getTaskPage(Long userId, BpmTaskPageReqVO pageReqVO);

    /**
     * 获得流程任务 Map
     *
     * @param processInstanceIds 流程实例的编号数组
     * @return 流程任务 Map
     */
    default Map<String, List<Task>> getTaskMapByProcessInstanceIds(List<String> processInstanceIds) {
        return CollectionUtils.convertMultiMap(getTasksByProcessInstanceIds(processInstanceIds),
                Task::getProcessInstanceId);
    }

    /**
     * 获得流程任务列表
     *
     * @param processInstanceIds 流程实例的编号数组
     * @return 流程任务列表
     */
    List<Task> getTasksByProcessInstanceIds(List<String> processInstanceIds);

    /**
     * 获得指定流程实例的流程任务列表，包括所有状态的
     *
     * @param processInstanceId 流程实例的编号
     * @return 流程任务列表
     */
    List<HistoricTaskInstance> getTaskListByProcessInstanceId(String processInstanceId);

    /**
     * 通过任务
     *
     * @param userId 用户编号
     * @param reqVO  通过请求
     */
    void approveTask(Long userId, @Valid BpmTaskApproveReqVO reqVO);

    /**
     * 不通过任务
     *
     * @param userId 用户编号
     * @param reqVO  不通过请求
     */
    void rejectTask(Long userId, @Valid BpmTaskRejectReqVO reqVO);

//    /**
//     * 将流程任务分配给指定用户
//     *
//     * @param userId 用户编号
//     * @param reqVO  分配请求
//     */
//    void transferTask(Long userId, BpmTaskTransferReqVO reqVO);

    /**
     * 更新 Task 状态，在创建时
     *
     * @param task 任务实体
     */
    void updateTaskStatusWhenCreated(Task task);

    /**
     * 更新 Task 状态，在取消时
     *
     * @param taskId 任务的编号
     */
    void updateTaskStatusWhenCanceled(String taskId);

    /**
     * 更新 Task 拓展记录，并发送通知
     *
     * @param task 任务实体
     */
    void updateTaskExtAssign(Task task);

    /**
     * 获取任务
     *
     * @param id 任务编号
     * @return 任务
     */
    Task getTask(String id);




}
