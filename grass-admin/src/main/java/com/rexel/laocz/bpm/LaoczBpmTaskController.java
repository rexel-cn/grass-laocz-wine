package com.rexel.laocz.bpm;

import com.rexel.bpm.domain.task.task.BpmTaskApproveReqVO;
import com.rexel.bpm.domain.task.task.BpmTaskRejectReqVO;
import com.rexel.common.core.controller.BaseController;
import com.rexel.common.core.domain.AjaxResult;
import com.rexel.common.core.page.TableDataInfo;
import com.rexel.common.utils.PageResult;
import com.rexel.common.utils.SecurityUtils;
import com.rexel.laocz.bpm.service.LaoczBpmTaskService;
import com.rexel.laocz.domain.dto.TaskPageDTO;
import com.rexel.laocz.domain.vo.TaskPageVO;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @ClassName LaoczBpmTaskController
 * @Description 流程任务
 * @Author 孟开通
 * @Date 2024/5/9 16:03
 **/
@RestController
@RequestMapping("/rexel-api/laocz-bpm/task")
public class LaoczBpmTaskController extends BaseController {

    @Autowired
    private LaoczBpmTaskService laoczBpmTaskService;

    /**
     * 获得待办的流程任务分页
     *
     * @param taskPageDTO 查询参数
     * @return 流程任务分页
     */
    @GetMapping("/todo/page")
    public TableDataInfo getTaskTodoPage(TaskPageDTO taskPageDTO) {
        PageResult<TaskPageVO> taskTodoPage = laoczBpmTaskService.getTaskTodoPage(taskPageDTO);
        return getDataTable(taskTodoPage.getList(),"bpmToDoPage", taskTodoPage.getTotal());
    }

    /**
     * 获取已办任务分页
     * @param taskPageDTO
     * @return
     */
    @GetMapping("/done/page")
    public TableDataInfo getTaskDonePage(TaskPageDTO taskPageDTO) {
        PageResult<TaskPageVO> taskDonePage = laoczBpmTaskService.getTaskDonePage(taskPageDTO);
        return getDataTable(taskDonePage.getList(),"bpmDonePage", taskDonePage.getTotal());
    }



    @PutMapping("/approve")
    @Operation(summary = "通过任务")
    public AjaxResult approveTask(@Valid @RequestBody BpmTaskApproveReqVO reqVO) {
        laoczBpmTaskService.approveTask(reqVO);
        return success(true);
    }

    @PutMapping("/reject")
    @Operation(summary = "不通过任务")
    public AjaxResult rejectTask(@Valid @RequestBody BpmTaskRejectReqVO reqVO) {
        laoczBpmTaskService.rejectTask(reqVO);
        return success(true);
    }
}
