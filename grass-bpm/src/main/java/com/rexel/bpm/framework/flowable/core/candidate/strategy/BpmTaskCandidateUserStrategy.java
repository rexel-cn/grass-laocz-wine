package com.rexel.bpm.framework.flowable.core.candidate.strategy;

import com.rexel.bpm.framework.flowable.core.candidate.BpmTaskCandidateStrategy;
import com.rexel.bpm.framework.flowable.core.enums.BpmTaskCandidateStrategyEnum;
import com.rexel.common.utils.StringUtils;
import com.rexel.system.service.ISysUserService;
import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

/**
 * 用户 {@link BpmTaskCandidateStrategy} 实现类
 *
 * @author kyle
 */
@Component
public class BpmTaskCandidateUserStrategy implements BpmTaskCandidateStrategy {

    @Resource
    private ISysUserService iSysUserService;

    @Override
    public BpmTaskCandidateStrategyEnum getStrategy() {
        return BpmTaskCandidateStrategyEnum.USER;
    }

    @Override
    public void validateParam(String param) {
        iSysUserService.validateUserList(StringUtils.splitToLongSet(param));
    }

    @Override
    public Set<Long> calculateUsers(DelegateExecution execution, String param) {
        return StringUtils.splitToLongSet(param);
    }

}
