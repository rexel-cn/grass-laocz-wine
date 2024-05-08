package com.rexel.bpm.convert.task;

import com.rexel.common.utils.NumberUtils;
import com.rexel.common.utils.PageResult;
import com.rexel.bpm.domain.dal.BpmProcessDefinitionInfoDO;
import com.rexel.bpm.domain.task.instance.BpmProcessInstanceRespVO;
import com.rexel.bpm.domain.vo.process.BpmProcessDefinitionRespVO;
import com.rexel.bpm.framework.flowable.core.event.BpmProcessInstanceStatusEvent;
import com.rexel.bpm.framework.flowable.core.util.FlowableUtils;
import com.rexel.common.core.domain.entity.SysUser;
import com.rexel.common.utils.BeanUtils;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Map;

/**
 * 流程实例 Convert
 *
 * @author 芋道源码
 */
@Mapper
public interface BpmProcessInstanceConvert {

    BpmProcessInstanceConvert INSTANCE = Mappers.getMapper(BpmProcessInstanceConvert.class);

    default PageResult<BpmProcessInstanceRespVO> buildProcessInstancePage(PageResult<HistoricProcessInstance> pageResult,
                                                                          Map<String, ProcessDefinition> processDefinitionMap,
                                                                          Map<String, List<Task>> taskMap,
                                                                          Map<Long, SysUser> userMap) {
        PageResult<BpmProcessInstanceRespVO> vpPageResult = BeanUtils.toBean(pageResult, BpmProcessInstanceRespVO.class);
        for (int i = 0; i < pageResult.getList().size(); i++) {
            BpmProcessInstanceRespVO respVO = vpPageResult.getList().get(i);
            respVO.setStatus(FlowableUtils.getProcessInstanceStatus(pageResult.getList().get(i)));
            respVO.setTasks(BeanUtils.toBean(taskMap.get(respVO.getId()), BpmProcessInstanceRespVO.Task.class));
            // user
            if (userMap != null) {
                SysUser startUser = userMap.get(NumberUtils.parseLong(pageResult.getList().get(i).getStartUserId()));
                if (startUser != null) {
                    respVO.setStartUser(BeanUtils.toBean(startUser, BpmProcessInstanceRespVO.User.class));
                }
            }
        }
        return vpPageResult;
    }

    default BpmProcessInstanceRespVO buildProcessInstance(HistoricProcessInstance processInstance,
                                                          ProcessDefinition processDefinition,
                                                          BpmProcessDefinitionInfoDO processDefinitionExt,
                                                          String bpmnXml,
                                                          SysUser startUser) {
        BpmProcessInstanceRespVO respVO = BeanUtils.toBean(processInstance, BpmProcessInstanceRespVO.class);
        respVO.setStatus(FlowableUtils.getProcessInstanceStatus(processInstance));
        respVO.setFormVariables(FlowableUtils.getProcessInstanceFormVariable(processInstance));
        // definition
        respVO.setProcessDefinition(BeanUtils.toBean(processDefinition, BpmProcessDefinitionRespVO.class));
        copyTo(processDefinitionExt, respVO.getProcessDefinition());
        respVO.getProcessDefinition().setBpmnXml(bpmnXml);
        // user
        if (startUser != null) {
            respVO.setStartUser(BeanUtils.toBean(startUser, BpmProcessInstanceRespVO.User.class));
        }
        return respVO;
    }

    @Mapping(source = "from.id", target = "to.id", ignore = true)
    void copyTo(BpmProcessDefinitionInfoDO from, @MappingTarget BpmProcessDefinitionRespVO to);

    default BpmProcessInstanceStatusEvent buildProcessInstanceStatusEvent(Object source, HistoricProcessInstance instance, Integer status) {
        return new BpmProcessInstanceStatusEvent(source).setId(instance.getId()).setStatus(status)
                .setProcessDefinitionKey(instance.getProcessDefinitionKey()).setBusinessKey(instance.getBusinessKey());
    }

    default BpmProcessInstanceStatusEvent buildProcessInstanceStatusEvent(Object source, ProcessInstance instance, Integer status) {;
        return new BpmProcessInstanceStatusEvent(source).setId(instance.getId()).setStatus(status)
                .setProcessDefinitionKey(instance.getProcessDefinitionKey()).setBusinessKey(instance.getBusinessKey());
    }

//    default BpmMessageSendWhenProcessInstanceApproveReqDTO buildProcessInstanceApproveMessage(ProcessInstance instance) {
//        return new BpmMessageSendWhenProcessInstanceApproveReqDTO()
//                .setStartUserId(NumberUtils.parseLong(instance.getStartUserId()))
//                .setProcessInstanceId(instance.getId())
//                .setProcessInstanceName(instance.getName());
//    }
//
//    default BpmMessageSendWhenProcessInstanceRejectReqDTO buildProcessInstanceRejectMessage(ProcessInstance instance, String reason) {
//        return new BpmMessageSendWhenProcessInstanceRejectReqDTO()
//            .setProcessInstanceName(instance.getName())
//            .setProcessInstanceId(instance.getId())
//            .setReason(reason)
//            .setStartUserId(NumberUtils.parseLong(instance.getStartUserId()));
//    }

}
