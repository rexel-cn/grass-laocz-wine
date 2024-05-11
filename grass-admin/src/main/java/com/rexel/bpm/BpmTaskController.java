package com.rexel.bpm;

import cn.hutool.core.collection.CollUtil;
import com.rexel.bpm.convert.task.BpmTaskConvert;
import com.rexel.bpm.domain.task.task.BpmTaskApproveReqVO;
import com.rexel.bpm.domain.task.task.BpmTaskPageReqVO;
import com.rexel.bpm.domain.task.task.BpmTaskRejectReqVO;
import com.rexel.bpm.domain.task.task.BpmTaskRespVO;
import com.rexel.bpm.service.task.BpmProcessInstanceService;
import com.rexel.bpm.service.task.BpmTaskService;
import com.rexel.common.core.controller.BaseController;
import com.rexel.common.core.domain.AjaxResult;
import com.rexel.common.core.domain.entity.SysUser;
import com.rexel.common.core.page.TableDataInfo;
import com.rexel.common.utils.NumberUtils;
import com.rexel.common.utils.PageResult;
import com.rexel.common.utils.SecurityUtils;
import com.rexel.system.service.ISysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static com.rexel.common.utils.CollectionUtils.convertSet;
import static com.rexel.common.utils.CollectionUtils.convertSetByFlatMap;


@Tag(name = "管理后台 - 流程任务实例")
@RestController
@RequestMapping("/bpm/task")
@Validated
public class BpmTaskController extends BaseController {

    @Resource
    private BpmTaskService taskService;
    @Resource
    private BpmProcessInstanceService processInstanceService;
    @Resource
    private ISysUserService iSysUserService;

    @GetMapping("todo-page")
    @Operation(summary = "获取 Todo 待办任务分页")
    public TableDataInfo getTaskTodoPage(@Valid BpmTaskPageReqVO pageVO) {
        PageResult<Task> pageResult = taskService.getTaskTodoPage(SecurityUtils.getUserId(), pageVO);
        if (CollUtil.isEmpty(pageResult.getList())) {
            return getDataTable(pageResult.getList());
        }
        // 拼接数据
        Map<String, ProcessInstance> processInstanceMap = processInstanceService.getProcessInstanceMap(
                convertSet(pageResult.getList(), Task::getProcessInstanceId));
        Map<Long, SysUser> userMap = iSysUserService.getUserMap(
                convertSet(processInstanceMap.values(), instance -> Long.valueOf(instance.getStartUserId())));
        PageResult<BpmTaskRespVO> bpmTaskRespVOPageResult = BpmTaskConvert.INSTANCE.buildTodoTaskPage(pageResult, processInstanceMap, userMap);
        return getDataTable(bpmTaskRespVOPageResult.getList(), bpmTaskRespVOPageResult.getTotal());
    }

    @GetMapping("done-page")
    @Operation(summary = "获取 Done 已办任务分页")
    public TableDataInfo getTaskDonePage(@Valid BpmTaskPageReqVO pageVO) {
        PageResult<HistoricTaskInstance> pageResult = taskService.getTaskDonePage(SecurityUtils.getUserId(), pageVO);
        if (CollUtil.isEmpty(pageResult.getList())) {
            return getDataTable(pageResult.getList());
        }

        // 拼接数据
        Map<String, HistoricProcessInstance> processInstanceMap = processInstanceService.getHistoricProcessInstanceMap(
                convertSet(pageResult.getList(), HistoricTaskInstance::getProcessInstanceId));
        Map<Long, SysUser> userMap = iSysUserService.getUserMap(
                convertSet(processInstanceMap.values(), instance -> Long.valueOf(instance.getStartUserId())));
        PageResult<BpmTaskRespVO> bpmTaskRespVOPageResult = BpmTaskConvert.INSTANCE.buildTaskPage(pageResult, processInstanceMap, userMap);
        return getDataTable(bpmTaskRespVOPageResult.getList(), bpmTaskRespVOPageResult.getTotal());
    }

    @GetMapping("manager-page")
    @Operation(summary = "获取全部任务的分页", description = "用于【流程任务】菜单")
    public TableDataInfo getTaskManagerPage(@Valid BpmTaskPageReqVO pageVO) {
        PageResult<HistoricTaskInstance> pageResult = taskService.getTaskPage(SecurityUtils.getUserId(), pageVO);
        if (CollUtil.isEmpty(pageResult.getList())) {
            return getDataTable(pageResult.getList());
        }

        // 拼接数据
        Map<String, HistoricProcessInstance> processInstanceMap = processInstanceService.getHistoricProcessInstanceMap(
                convertSet(pageResult.getList(), HistoricTaskInstance::getProcessInstanceId));
        // 获得 User 和 Dept Map
        Set<Long> userIds = convertSet(processInstanceMap.values(), instance -> Long.valueOf(instance.getStartUserId()));
        userIds.addAll(convertSet(pageResult.getList(), task -> NumberUtils.parseLong(task.getAssignee())));
        Map<Long, SysUser> userMap = iSysUserService.getUserMap(userIds);
        PageResult<BpmTaskRespVO> bpmTaskRespVOPageResult = BpmTaskConvert.INSTANCE.buildTaskPage(pageResult, processInstanceMap, userMap);
        return getDataTable(bpmTaskRespVOPageResult.getList(), bpmTaskRespVOPageResult.getTotal());
    }

    @GetMapping("/list-by-process-instance-id")
    @Operation(summary = "获得指定流程实例的任务列表", description = "包括完成的、未完成的")
    @Parameter(name = "processInstanceId", description = "流程实例的编号", required = true)
    public AjaxResult getTaskListByProcessInstanceId(
            @RequestParam("processInstanceId") String processInstanceId) {
        List<HistoricTaskInstance> taskList = taskService.getTaskListByProcessInstanceId(processInstanceId);
        if (CollUtil.isEmpty(taskList)) {
            return success(Collections.emptyList());
        }

        // 拼接数据
        HistoricProcessInstance processInstance = processInstanceService.getHistoricProcessInstance(processInstanceId);
        // 获得 User 和 Dept Map
        Set<Long> userIds = convertSetByFlatMap(taskList, task ->
                Stream.of(NumberUtils.parseLong(task.getAssignee()), NumberUtils.parseLong(task.getOwner())));
        userIds.add(NumberUtils.parseLong(processInstance.getStartUserId()));
        Map<Long, SysUser> userMap = iSysUserService.getUserMap(userIds);
        return success(BpmTaskConvert.INSTANCE.buildTaskListByProcessInstanceId(taskList, processInstance, userMap));
    }

    @PutMapping("/approve")
    @Operation(summary = "通过任务")
    public AjaxResult approveTask(@Valid @RequestBody BpmTaskApproveReqVO reqVO) {
        taskService.approveTask(SecurityUtils.getUserId(), reqVO);
        return success(true);
    }

    @PutMapping("/reject")
    @Operation(summary = "不通过任务")
    public AjaxResult rejectTask(@Valid @RequestBody BpmTaskRejectReqVO reqVO) {
        taskService.rejectTask(SecurityUtils.getUserId(), reqVO);
        return success(true);
    }
}
