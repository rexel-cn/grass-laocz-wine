package com.rexel.bpm.framework.flowable.core.candidate;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.google.common.annotations.VisibleForTesting;
import com.rexel.bpm.constant.ErrorCodeConstants;
import com.rexel.bpm.framework.flowable.core.enums.BpmTaskCandidateStrategyEnum;
import com.rexel.bpm.framework.flowable.core.util.BpmnModelUtils;
import com.rexel.common.core.domain.entity.SysUser;
import com.rexel.common.enums.CommonStatusEnum;
import com.rexel.common.exception.CustomException;
import com.rexel.system.service.ISysUserService;
import com.rexel.system.service.impl.SysUserServiceImpl;
import com.rexel.user.ISysUserServiceFrameworkApi;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.delegate.DelegateExecution;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * {@link BpmTaskCandidateStrategy} 的调用者，用于调用对应的策略，实现任务的候选人的计算
 *
 * @author 芋道源码
 */
@Slf4j
public class BpmTaskCandidateInvoker {

    private final Map<BpmTaskCandidateStrategyEnum, BpmTaskCandidateStrategy> strategyMap = new HashMap<>();

    private final ISysUserService iSysUserService;

    public BpmTaskCandidateInvoker(List<BpmTaskCandidateStrategy> strategyList,
                                   ISysUserService iSysUserService) {
        strategyList.forEach(strategy -> {
            BpmTaskCandidateStrategy oldStrategy = strategyMap.put(strategy.getStrategy(), strategy);
            Assert.isNull(oldStrategy, "策略(%s) 重复", strategy.getStrategy());
        });
        this.iSysUserService = iSysUserService;
    }

    /**
     * 校验流程模型的任务分配规则全部都配置了
     * 目的：如果有规则未配置，会导致流程任务找不到负责人，进而流程无法进行下去！
     *
     * @param bpmnBytes BPMN XML
     */
    public void validateBpmnConfig(byte[] bpmnBytes) {
        BpmnModel bpmnModel = BpmnModelUtils.getBpmnModel(bpmnBytes);
        assert bpmnModel != null;
        List<UserTask> userTaskList = BpmnModelUtils.getBpmnModelElements(bpmnModel, UserTask.class);
        // 遍历所有的 UserTask，校验审批人配置
        userTaskList.forEach(userTask -> {
            // 1. 非空校验
            Integer strategy = BpmnModelUtils.parseCandidateStrategy(userTask);
            String param = BpmnModelUtils.parseCandidateParam(userTask);
            if (strategy == null) {
                throw new CustomException(ErrorCodeConstants.MODEL_DEPLOY_FAIL_TASK_CANDIDATE_NOT_CONFIG, userTask.getName());
            }
            BpmTaskCandidateStrategy candidateStrategy = getCandidateStrategy(strategy);
            if (candidateStrategy.isParamRequired() && StrUtil.isBlank(param)) {
                throw new CustomException(ErrorCodeConstants.MODEL_DEPLOY_FAIL_TASK_CANDIDATE_NOT_CONFIG, userTask.getName());
            }
            // 2. 具体策略校验
            getCandidateStrategy(strategy).validateParam(param);
        });
    }

    /**
     * 计算任务的候选人
     *
     * @param execution 执行任务
     * @return 用户编号集合
     */
    public Set<Long> calculateUsers(DelegateExecution execution) {
        Integer strategy = BpmnModelUtils.parseCandidateStrategy(execution.getCurrentFlowElement());
        String param = BpmnModelUtils.parseCandidateParam(execution.getCurrentFlowElement());
        // 1.1 计算任务的候选人
        Set<Long> userIds = getCandidateStrategy(strategy).calculateUsers(execution, param);
        // 1.2 移除被禁用的用户
        removeDisableUsers(userIds);

        // 2. 校验是否有候选人
        if (CollUtil.isEmpty(userIds)) {
            log.error("[calculateUsers][流程任务({}/{}/{}) 任务规则({}/{}) 找不到候选人]", execution.getId(),
                    execution.getProcessDefinitionId(), execution.getCurrentActivityId(), strategy, param);
            throw new CustomException(ErrorCodeConstants.TASK_CREATE_FAIL_NO_CANDIDATE_USER);
        }
        return userIds;
    }

    @VisibleForTesting
    void removeDisableUsers(Set<Long> assigneeUserIds) {
        if (CollUtil.isEmpty(assigneeUserIds)) {
            return;
        }
        Map<Long, SysUser> userMap = iSysUserService.getUserMap(assigneeUserIds);
        assigneeUserIds.removeIf(id -> {
            SysUser user = userMap.get(id);
            return user == null || !CommonStatusEnum.ENABLE.getStatus().equals(user.getStatus());
        });
    }

    private BpmTaskCandidateStrategy getCandidateStrategy(Integer strategy) {
        BpmTaskCandidateStrategyEnum strategyEnum = BpmTaskCandidateStrategyEnum.valueOf(strategy);
        Assert.notNull(strategyEnum, "策略(%s) 不存在", strategy);
        BpmTaskCandidateStrategy strategyObj = strategyMap.get(strategyEnum);
        Assert.notNull(strategyObj, "策略(%s) 不存在", strategy);
        return strategyObj;
    }

}
