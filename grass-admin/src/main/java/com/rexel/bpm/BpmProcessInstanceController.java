package com.rexel.bpm;

import cn.hutool.core.collection.CollUtil;
import com.rexel.bpm.convert.task.BpmProcessInstanceConvert;
import com.rexel.bpm.domain.task.instance.BpmProcessInstanceCreateReqVO;
import com.rexel.common.utils.PageResult;
import com.rexel.bpm.domain.dal.BpmProcessDefinitionInfoDO;
import com.rexel.bpm.domain.task.instance.BpmProcessInstanceCancelReqVO;
import com.rexel.bpm.domain.task.instance.BpmProcessInstancePageReqVO;
import com.rexel.bpm.domain.task.instance.BpmProcessInstanceRespVO;
import com.rexel.bpm.framework.flowable.core.util.BpmnModelUtils;
import com.rexel.bpm.service.definition.BpmProcessDefinitionService;
import com.rexel.bpm.service.task.BpmProcessInstanceService;
import com.rexel.bpm.service.task.BpmTaskService;
import com.rexel.common.core.controller.BaseController;
import com.rexel.common.core.domain.AjaxResult;
import com.rexel.common.core.domain.entity.SysUser;
import com.rexel.common.core.page.TableDataInfo;
import com.rexel.common.utils.NumberUtils;
import com.rexel.common.utils.SecurityUtils;
import com.rexel.system.service.ISysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.task.api.Task;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

import static com.rexel.common.utils.CollectionUtils.convertList;
import static com.rexel.common.utils.CollectionUtils.convertSet;


@Tag(name = "管理后台 - 流程实例") // 流程实例，通过流程定义创建的一次“申请”
@RestController
@RequestMapping("/bpm/process-instance")
@Validated
public class BpmProcessInstanceController extends BaseController {

    @Resource
    private BpmProcessInstanceService processInstanceService;
    @Resource
    private BpmTaskService taskService;
    @Resource
    private BpmProcessDefinitionService processDefinitionService;
    @Resource
    private ISysUserService iSysUserService;
    @GetMapping("/test")
    public AjaxResult test(){
        return AjaxResult.success(111);
    }

    @GetMapping("/my-page")
    @Operation(summary = "获得我的实例分页列表", description = "在【我的流程】菜单中，进行调用")
    public TableDataInfo getProcessInstanceMyPage(
            @Valid BpmProcessInstancePageReqVO pageReqVO) {
        PageResult<HistoricProcessInstance> pageResult = processInstanceService.getProcessInstancePage(
                SecurityUtils.getUserId(), pageReqVO);
        if (CollUtil.isEmpty(pageResult.getList())) {
            return getDataTable(pageResult.getList());
        }
        // 拼接返回
        Map<String, List<Task>> taskMap = taskService.getTaskMapByProcessInstanceIds(
                convertList(pageResult.getList(), HistoricProcessInstance::getId));
        Map<String, ProcessDefinition> processDefinitionMap = processDefinitionService.getProcessDefinitionMap(
                convertSet(pageResult.getList(), HistoricProcessInstance::getProcessDefinitionId));
        PageResult<BpmProcessInstanceRespVO> bpmProcessInstanceRespVOPageResult = BpmProcessInstanceConvert.INSTANCE.buildProcessInstancePage(pageResult,
                processDefinitionMap, taskMap, null);
        return getDataTable(bpmProcessInstanceRespVOPageResult.getList(), bpmProcessInstanceRespVOPageResult.getTotal());
    }

    @GetMapping("/manager-page")
    @Operation(summary = "获得管理流程实例的分页列表", description = "在【流程实例】菜单中，进行调用")
    public TableDataInfo getProcessInstanceManagerPage(
            @Valid BpmProcessInstancePageReqVO pageReqVO) {
        PageResult<HistoricProcessInstance> pageResult = processInstanceService.getProcessInstancePage(
                null, pageReqVO);
        if (CollUtil.isEmpty(pageResult.getList())) {
            return getDataTable(pageResult.getList());
        }

        // 拼接返回
        Map<String, List<Task>> taskMap = taskService.getTaskMapByProcessInstanceIds(
                convertList(pageResult.getList(), HistoricProcessInstance::getId));
        Map<String, ProcessDefinition> processDefinitionMap = processDefinitionService.getProcessDefinitionMap(
                convertSet(pageResult.getList(), HistoricProcessInstance::getProcessDefinitionId));
        // 发起人信息
        Map<Long, SysUser> userMap = iSysUserService.getUserMap(
                convertSet(pageResult.getList(), processInstance -> NumberUtils.parseLong(processInstance.getStartUserId())));
        PageResult<BpmProcessInstanceRespVO> bpmProcessInstanceRespVOPageResult = BpmProcessInstanceConvert.INSTANCE.buildProcessInstancePage(pageResult,
                processDefinitionMap, taskMap, userMap);

        return getDataTable(bpmProcessInstanceRespVOPageResult.getList(), bpmProcessInstanceRespVOPageResult.getTotal());
    }


    @GetMapping("/get")
    @Operation(summary = "获得指定流程实例", description = "在【流程详细】界面中，进行调用")
    @Parameter(name = "id", description = "流程实例的编号", required = true)
    public AjaxResult getProcessInstance(@RequestParam("id") String id) {
        HistoricProcessInstance processInstance = processInstanceService.getHistoricProcessInstance(id);
        if (processInstance == null) {
            return AjaxResult.success();
        }
        // 拼接返回
        ProcessDefinition processDefinition = processDefinitionService.getProcessDefinition(
                processInstance.getProcessDefinitionId());
        BpmProcessDefinitionInfoDO processDefinitionInfo = processDefinitionService.getProcessDefinitionInfo(
                processInstance.getProcessDefinitionId());
        String bpmnXml = BpmnModelUtils.getBpmnXml(
                processDefinitionService.getProcessDefinitionBpmnModel(processInstance.getProcessDefinitionId()));
        SysUser user = iSysUserService.getUser(NumberUtils.parseLong(processInstance.getStartUserId()));
        BpmProcessInstanceRespVO bpmProcessInstanceRespVO = BpmProcessInstanceConvert.INSTANCE.buildProcessInstance(processInstance,
                processDefinition, processDefinitionInfo, bpmnXml, user);
        return AjaxResult.success(bpmProcessInstanceRespVO);
    }

    @DeleteMapping("/cancel-by-start-user")
    @Operation(summary = "用户取消流程实例", description = "取消发起的流程")
    public AjaxResult cancelProcessInstanceByStartUser(
            @Valid @RequestBody BpmProcessInstanceCancelReqVO cancelReqVO) {
        processInstanceService.cancelProcessInstanceByStartUser(SecurityUtils.getUserId(), cancelReqVO);
        return AjaxResult.success();
    }
    @PostMapping("/create")
    @Operation(summary = "新建流程实例")
    public AjaxResult createProcessInstance(@Valid @RequestBody BpmProcessInstanceCreateReqVO createReqVO) {
        return success(processInstanceService.createProcessInstance(createReqVO));
    }

}
