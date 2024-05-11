package com.rexel.laocz.bpm.service;

import com.rexel.bpm.domain.task.task.BpmTaskApproveReqVO;
import com.rexel.bpm.domain.task.task.BpmTaskPageReqVO;
import com.rexel.bpm.domain.task.task.BpmTaskRejectReqVO;
import com.rexel.common.utils.PageResult;
import com.rexel.laocz.domain.dto.TaskPageDTO;
import com.rexel.laocz.domain.vo.TaskPageVO;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;

import java.util.List;

/**
 * 流程任务实例 Service 接口
 *
 * @author 孟开通
 */
public interface LaoczBpmTaskService {

    /**
     * 获得待办的流程任务分页
     *
     * @param taskPageDTO 查询参数
     * @return 流程任务分页
     */
    PageResult<TaskPageVO> getTaskTodoPage(TaskPageDTO taskPageDTO);

    /**
     * 获得已办的流程任务分页
     *
     * @param taskPageDTO    查询参数
     * @return 流程任务分页
     */
    PageResult<TaskPageVO> getTaskDonePage(TaskPageDTO taskPageDTO);


    /**
     * 通过
     * @param reqVO
     */
    void approveTask(BpmTaskApproveReqVO reqVO);

    /**
     * 不通过
     * @param reqVO
     */
    void rejectTask(BpmTaskRejectReqVO reqVO);

    /**
     * 获取任务
     *
     * @param id 任务编号
     * @return 任务
     */
    Task getTask(String id);
}
