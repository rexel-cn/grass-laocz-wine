package com.rexel.bpm;

import cn.hutool.core.collection.CollUtil;
import com.rexel.bpm.convert.definition.BpmModelConvert;
import com.rexel.bpm.domain.vo.model.*;
import com.rexel.bpm.service.definition.BpmModelService;
import com.rexel.bpm.service.definition.BpmProcessDefinitionService;
import com.rexel.common.core.controller.BaseController;
import com.rexel.common.core.domain.AjaxResult;
import com.rexel.common.core.page.TableDataInfo;
import com.rexel.common.utils.CollectionUtils;
import com.rexel.common.utils.PageResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.Model;
import org.flowable.engine.repository.ProcessDefinition;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.rexel.common.utils.CollectionUtils.convertMap;


@Tag(name = "管理后台 - 流程模型")
@RestController
@RequestMapping("/bpm/model")
@Validated
public class BpmModelController extends BaseController {

    @Resource
    private BpmModelService modelService;
    @Resource
    private BpmProcessDefinitionService processDefinitionService;

    @GetMapping("/page")
    public TableDataInfo list(BpmModelPageReqVO bpmModelPageReqVO) {
        PageResult<Model> modelPage = modelService.getModelPage(bpmModelPageReqVO);
        if (CollUtil.isEmpty(modelPage.getList())) {
            return getDataTable(modelPage.getList());
        }
        // 获得 Deployment Map
        Set<String> deploymentIds = new HashSet<>();
        modelPage.getList().forEach(model -> CollectionUtils.addIfNotNull(deploymentIds, model.getDeploymentId()));
        Map<String, Deployment> deploymentMap = processDefinitionService.getDeploymentMap(deploymentIds);
        // 获得 ProcessDefinition Map
        List<ProcessDefinition> processDefinitions = processDefinitionService.getProcessDefinitionListByDeploymentIds(deploymentIds);
        Map<String, ProcessDefinition> processDefinitionMap = convertMap(processDefinitions, ProcessDefinition::getDeploymentId);
        PageResult<BpmModelRespVO> bpmModelRespVOPageResult = BpmModelConvert.INSTANCE.buildModelPage(modelPage, deploymentMap, processDefinitionMap);

        return getDataTable(bpmModelRespVOPageResult.getList(),bpmModelRespVOPageResult.getTotal());
    }
    @GetMapping("/get")
    @Operation(summary = "获得模型")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public AjaxResult getModel(@RequestParam("id") String id) {
        Model model = modelService.getModel(id);
        if (model == null) {
            return null;
        }
        byte[] bpmnBytes = modelService.getModelBpmnXML(id);
        return success(BpmModelConvert.INSTANCE.buildModel(model, bpmnBytes));
    }

    @PostMapping("/create")
    @Operation(summary = "新建模型")
    public AjaxResult createModel(@Valid @RequestBody BpmModelCreateReqVO createRetVO) {
        return AjaxResult.success(modelService.createModel(createRetVO, null));
    }
    @PutMapping("/update")
    @Operation(summary = "修改模型")
    public AjaxResult updateModel(@Valid @RequestBody BpmModelUpdateReqVO modelVO) {
        modelService.updateModel(modelVO);
        return AjaxResult.success();
    }
    @PostMapping("/deploy")
    @Operation(summary = "部署模型")
    public AjaxResult deployModel(@RequestParam("id") String id) {
        modelService.deployModel(id);
        return AjaxResult.success();
    }

    @PutMapping("/update-state")
    @Operation(summary = "修改模型的状态", description = "实际更新的部署的流程定义的状态")
    public AjaxResult updateModelState(@Valid @RequestBody BpmModelUpdateStateReqVO reqVO) {
        modelService.updateModelState(reqVO.getId(), reqVO.getState());
        return AjaxResult.success();
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除模型")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public AjaxResult deleteModel(@RequestParam("id") String id) {
        modelService.deleteModel(id);
        return AjaxResult.success();
    }
}
